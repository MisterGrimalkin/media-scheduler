package net.amarantha.scheduler.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.utility.SchedulerProperties;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

@Singleton
public class WebService {

    private HttpServer server;

    @Inject private ControlResource controlResource;
    @Inject private CueResource cueResource;
    @Inject private ScheduleResource scheduleResource;
    @Inject private ShowerResource showerResource;

    @Inject private SchedulerProperties props;

    public HttpServer startWebService() {

        System.out.println("Starting Web Service....");

        String fullUri = "http://"+props.getIp().trim()+":8001/scheduler/";
        System.out.println(fullUri);

        ResourceConfig rc = new ResourceConfig().packages("net.amarantha.scheduler.webservice");
//        rc.register(LoggingFilter.class);

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(fullUri), rc);

        System.out.println("Web Service Online @ " + fullUri);

        showerResource.startMonitor();

        return server;
    }

    public void stopWebService() {
        showerResource.stopMonitor();
        if ( server!=null ) {
            server.shutdown();
        }
    }


}
