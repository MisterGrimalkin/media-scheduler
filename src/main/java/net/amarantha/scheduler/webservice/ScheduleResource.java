package net.amarantha.scheduler.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import net.amarantha.scheduler.scheduler.JsonEncoder;
import net.amarantha.scheduler.scheduler.MediaEvent;
import net.amarantha.scheduler.scheduler.Scheduler;
import net.amarantha.scheduler.showtime.ShowTime;
import net.amarantha.scheduler.showtime.ShowTimeManager;
import net.amarantha.scheduler.utility.Now;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.LocalDate;

@Path("/")
public class ScheduleResource extends Resource {

    private static Now now;
    private static Scheduler scheduler;
    private static JsonEncoder json;
    private static ShowTimeManager showTimeManager;

    public ScheduleResource() {}

    @Inject
    public ScheduleResource(Now now, Scheduler scheduler, JsonEncoder json, ShowTimeManager showTimeManager) {
        ScheduleResource.now = now;
        ScheduleResource.scheduler = scheduler;
        ScheduleResource.json = json;
        ScheduleResource.showTimeManager = showTimeManager;
    }

    @GET
    @Path("showtimes")
    public Response getShowTimes() {
        return ok(showTimeManager.encodeShows());
    }

    @GET
    @Path("showtime")
    public Response getShowTime(@QueryParam("id") Integer id) {
        return ok(showTimeManager.encodeShow(id));
    }

    @POST
    @Path("showtime/delete")
    public Response deleteShowTime(@QueryParam("id") Integer id) {
        if ( showTimeManager.deleteShow(id) ) {
            return ok("Deleted");
        } else {
            return error("Not found");
        }
    }

    @POST
    @Path("showtime/update")
    public Response updateShowTime(String json) {
        ShowTime showTime = showTimeManager.decodeShow(json);
        if ( showTime.getId()>0 ) {
            showTimeManager.deleteShow(showTime.getId());
        }
        showTimeManager.addShow(showTime);
        return ok("Done");
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
