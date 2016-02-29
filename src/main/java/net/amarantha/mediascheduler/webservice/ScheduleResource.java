package net.amarantha.mediascheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.scheduler.CueList;
import net.amarantha.mediascheduler.scheduler.MediaEvent;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
import net.amarantha.mediascheduler.scheduler.Scheduler;
import net.amarantha.mediascheduler.utility.Now;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

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
    @Path("time")
    public Response getTime() {
        return ok(now.time().toString());
    }

    @GET
    public Response get() {
        return ok(json.encodeAllSchedules());
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