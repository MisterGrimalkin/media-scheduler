package net.amarantha.mediascheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.device.ArKaos;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("control")
public class ControlResource extends Resource {

    private static ArKaos mediaServer;

    public ControlResource() {}

    @Inject
    public ControlResource(ArKaos mediaServer) {
        ControlResource.mediaServer = mediaServer;
    }

    @POST
    @Path("brightness")
    public Response setBrightness(@QueryParam("value") int brightness) {
        System.out.println("try to set brightness="+brightness);
        if ( brightness < 0 || brightness > 127) {
            return error("Brightness value out of range");
        }
        mediaServer.setBrightness(brightness);
        return ok("Brightness set");
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

    @POST
    @Path("stop")
    public Response stop() {
        mediaServer.stopCueList();
        return ok("Stopped");
    }

}
