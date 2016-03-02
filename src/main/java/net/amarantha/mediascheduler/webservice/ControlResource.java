package net.amarantha.mediascheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.device.ArKaos;
import net.amarantha.mediascheduler.scheduler.Schedule;
import net.amarantha.mediascheduler.scheduler.Scheduler;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("control")
public class ControlResource extends Resource {

    private static ArKaos mediaServer;
    private static Scheduler scheduler;

    public ControlResource() {}

    @Inject
    public ControlResource(ArKaos mediaServer, Scheduler scheduler) {
        ControlResource.mediaServer = mediaServer;
        ControlResource.scheduler = scheduler;
    }

    @POST
    @Path("start")
    public Response start() {
        scheduler.pause(false);
        return ok("Started");
    }

    @POST
    @Path("stop")
    public Response stop() {
        scheduler.pause(true);
        mediaServer.stopAll();
        return ok("Stopped");
    }

    @GET
    @Path("brightness")
    public Response getBrightness() {
        return ok(mediaServer.getBrightness());
    }

    @POST
    @Path("brightness")
    public Response setBrightness(@QueryParam("value") int brightness) {
        if ( brightness < 0 || brightness > 127) {
            return error("Brightness value out of range");
        }
        mediaServer.setBrightness(brightness);
        return ok("Brightness set");
    }

    @GET
    @Path("contrast")
    public Response getContrast() {
        return ok(mediaServer.getContrast());
    }

    @POST
    @Path("contrast")
    public Response setContrast(@QueryParam("value") int contrast) {
        if ( contrast < 0 || contrast > 127) {
            return error("Contrast value out of range");
        }
        mediaServer.setContrast(contrast);
        return ok("Contrast set");
    }

}
