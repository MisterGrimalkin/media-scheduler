package net.amarantha.scheduler.scheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.cue.HttpCue;
import net.amarantha.scheduler.cue.MidiCue;
import net.amarantha.scheduler.exception.ScheduleConflictException;
import net.amarantha.scheduler.midi.MidiCommand;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

import static net.amarantha.scheduler.scheduler.JsonArrayBuilder.*;
import static net.amarantha.scheduler.scheduler.JsonBuilder.*;

@Singleton
public class JsonEncoderImpl implements JsonEncoder {

    @Inject private Scheduler scheduler;

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
        return
            json().put("cues",
                jsonArray().from(scheduler.getCues(), (cue) -> {

                    JsonBuilder cueJson =
                        json()
                            .put("class", cue.getClass().getSimpleName())
                            .put("id", cue.getId())
                            .put("name", cue.getName());

                    if (cue instanceof HttpCue) {
                        HttpCue httpCue = (HttpCue) cue;
                        cueJson
                            .put("path", httpCue.getPath())
                            .put("payload", httpCue.getPayload())
                            .put("method", httpCue.getMethod())
                            .put("hosts",
                                jsonArray().from(httpCue.getHosts(), (host) ->
                                    json().put("ip", host)
                                ))
                            .put("params",
                                jsonArray().from(httpCue.getParams(), (param) ->
                                    json().put("name", param.getName())
                                          .put("value", param.getValue())
                                )
                            );

                    } else if ( cue instanceof MidiCue ) {
                        MidiCommand command = ((MidiCue)cue).getCommand();
                        cueJson
                            .put("command", command.getCommand())
                            .put("channel", command.getChannel())
                            .put("data1", command.getData1())
                            .put("data2", command.getData2());
                    }

                    return cueJson;
                })
            ).toJsonString();
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
    public Cue decodeCue(String json) {


        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray cuesArray = (JSONArray)jsonObject.get("cues");
            System.out.println(cuesArray);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            Cue cue = createMapper().readValue(json, Cue.class);
            if ( cue.getId()==-1 ) {
                cue.setId(Scheduler.nextCueId++);
            }
            return cue;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<Cue> decodeCuesFromFile(String filename) {
        Set<Cue> result = new HashSet<>();
        try {
            List<Cue> cues = createMapper().readValue(new File(filename), new TypeReference<List<Cue>>(){});
            if ( cues!=null ) {
                for (Cue cue : cues) {
                    if (cue.getId() == -1) {
                        cue.setId(Scheduler.nextCueId++);
                    }
                    result.add(cue);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            encodeCuesToFile(filename);
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
