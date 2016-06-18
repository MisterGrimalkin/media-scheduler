package net.amarantha.scheduler.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.scheduler.JsonEncoderImpl;
import net.amarantha.scheduler.utility.FileService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static net.amarantha.scheduler.scheduler.JsonEncoderImpl.createMapper;

@Path("showers")
public class ShowerResource extends Resource {

    private static HttpService http;
    private static HostManager hosts;
    private static FileService files;

    @Inject
    public ShowerResource(HttpService http, HostManager hosts, FileService files) {
        ShowerResource.http = http;
        ShowerResource.hosts = hosts;
        ShowerResource.files = files;
    }

    public ShowerResource() {}

    private static Map<Integer, String> messages = new HashMap<>();
    private static int maleTicket = 1;
    private static int femaleTicket = 1;

    private static Timer timer = new Timer();

    /////////////
    // Monitor //
    /////////////

    public void startMonitor() {
        loadTickets();
        updateMale();
        updateFemale();
        loadMessages();
        postMessages();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
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
            }
        }, 30000, 10000);
        System.out.println("Shower Monitor Online");
    }

    public void stopMonitor() {
        timer.cancel();
        postShowerNumbers("male", "-");
        postShowerNumbers("female", "-");
        postToAll("Showers Open at 9am");
    }


    /////////////////
    // Persistence //
    /////////////////

    private void loadTickets() {
        String ticketNumbers = files.readFromFile("data/tickets.dat");
        if ( ticketNumbers!=null ) {
            String[] pieces = ticketNumbers.split(",");
            if ( pieces.length==2 ) {
                femaleTicket = Integer.parseInt(pieces[0]);
                maleTicket = Integer.parseInt(pieces[1]);
            }
        }
    }

    private void saveTickets() {
        files.writeToFile("data/tickets.dat", femaleTicket+","+maleTicket);
    }

    private void loadMessages() {
        String json = files.readFromFile("data/shower-messages.json");
        if ( json!=null ) {
            try {
                messages = createMapper().readValue(json, new TypeReference<Map<Integer, String>>(){});
                for ( Entry<Integer, String> entry : messages.entrySet() ) {
                    if ( entry.getKey()>=nextMessageId ) {
                        nextMessageId = entry.getKey()+1;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveMessages() {
        try {
            String json = createMapper().writeValueAsString(messages);
            files.writeToFile("data/shower-messages.json", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    ////////////////////
    // Shower Tickets //
    ////////////////////

    @GET
    @Path("female")
    public Response getFemaleTicket() {
        return ok(femaleTicket);
    }
    @GET
    @Path("male")
    public Response getMaleTicket() {
        return ok(maleTicket);
    }

    @POST
    @Path("female/next")
    public Response nextFemaleTicket() {
        femaleTicket++;
        updateFemale();
        return ok();
    }
    @POST
    @Path("male/next")
    public Response nextMaleTicket() {
        maleTicket++;
        updateMale();
        return ok();
    }

    @POST
    @Path("female/{number}")
    public Response setFemaleTicket(@PathParam("number") int number) {
        femaleTicket = number;
        updateFemale();
        return ok();
    }
    @POST
    @Path("male/{number}")
    public Response setMaleTicket(@PathParam("number") int number) {
        maleTicket = number;
        updateMale();
        return ok();
    }

    public void updateMale() {
        postShowerNumbers("male", maleTicket+";;"+maleTicket);
    }

    public void updateFemale() {
        postShowerNumbers("female", femaleTicket+";;"+femaleTicket);
    }

    private void postShowerNumbers(String gender, String content) {
        System.out.println("Tickets -->");
        saveTickets();
        for ( String host : hosts.getHosts("showers") ) {
            System.out.println("--> "+host);
            http.postAsync(
                    response ->
                            http.postAsync(null, host, "lightboard/scene/showers/group/"+gender+"/add", content),
                    host, "lightboard/scene/showers/group/" + gender + "/clear", "");
        }
    }

    //////////////
    // Messages //
    //////////////

    private static int nextMessageId = 1;

    @GET
    @Path("messages")
    public Response getMessages() {
        String json = "";
        try {
            json = createMapper().writeValueAsString(messages);
        } catch (JsonProcessingException e) {
            return error(e.getMessage());
        }
        return ok(json);
    }

    @GET
    @Path("message/{messageId}")
    public Response getMessage(@PathParam("messageId") int id) {
        String msg = messages.get(id);
        if ( msg==null ) {
            return error("Message Not Found");
        } else {
            return ok(msg);
        }
    }

    @POST
    @Path("message/add")
    public Response addMessage(String message) {
        messages.put(nextMessageId++, message);
        postMessages();
        return ok("Added");
    }

    @POST
    @Path("message/replace/{messageId}")
    public Response replaceMessage(@PathParam("messageId") int id, String message) {
        messages.put(id, message);
        postMessages();
        return ok("Replaced");
    }

    @POST
    @Path("message/delete/{messageId}")
    public Response deleteMessage(@PathParam("messageId") int id) {
        if ( messages.remove(id)==null ) {
            return error("Not found");
        } else {
            postMessages();
            return ok("Deleted");
        }
    }

    /////////////
    // Tickets //
    /////////////

    private void postMessages() {
        System.out.println("Shower Messages -->");
        saveMessages();
        for ( String host : hosts.getHosts("showers") ) {
            System.out.println("--> "+host);
            http.postAsync(
                    response -> {
                        for (Entry<Integer, String> entry : messages.entrySet() ) {
                            http.post(host, "lightboard/scene/showers/group/scroller/add", entry.getValue());
                        }
                        http.post(host, "lightboard/scene/reload", "");
                    },
                    host, "lightboard/scene/showers/group/scroller/clear", "");
        }
    }

    private void postToAll(String message) {
        System.out.println("Shower Messages -->");
        for ( String host : hosts.getHosts("showers") ) {
            System.out.println("--> "+host);
            http.postAsync(
                    response -> {
                        http.post(host, "lightboard/scene/showers/group/scroller/add", message);
                    },
                    host, "lightboard/scene/showers/group/scroller/clear", "");
        }
    }

    /////////////////
    // Mode Select //
    /////////////////

    @POST
    @Path("showermode")
    public Response showerMode() {
        System.out.println("SHOWER MODE -->");
        for ( String host : hosts.getHosts("showers") ) {
            System.out.println("--> "+host);
            http.post(host, "lightboard/scene/showers/load", "");
        }
        return ok("Ok");
    }

    @POST
    @Path("eventsmode")
    public Response eventsMode() {
        System.out.println("EVENTS MODE -->");
        for ( String host : hosts.getHosts("events") ) {
            System.out.println("--> "+host);
            http.post(host, "lightboard/scene/events/load", "");
        }
        return ok("Ok");
    }

    @POST
    @Path("firelogo")
    public Response fireLogo() {
        System.out.println("FIRE LOGO -->");
        for ( String host : hosts.getHosts("logo") ) {
            System.out.println("--> "+host);
            http.post(host, "lightboard/scene/greenpeace-logo/load", "");
        }
        return ok("Ok");
    }

    @POST
    @Path("firescroller")
    public Response fireScroller() {
        System.out.println("FIRE SCROLLER -->");
        for ( String host : hosts.getHosts("scroller") ) {
            System.out.println("--> "+host);
            http.post(host, "lightboard/scene/single-message/load", "");
        }
        return ok("Ok");
    }

}
