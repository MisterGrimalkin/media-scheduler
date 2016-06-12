package net.amarantha.scheduler.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.utility.PropertyManager;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

@Singleton
public class WebService {

    private HttpServer server;

    @Inject private ControlResource controlResource;
    @Inject private CueResource cueResource;
    @Inject private ScheduleResource scheduleResource;

    @Inject private PropertyManager props;

    public HttpServer startWebService() {

        System.out.println("Starting Web Service....");

        String fullUri = "http://"+props.getString("ip","127.0.0.1")+":8001/scheduler/";

        ResourceConfig rc = new ResourceConfig().packages("net.amarantha.scheduler.webservice");
//        rc.register(LoggingFilter.class);

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(fullUri), rc);

        System.out.println("Web Service Online @ " + fullUri);

        return server;
    }

    public void stopWebService() {
        if ( server!=null ) {
            server.shutdown();
        }
    }


}
