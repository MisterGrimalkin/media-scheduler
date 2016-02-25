package net.amarantha.mediascheduler.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.mediascheduler.device.ArKaos;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.entity.CueList;
import net.amarantha.mediascheduler.entity.MediaEvent;
import net.amarantha.mediascheduler.exception.DuplicateEventException;
import net.amarantha.mediascheduler.exception.ScheduleConflictException;
import net.amarantha.mediascheduler.exception.SchedulerException;
import net.amarantha.mediascheduler.utility.Now;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class Scheduler {

    private List<MediaEvent> events = new ArrayList<>();
    private List<CueList> cueLists;

    @Inject private ArKaos mediaServer;
    @Inject private Projector projector;

    public Scheduler() {
    }

    public MediaEvent addEvent(MediaEvent event) throws SchedulerException {
        checkConflicts(event);
        events.add(event);
        return event;
    }

    private boolean checkConflicts(MediaEvent newEvent) throws SchedulerException {
        for ( MediaEvent event : events ) {
//            if ( event.getDescription().equals(newEvent.getDescription()) ) {
//                throw new DuplicateEventException("Event description '" + newEvent.getDescription() + "' already in use");
//            }
//            if ( event.getStartTime().before(newEvent.getStartTime()) && event.getEndTime().after(newEvent.getStartTime()) ) {
//                throw new ScheduleConflictException("Conflicts with event " + event.getId() + " " + event.getDescription());
//            }
        }
        return false;
    }

    private static long nextId = 1;

//    public MediaEvent createEvent(int cuelist, String description, String start, String end) throws SchedulerException {
//        return addEvent(new MediaEvent(nextId++, cuelist, description, Now.parseDateTime(start), Now.parseDateTime(end)));
//    }

    public List<MediaEvent> getEvents() {
        return events;
    }

    public void startup() {
        mediaServer.open();
        projector.switchOn(true);
    }

    public void shutdown() {
        mediaServer.close();
        projector.switchOn(false);
    }


}
