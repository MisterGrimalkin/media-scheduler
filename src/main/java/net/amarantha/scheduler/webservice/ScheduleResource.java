package net.amarantha.scheduler.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import net.amarantha.scheduler.scheduler.JsonEncoder;
import net.amarantha.scheduler.scheduler.MediaEvent;
import net.amarantha.scheduler.scheduler.Scheduler;
import net.amarantha.scheduler.utility.Now;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
            try {
                return ok(json.encodeMediaEvent(event));
            } catch (JsonProcessingException e) {
                return error(e.getMessage());
            }
        }
        return error("Event not found");
    }

    @POST
    @Path("add")
    public Response createEvent(String content) {
        try {
            MediaEvent event = json.decodeMediaEvent(content);
            scheduler.addEvent(event);
        } catch (Exception e) {
            return error(e.getMessage());
        }
        return ok("Event created");
    }

    @POST
    @Path("update")
    public Response updateEvent(String content) {
        MediaEvent newEvent;
        try {
            newEvent = json.decodeMediaEvent(content);
        } catch (IOException e) {
            return error(e.getMessage());
        }
        MediaEvent existing = scheduler.getEventById(newEvent.getId());
        if ( existing == null ) {
            return error("Event not found");
        }
        scheduler.removeEvent(existing.getId());
        try {
            scheduler.addEvent(newEvent);
        } catch (Exception e) {
            String msg = e.getMessage();
            try {
                scheduler.addEvent(existing);
            } catch (Exception e1) {
                msg += " " + e1.getMessage();
            }
            return error(msg);
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
