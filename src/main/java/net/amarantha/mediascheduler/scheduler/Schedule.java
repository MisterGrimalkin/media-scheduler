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
        if ( event.getRepeatOn().isEmpty() ) {
            for ( Entry<DayOfWeek, Map<LocalDate, List<MediaEvent>>> dowEntry : allEvents.entrySet() ) {
                for ( Entry<LocalDate, List<MediaEvent>> dateEntry : dowEntry.getValue().entrySet() ) {
                    List<MediaEvent> eventList = dateEntry.getValue();
                    for ( MediaEvent otherEvent : eventList ) {
                        if ( isConflict(event, otherEvent) ) {
                            throw new ScheduleConflictException(otherEvent);
                        }
                    }
                }
            }
        } else {
            // need to figure out how to handle repeats
        }
    }

    private boolean isConflict(MediaEvent thisEvent, MediaEvent otherEvent) {
        if ( thisEvent.getStartDate().equals(otherEvent.getStartDate()) ) {
            LocalTime thisStart = thisEvent.getStartTime();
            LocalTime thisEnd = thisEvent.getEndTime();
            LocalTime otherStart = otherEvent.getStartTime();
            LocalTime otherEnd = otherEvent.getEndTime();
            if (
                       (thisStart.compareTo(otherStart) <= 0 && thisEnd.compareTo(otherStart) > 0)
                    || (thisStart.compareTo(otherStart) > 0 && thisEnd.compareTo(otherEnd) < 0)
                    || (thisStart.compareTo(otherEnd) < 0 && thisEnd.compareTo(otherEnd) >= 0 )
            ) {
                return true;
            }

        }
        return false;
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


    ///////////////////
    // Remove Events //
    ///////////////////

    MediaEvent getEventById(long eventId) {
        for ( Entry<DayOfWeek, Map<LocalDate, List<MediaEvent>>> dowEntry : allEvents.entrySet() ) {
            for ( Entry<LocalDate, List<MediaEvent>> dateEntry : dowEntry.getValue().entrySet() ) {
                List<MediaEvent> eventList = dateEntry.getValue();
                for ( MediaEvent event : eventList ) {
                    if ( event.getId()==eventId ) {
                        return event;
                    }
                }
            }
        }
        return null;
    }

    boolean removeEvent(long eventId) {
        for ( Entry<DayOfWeek, Map<LocalDate, List<MediaEvent>>> dowEntry : allEvents.entrySet() ) {
            for ( Entry<LocalDate, List<MediaEvent>> dateEntry : dowEntry.getValue().entrySet() ) {
                List<MediaEvent> eventList = dateEntry.getValue();
                for ( MediaEvent event : eventList ) {
                    if ( event.getId()==eventId ) {
                        return eventList.remove(event);
                    }
                }
            }
        }
        return false;
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
