package net.amarantha.scheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.scheduler.exception.CueInUseException;
import net.amarantha.scheduler.exception.DuplicateCueException;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.scheduler.JsonEncoder;
import net.amarantha.scheduler.scheduler.Scheduler;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("cue")
public class CueResource extends Resource {

    private static String msgCueListCreated = "Cue created";
    private static String msgCueListRemoved = "Cue removed";

    private static Scheduler scheduler;
    private static JsonEncoder json;

    public CueResource() {}

    @Inject
    public CueResource(Scheduler scheduler, JsonEncoder json) {
        CueResource.scheduler = scheduler;
        CueResource.json = json;
    }

    @GET
    public Response getCues() {
        return ok(json.encodeCues());
    }

    @POST
    @Path("create")
    public Response createCue(String content) {
        Cue cue = json.decodeCue(content);
        try {
            if (cue != null) {
                scheduler.addCue(cue);
                return ok(msgCueListCreated);
            } else {
                return error("Could not create Cue List");
            }
        } catch (DuplicateCueException e) {
            return error(e.getMessage());
        }
    }

    @POST
    @Path("remove")
    public Response removeCue(int id) {
        try {
            scheduler.removeCue(id);
            return ok(msgCueListRemoved);
        } catch (CueInUseException e) {
            return error(e.getMessage());
        }
    }

}
