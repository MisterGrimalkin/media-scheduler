package net.amarantha.scheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.scheduler.MainSystem;
import net.amarantha.scheduler.device.ArKaos;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.scheduler.Scheduler;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Timer;
import java.util.TimerTask;

import static net.amarantha.scheduler.webservice.ShowerResource.modeScene;

@Path("control")
public class ControlResource extends Resource {

    private static ArKaos mediaServer;
    private static Scheduler scheduler;
    private static MainSystem system;
    private static HttpService http;
    private static HostManager hosts;

    public ControlResource() {}

    @Inject
    public ControlResource(ArKaos mediaServer, Scheduler scheduler, MainSystem system, HttpService http, HostManager hosts) {
        ControlResource.mediaServer = mediaServer;
        ControlResource.scheduler = scheduler;
        ControlResource.system = system;
        ControlResource.http = http;
        ControlResource.hosts = hosts;
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

    @POST
    @Path("shutdown")
    public Response shutdown() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                system.shutdown();
            }
        }, 1000);
        return ok("Goodbye. It's been emotional.");
    }

    @POST
    @Path("zap")
    public Response zap(String zapString) {
        System.out.println("ZAP! -->");
        long delay = zapString.equals("A;;B;;C") ? 60000 : 10000;
        system.pauseThreads(delay);
        for ( String host : hosts.getHosts("zapper") ) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("--> " + host);
                    http.post(host, "lightboard/scene/zapper/group/zaps/clear", "");
                    if ( zapString.equals("-;;-;;-") ) {
                        http.post(host, "lightboard/scene/" + modeScene + "/load", "");
                    } else if ( zapString.equals("A;;B;;C") ) {
                        http.post(host, "lightboard/scene/zapper-finale/load", "");
                    } else {
                        http.post(host, "lightboard/scene/zapper/group/zaps/add", zapString);
                        http.post(host, "lightboard/scene/zapper/load", "");
                    }
                }
            }, 0);
        }
        return ok("Zapped!");


    }

}
