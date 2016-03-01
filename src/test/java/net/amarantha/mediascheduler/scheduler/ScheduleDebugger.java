package net.amarantha.mediascheduler.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ScheduleDebugger {

    public static void printEvents(Schedule schedule, LocalDate date) {
        printEvents(schedule, date, date);
    }

    public static void printEvents(Schedule schedule, LocalDate from, LocalDate to) {
        Map<LocalDate, List<MediaEvent>> events = schedule.getEvents(from, to);
        for ( Map.Entry<LocalDate, List<MediaEvent>> entry : events.entrySet() ) {
            System.out.println(entry.getKey().getDayOfWeek() + " " + entry.getKey());
            if ( entry.getValue().isEmpty() ) {
                System.out.println("\t(none)");
            } else {
                for (MediaEvent event : entry.getValue()) {
                    System.out.println("\t"+event.getStartTime() + " - " + event.getEndTime() + " : " + event.getCueListId());
                }
            }
        }
    }


}

