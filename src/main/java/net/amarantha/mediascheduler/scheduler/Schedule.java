package net.amarantha.mediascheduler.scheduler;

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

    List<MediaEvent> getUniqueEvents() {
        Set<Long> ids = new HashSet<>();
        List<MediaEvent> result = new ArrayList<>();
        for ( Entry<DayOfWeek, Map<LocalDate, List<MediaEvent>>> dowEntry : allEvents.entrySet() ) {
            for ( Entry<LocalDate, List<MediaEvent>> dateEntry : dowEntry.getValue().entrySet() ) {
                for ( MediaEvent event : dateEntry.getValue() ) {
                    if ( !ids.contains(event.getId()) ) {
                        result.add(event);
                        ids.add(event.getId());
                    }
                }
            }
        }
        return result;
    }

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

    List<MediaEvent> getEventsByCueList(CueList cueList) {
        List<MediaEvent> result = new ArrayList<>();
        for ( Entry<DayOfWeek, Map<LocalDate, List<MediaEvent>>> dowEntry : allEvents.entrySet() ) {
            for ( Entry<LocalDate, List<MediaEvent>> dateEntry : dowEntry.getValue().entrySet() ) {
                List<MediaEvent> eventList = dateEntry.getValue();
                for ( MediaEvent event : eventList ) {
                    if ( event.getCueList().equals(cueList) ) {
                        result.add(event);
                    }
                }
            }
        }
        return result;
    }


    ////////////////
    // Add Events //
    ////////////////

    void addEvent(MediaEvent event) throws ScheduleConflictException {
        checkConflicts(event);
        if ( event.isRepeating() ) {
            // Repeating event
            for ( DayOfWeek dow : event.getRepeatOn() ) {
                addEventToMap(event, dow, null);
            }
        } else {
            // One-off event
            addEventToMap(event, event.getStartDate().getDayOfWeek(), event.getStartDate());
        }
    }

    private void checkConflicts(MediaEvent event) throws ScheduleConflictException {
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
    }

    private boolean isConflict(MediaEvent thisEvent, MediaEvent otherEvent) {
        if ( timesOverlap(thisEvent, otherEvent) ) {
            if ( thisEvent.isRepeating() || otherEvent.isRepeating() ) {
                Set<DayOfWeek> common = commonDays(thisEvent, otherEvent);
                return !common.isEmpty();
            } else {
                return thisEvent.getStartDate().equals(otherEvent.getStartDate());
            }
        }
        return false;
    }

    private Set<DayOfWeek> commonDays(MediaEvent thisEvent, MediaEvent otherEvent) {
        Set<DayOfWeek> result = new HashSet<>();
        DayOfWeek thisStartDay = thisEvent.getStartDate().getDayOfWeek();
        DayOfWeek otherStartDay = otherEvent.getStartDate().getDayOfWeek();
        if ( otherEvent.getRepeatOn().contains(thisStartDay) ) {
            result.add(thisStartDay);
        }
        if ( thisEvent.getRepeatOn().contains(otherStartDay) ) {
            result.add(otherStartDay);
        }
        for ( DayOfWeek dow : DayOfWeek.values() ) {
            if ( thisEvent.getRepeatOn().contains(dow) && otherEvent.getRepeatOn().contains(dow) ) {
                result.add(dow);
            }
        }
        return result;
    }

    private boolean timesOverlap(MediaEvent thisEvent, MediaEvent otherEvent) {
        LocalTime thisStart = thisEvent.getStartTime();
        LocalTime thisEnd = thisEvent.getEndTime();
        LocalTime otherStart = otherEvent.getStartTime();
        LocalTime otherEnd = otherEvent.getEndTime();
        return (
                           (thisStart.compareTo(otherStart) <= 0 && thisEnd.compareTo(otherStart) > 0)
                        || (thisStart.compareTo(otherStart) > 0 && thisEnd.compareTo(otherEnd) < 0)
                        || (thisStart.compareTo(otherEnd) < 0 && thisEnd.compareTo(otherEnd) >= 0 )
                );
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

}
