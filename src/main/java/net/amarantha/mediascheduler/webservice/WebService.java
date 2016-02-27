package net.amarantha.mediascheduler.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

@Singleton
public class WebService {

    private HttpServer server;

    @Inject private ScheduleResource scheduleResource;

    public HttpServer startWebService() {

        System.out.println("Starting Web Service....");

        String fullUri = "http://127.0.0.1:8001/mediascheduler/";
        final ResourceConfig rc = new ResourceConfig().packages("net.amarantha.mediascheduler.webservice");
        rc.register(LoggingFilter.class);

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
