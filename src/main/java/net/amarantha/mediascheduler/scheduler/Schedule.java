package net.amarantha.mediascheduler.scheduler;

import net.amarantha.mediascheduler.entity.MediaEvent;
import net.amarantha.mediascheduler.exception.ScheduleConflictException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Schedule {

    private Map<DayOfWeek, Map<LocalDate, List<MediaEvent>>> allEvents;

    public Schedule() {
        allEvents = new LinkedHashMap<>();
        for ( DayOfWeek dow : DayOfWeek.values() ) {
            allEvents.put(dow, new HashMap<>());
        }
    }


    ////////////////
    // Get Events //
    ////////////////

    public MediaEvent getEvent(LocalDateTime dateTime) {
        return getEvent(dateTime.toLocalDate(), dateTime.toLocalTime());
    }

    public MediaEvent getEvent(LocalDate date, LocalTime time) {
        List<MediaEvent> events = getEvents(date);
        for ( MediaEvent event : events ) {
            if ( event.getStartTime().compareTo(time)<=0 && event.getEndTime().compareTo(time)>0 ) {
                return event;
            }
        }
        return null;
    }

    public Map<LocalDate, List<MediaEvent>> getEvents(LocalDate from, LocalDate to) {
        Map<LocalDate, List<MediaEvent>> result = new LinkedHashMap<>();
        for ( LocalDate d = from; d.compareTo(to)<=0; d = d.plusDays(1) ) {
            result.put(d, getEvents(d));
        }
        return result;
    }

    public List<MediaEvent> getEvents(LocalDate date) {
        List<MediaEvent> result = new LinkedList<>();
        Map<LocalDate, List<MediaEvent>> dowMap = allEvents.get(date.getDayOfWeek());
        List<MediaEvent> dateEvents = dowMap.get(date);
        if ( dateEvents!=null ) {
            result.addAll(dateEvents);
        }
        List<MediaEvent> repeatEvents = dowMap.get(null);
        if ( repeatEvents!=null ) {
            result.addAll(repeatEvents.stream()
                    .filter(event -> event.getStartDate().compareTo(date) <= 0)
                    .collect(Collectors.toList()));
        }
        Collections.sort(result);
        return result;
    }


    ////////////////
    // Add Events //
    ////////////////

    void addEvent(MediaEvent event) throws ScheduleConflictException {
        checkConflicts(event);
        if ( event.getRepeatOn().isEmpty() ) {
            // One-off event
            addEventToMap(event, event.getStartDate().getDayOfWeek(), event.getStartDate());
        } else {
            // Repeating event
            for ( DayOfWeek dow : event.getRepeatOn() ) {
                addEventToMap(event, dow, null);
            }
        }
    }

    private void checkConflicts(MediaEvent event) throws ScheduleConflictException {
    }

    private void addEventToMap(MediaEvent event, DayOfWeek dow, LocalDate date) {
        Map<LocalDate, List<MediaEvent>> dowMap = allEvents.get(dow);
        List<MediaEvent> events = dowMap.get(date);
        if ( events == null ) {
            events = new LinkedList<>();
            dowMap.put(date, events);
        }
        events.add(event);
        Collections.sort(events);
    }


    ///////////////
    // Debugging //
    ///////////////

    public void printEvents(LocalDate date) {
        printEvents(date, date);
    }

    public void printEvents(LocalDate from, LocalDate to) {
        Map<LocalDate, List<MediaEvent>> events = getEvents(from, to);
        for ( Entry<LocalDate, List<MediaEvent>> entry : events.entrySet() ) {
            System.out.println(entry.getKey().getDayOfWeek() + " " + entry.getKey());
            if ( entry.getValue().isEmpty() ) {
                System.out.println("\t(none)");
            } else {
                for (MediaEvent event : entry.getValue()) {
                    System.out.println("\t"+event.getStartTime() + " - " + event.getEndTime() + " : " + event.getCueList().getName());
                }
            }
        }
    }

}
