package net.amarantha.scheduler.scheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.cue.HttpCue;
import net.amarantha.scheduler.cue.MidiCue;
import net.amarantha.scheduler.exception.ScheduleConflictException;
import net.amarantha.scheduler.http.Param;
import net.amarantha.scheduler.midi.MidiCommand;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.grizzly.http.Method;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

import static net.amarantha.scheduler.scheduler.JsonArrayBuilder.jsonArray;
import static net.amarantha.scheduler.scheduler.JsonBuilder.json;

@Singleton
public class JsonEncoderImpl implements JsonEncoder {

    @Inject private Scheduler scheduler;
    @Inject private Injector injector;

    public JsonEncoderImpl() {}

    private static class ScheduleWrapper {
        public final int priority;
        public final List<MediaEvent> events;
        @JsonCreator
        private ScheduleWrapper(@JsonProperty("priority") int priority, @JsonProperty("events") List<MediaEvent> events) {
            this.priority = priority;
            this.events = events;
        }
    }

    private List<ScheduleWrapper> buildWrappers() {
        Map<Integer, Schedule> schedules = scheduler.getSchedules();
        List<ScheduleWrapper> wrappers = new LinkedList<>();
        for ( Entry<Integer, Schedule> entry : schedules.entrySet() ) {
            List<MediaEvent> allEvents = entry.getValue().getUniqueEvents();
            wrappers.add(new ScheduleWrapper(entry.getKey(), allEvents));
        }
        return wrappers;
    }

    ///////////////
    // Schedules //
    ///////////////

    @Override
    public String encodeAllSchedules() {
        try {
            return createMapper().writeValueAsString(buildWrappers());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void encodeAllSchedulesToFile(String filename) {
        try {
            createMapper().writeValue(new File(filename), buildWrappers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<Integer, Schedule> decodeSchedulesFromFile(String filename) {
        Map<Integer, Schedule> result = new HashMap<>();
        try {
            List<ScheduleWrapper> wrappers = createMapper().readValue(new File(filename), new TypeReference<List<ScheduleWrapper>>(){});
            for ( ScheduleWrapper wrapper : wrappers ) {
                Schedule schedule = new Schedule();
                for ( MediaEvent event : wrapper.events ) {
                    try {
                        schedule.addEvent(event);
                    } catch (ScheduleConflictException e) {
                        e.printStackTrace();
                    }
                }
                result.put(wrapper.priority, schedule);
            }
        } catch (IOException e) {
            e.printStackTrace();
            encodeAllSchedulesToFile(filename);
        }
        return result;
    }

    @Override
    public String encodeSchedule(int priority, LocalDate date) {
        Schedule schedule = scheduler.getSchedules().get(priority);
        if ( schedule!=null ) {
            List<MediaEvent> events = schedule.getEvents(date);
            try {
                return createMapper().writeValueAsString(events);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //////////
    // Cues //
    //////////

    @Override
    public String encodeCues() {
        String cuesJson =
            json().put("cues",
                jsonArray().from(scheduler.getCues(), this::encodeCue)
            ).toJsonString();
        return cuesJson;
    }

    @Override
    public JsonBuilder encodeCue(Cue cue) {
        JsonBuilder cueJson =
                json()
                        .put("class", cue.getClass().getSimpleName())
                        .put("id", cue.getId())
                        .put("name", cue.getName());

        if (cue instanceof HttpCue) {
            HttpCue httpCue = (HttpCue) cue;
            cueJson
                    .put("path", httpCue.getPath())
                    .put("payload", httpCue.getPayload()==null ? "" : httpCue.getPayload())
                    .put("method", httpCue.getMethod())
                    .put("hosts",
                            jsonArray().from(httpCue.getHosts(), (host) ->
                                    json().put("ip", host)
                            ))
                    .put("params",
                            jsonArray().from(httpCue.getParams(), (param) ->
                                    json().put("name", param.getName())
                                            .put("value", param.getValue())
                            ));

        } else if ( cue instanceof MidiCue ) {
            MidiCommand command = ((MidiCue)cue).getCommand();
            cueJson
                    .put("command", command.getCommand())
                    .put("channel", command.getChannel())
                    .put("data1", command.getData1())
                    .put("data2", command.getData2());
        }

        return cueJson;
    }

    @Override
    public void encodeCuesToFile(String filename) {
        try (FileWriter output = new FileWriter(filename)) {
            output.write(encodeCues());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Cue> decodeCues(String json) {
        Set<Cue> result = new HashSet<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray cuesArray = (JSONArray) jsonObject.get("cues");
            for ( int i=0; i<cuesArray.length(); i++ ) {
                JSONObject cueObj = cuesArray.getJSONObject(i);
                Cue cue = decodeCue(cueObj);
                result.add(cue);
            }
        } catch (JSONException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Cue decodeCue(JSONObject cueObj) throws JSONException, ClassNotFoundException {
        String className = "net.amarantha.scheduler.cue." + cueObj.get("class").toString();
        Class<? extends Cue> cueClass = (Class<? extends Cue>) Class.forName(className);
        Cue cue = injector.getInstance(cueClass);
        cue.setId(cueObj.getInt("id"));
        cue.setName(cueObj.getString("name"));
        if ( cue instanceof HttpCue ) {
            HttpCue httpCue = (HttpCue)cue;
            httpCue.setPath(cueObj.getString("path"));
            httpCue.setPayload(cueObj.getString("payload"));
            httpCue.setMethod(Method.valueOf(cueObj.getString("method")));
            JSONArray hostsArr = (JSONArray)cueObj.get("hosts");
            for ( int j=0; j<hostsArr.length(); j++ ) {
                JSONObject hostObj = hostsArr.getJSONObject(j);
                httpCue.addHost(hostObj.getString("ip"));
            }
            JSONArray paramArr = (JSONArray)cueObj.get("params");
            for ( int j=0; j<paramArr.length(); j++ ) {
                JSONObject paramObj = paramArr.getJSONObject(j);
                httpCue.addParam(new Param(paramObj.getString("name"), paramObj.getString("value")));
            }

        } else if (cue instanceof MidiCue) {
            MidiCue midiCue = (MidiCue) cue;
            Integer command = cueObj.getInt("command");
            Integer channel = cueObj.getInt("channel");
            Integer data1 = cueObj.getInt("data1");
            Integer data2 = cueObj.getInt("data2");
            midiCue.setCommand(new MidiCommand(command, channel+1, data1, data2));
        }
        return cue;
    }

    @Override
    public Set<Cue> decodeCuesFromFile(String filename) {
        Set<Cue> result = new HashSet<>();
        try {
            String json = new String(Files.readAllBytes(Paths.get(filename)));
            result = decodeCues(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String encodeMediaEvent(MediaEvent event) throws JsonProcessingException {
        return createMapper().writeValueAsString(event);
    }

    @Override
    public MediaEvent decodeMediaEvent(String json) throws IOException {
        MediaEvent event = createMapper().readValue(json, MediaEvent.class);
        if ( event.getId() == -1 ) {
            event.setId(Scheduler.nextEventId++);
        }
        return event;
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return mapper;
    }

    @Override
    public void saveSchedules() {
        System.out.println(encodeAllSchedules());
    }

}
