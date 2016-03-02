package net.amarantha.mediascheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.exception.CueNotFoundException;
import net.amarantha.mediascheduler.exception.ScheduleConflictException;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
import net.amarantha.mediascheduler.scheduler.MediaEvent;
import net.amarantha.mediascheduler.scheduler.Scheduler;
import net.amarantha.mediascheduler.utility.Now;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Path("schedule")
public class ScheduleResource extends Resource {

    private static Now now;
    private static Scheduler scheduler;
    private static JsonEncoder json;

    public ScheduleResource() {}

    @Inject
    public ScheduleResource(Now now, Scheduler scheduler, JsonEncoder json) {
        ScheduleResource.now = now;
        ScheduleResource.scheduler = scheduler;
        ScheduleResource.json = json;
    }

    @GET
    @Path("date")
    public Response getDate() {
        return ok(now.date().toString());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("time")
    public Response getTime() {
        return ok(now.time().toString());
    }

    @GET
    @Path("all")
    public Response get() {
        return ok(json.encodeAllSchedules());
    }

    @GET
    public Response getForDate(@QueryParam("date") String date) {
        return ok(json.encodeSchedule(1, LocalDate.parse(date)));
    }

    @GET
    @Path("event")
    public Response getEvent(@QueryParam("id") int id) {
        MediaEvent event = scheduler.getEventById(id);
        if ( event!=null ) {
            String j = json.encodeMediaEvent(event);
            if ( j!=null ) {
                return ok(j);
            }
        }
        return error("Event not found");
    }

    @POST
    @Path("add")
    public Response createEvent(String content) {
        MediaEvent event = json.decodeMediaEvent(content);
        try {
            scheduler.addEvent(event);
        } catch (Exception e) {
            return error(e.getMessage());
        }
        return ok("Event created");
    }

    @POST
    @Path("update")
    public Response updateEvent(String content) {
        MediaEvent event = json.decodeMediaEvent(content);
        MediaEvent existing = scheduler.getEventById(event.getId());
        if ( existing == null ) {
            return error("Event not found");
        }
        scheduler.removeEvent(existing.getId());
        try {
            scheduler.addEvent(event);
        } catch (Exception e) {
            try {
                scheduler.addEvent(existing);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return error(e.getMessage());
        }
        return ok("Event created");
    }

    @POST
    @Path("remove")
    public Response removeEvent(String content) {
        boolean wasDeleted;
        try {
            int id = Integer.parseInt(content);
            wasDeleted = scheduler.removeEvent(id);
        } catch (Exception e) {
            return error(e.getMessage());
        }
        return ok(wasDeleted?"Event removed":"Event not found");
    }

}
