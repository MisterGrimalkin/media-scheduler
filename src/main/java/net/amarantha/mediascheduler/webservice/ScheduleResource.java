package net.amarantha.mediascheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.scheduler.CueList;
import net.amarantha.mediascheduler.scheduler.MediaEvent;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
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

    @POST
    @Path("add")
    public Response createEvent(String content) {
        MediaEvent event = json.parseMediaEvent(content);
        try {
            scheduler.addEvent(event);
        } catch (Exception e) {
            return error("Could not create event: " + e.getMessage());
        }
        return ok("Event created");
    }

    @POST
    @Path("remove")
    public Response removeEvent(String content) {
        int id = Integer.parseInt(content);
        scheduler.removeEvent(id);
        return ok(content);
    }

    @GET
    @Path("cuelist")
    public Response getCueLists() {
        return ok(json.encodeCueLists());
    }

    @POST
    @Path("cuelist")
    public Response createCueList(String content) {
        CueList cueList = json.parseCueList(content);
        scheduler.addCueList(cueList);
        return ok("Cue List created");

    }


}
