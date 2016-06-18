package net.amarantha.scheduler.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.utility.FileService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static net.amarantha.scheduler.scheduler.JsonEncoderImpl.createMapper;

@Path("scroller")
public class ScrollerResource extends Resource {

    private static HttpService http;
    private static HostManager hosts;
    private static FileService files;

    @Inject
    public ScrollerResource(HttpService http, HostManager hosts, FileService files) {
        ScrollerResource.http = http;
        ScrollerResource.hosts = hosts;
        ScrollerResource.files = files;
    }

    public ScrollerResource() {}

    private static Timer timer = new Timer();

    public void setup() {
        loadMessages();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                postMessages();
            }
        }, 0, 120000);
        System.out.println("Scroller Updater Online");
    }

    /////////////////
    // Persistence //
    /////////////////

    private void loadMessages() {
        String json = files.readFromFile("data/scroller-messages.json");
        if ( json!=null ) {
            try {
                messages = createMapper().readValue(json, new TypeReference<Map<Integer, String>>(){});
                for ( Map.Entry<Integer, String> entry : messages.entrySet() ) {
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
            files.writeToFile("data/scroller-messages.json", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    //////////////
    // Messages //
    //////////////

    private static int nextMessageId = 1;
    private static Map<Integer, String> messages = new HashMap<>();

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

    private void postMessages() {
        System.out.println("Scroller Messages -->");
        saveMessages();
        for ( String host : hosts.getHosts("scroller") ) {
            System.out.println("--> "+host);
            http.postAsync(
                    response -> {
                        for (Map.Entry<Integer, String> entry : messages.entrySet() ) {
                            String msg =
                                entry.getValue().toUpperCase()
                                    .replaceAll("\\{RED}", "{red}")
                                    .replaceAll("\\{GREEN}", "{green}")
                                    .replaceAll("\\{YELLOW}", "{yellow}");

                            http.post(host, "lightboard/scene/single-message/group/message/add", msg);
                        }
                        http.post(host, "lightboard/scene/reload", "");
                    },
                    host, "lightboard/scene/single-message/group/message/clear", "");
        }
    }

}
