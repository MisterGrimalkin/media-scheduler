package net.amarantha.mediascheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.scheduler.ScheduleLoader;
import net.amarantha.mediascheduler.scheduler.Scheduler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("schedule")
public class ScheduleResource extends Resource {

    private static Scheduler scheduler;
    private static ScheduleLoader loader;

    public ScheduleResource() {}

    @Inject
    public ScheduleResource(Scheduler scheduler, ScheduleLoader loader) {
        ScheduleResource.scheduler = scheduler;
        ScheduleResource.loader = loader;
    }

    @GET
    public Response get() {
        return ok(loader.getSchedulesJson());
    }


}
