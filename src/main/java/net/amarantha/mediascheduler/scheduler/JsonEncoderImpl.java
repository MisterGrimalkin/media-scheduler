package net.amarantha.mediascheduler.scheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.mediascheduler.cue.Cue;
import net.amarantha.mediascheduler.exception.ScheduleConflictException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

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

    @Override
    public String encodeCues() {
        try {
            return createMapper().writeValueAsString(scheduler.getCues());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void encodeCuesToFile(String filename) {
        try {
            createMapper().writeValue(new File(filename), scheduler.getCues());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cue decodeCue(String json) {
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
