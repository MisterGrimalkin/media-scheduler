package net.amarantha.scheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.http.HttpCallback;
import net.amarantha.scheduler.http.HttpService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Timer;
import java.util.TimerTask;

@Path("showers")
public class ShowerResource extends Resource {

    private static HttpService http;
    private static HostManager hosts;

    public ShowerResource() {}

    private static int maleTicket = 1;
    private static int femaleTicket = 1;

    private static Timer timer = new Timer();

    public void startMonitor() {
        updateMale();
        updateFemale();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                System.out.println("Checking shower....");
                for ( String host : hosts.getHosts("showers") ) {
                    String male = http.get(host, "lightboard/scene/showers/group/male/list");
                    if ( male!=null ) {
                        if ( !male.equals(maleTicket+";;"+maleTicket) ) {
                            updateMale();
                        }
                        String female = http.get(host, "lightboard/scene/showers/group/female/list");
                        if ( female!=null ) {
                            if (!female.equals(femaleTicket+";;"+femaleTicket)) {
                                updateFemale();
                            }
                        }
                    }
                }
//                System.out.println("Checked");
            }
        }, 0, 5000);
    }

    public void stopMonitor() {
        timer.cancel();
        postToShowerSigns("male", "---");
        postToShowerSigns("female", "---");
    }


    @Inject
    public ShowerResource(HttpService http, HostManager hosts) {
        ShowerResource.http = http;
        ShowerResource.hosts = hosts;
    }

    @GET
    @Path("male")
    public Response getMaleTicket() {
        return ok(maleTicket);
    }

    @POST
    @Path("male/next")
    public Response nextMaleTicket() {
        maleTicket++;
        updateMale();
        return ok();
    }

    @POST
    @Path("male/{number}")
    public Response setMaleTicket(@PathParam("number") int number) {
        maleTicket = number;
        updateMale();
        return ok();
    }

    @GET
    @Path("female")
    public Response getFemaleTicket() {
        return ok(femaleTicket);
    }

    @POST
    @Path("female/next")
    public Response nextFemaleTicket() {
        femaleTicket++;
        updateFemale();
        return ok();
    }

    @POST
    @Path("female/{number}")
    public Response setFemaleTicket(@PathParam("number") int number) {
        femaleTicket = number;
        updateFemale();
        return ok();
    }

    public void updateMale() {
        postToShowerSigns("male", maleTicket+";;"+maleTicket);
    }

    public void updateFemale() {
        postToShowerSigns("female", femaleTicket+";;"+femaleTicket);
    }

    private void postToShowerSigns(String gender, String content) {
        for ( String host : hosts.getHosts("showers") ) {
            http.postAsync(
                response ->
                    http.postAsync(null, host, "lightboard/scene/showers/group/"+gender+"/add", content),
            host, "lightboard/scene/showers/group/" + gender + "/clear", "");
        }
    }

}
