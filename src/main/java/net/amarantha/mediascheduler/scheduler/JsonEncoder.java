package net.amarantha.mediascheduler.scheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

@Singleton
public class JsonEncoder {

    @Inject private Scheduler scheduler;

    public JsonEncoder() {}

    private class ScheduleWrapper {
        public final int priority;
        public final List<MediaEvent> events;
        private ScheduleWrapper(int priority, List<MediaEvent> events) {
            this.priority = priority;
            this.events = events;
        }
    }

    public String encodeAllSchedules() {

        Map<Integer, Schedule> schedules = scheduler.getSchedules();
        List<ScheduleWrapper> wrappers = new LinkedList<>();
        for ( Entry<Integer, Schedule> entry : schedules.entrySet() ) {
            List<MediaEvent> allEvents = entry.getValue().getUniqueEvents();
            wrappers.add(new ScheduleWrapper(entry.getKey(), allEvents));
        }

        try {
            return createMapper().writeValueAsString(wrappers);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

    }

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

    public String encodeCues() {
        try {
            return createMapper().writeValueAsString(scheduler.getCues());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void encodeCuesToFile(String filename) {
        try {
            createMapper().writeValue(new File(filename), scheduler.getCues());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public String encodeMediaEvent(MediaEvent event) {
        try {
            return createMapper().writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }



    public MediaEvent decodeMediaEvent(String json) {
        try {
            MediaEvent event = createMapper().readValue(json, MediaEvent.class);
            if ( event.getId() == -1 ) {
                event.setId(Scheduler.nextEventId++);
            }
            return event;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public void saveSchedules() {
        System.out.println(encodeAllSchedules());
    }

}
