package net.amarantha.scheduler.webservice;

import com.google.inject.Inject;
import net.amarantha.scheduler.exception.CueInUseException;
import net.amarantha.scheduler.exception.DuplicateCueException;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.scheduler.JsonEncoder;
import net.amarantha.scheduler.scheduler.Scheduler;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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
    public Response getCues(@QueryParam("id") Long id) {
        String cues;
        if ( id==null ) {
            cues = json.encodeCues();
        } else {
            Cue cue = scheduler.getCue(id);
            if ( cue==null ) {
                return error("Cue " + id + " not found");
            } else {
                cues = json.encodeCue(cue).toJsonString();
            }

        }
        return ok(cues);
    }

    @POST
    @Path("add")
    public Response createCue(String content) {
        try {
            Cue cue = json.decodeCue(new JSONObject(content));
            if (cue != null) {
                scheduler.addCue(cue);
                return ok(msgCueListCreated);
            } else {
                return error("Could not create Cue");
            }
        } catch (DuplicateCueException | ClassNotFoundException | JSONException e) {
            return error(e.getMessage());
        }
    }

    @POST
    @Path("update")
    public Response updateCue(String content) {
        try {
            Cue cue = json.decodeCue(new JSONObject(content));
            if (cue != null) {
                scheduler.removeCue(cue, true);
                scheduler.addCue(cue);
                return ok(msgCueListCreated);
            } else {
                return error("Could not create Cue");
            }
        } catch (DuplicateCueException | ClassNotFoundException | JSONException | CueInUseException e) {
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
