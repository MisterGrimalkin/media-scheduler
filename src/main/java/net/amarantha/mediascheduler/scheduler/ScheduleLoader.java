package net.amarantha.mediascheduler.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.mediascheduler.entity.MediaEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Singleton
public class ScheduleLoader {

    @Inject private Scheduler scheduler;

    public ScheduleLoader() {}

    private class ScheduleWrapper {
        public final int priority;
        public final List<MediaEvent> events;
        private ScheduleWrapper(int priority, List<MediaEvent> events) {
            this.priority = priority;
            this.events = events;
        }
    }




    public void saveSchedules() {
        System.out.println(getSchedulesJson());
    }

    public String getSchedulesJson() {

        Map<Integer, Schedule> schedules = scheduler.getSchedules();
        List<ScheduleWrapper> wrappers = new LinkedList<>();
        for ( Entry<Integer, Schedule> entry : schedules.entrySet() ) {
            List<MediaEvent> allEvents = entry.getValue().getUniqueEvents();
            wrappers.add(new ScheduleWrapper(entry.getKey(), allEvents));
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

            return mapper.writeValueAsString(wrappers);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

    }



}
