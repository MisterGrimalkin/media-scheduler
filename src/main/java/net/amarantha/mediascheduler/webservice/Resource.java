package net.amarantha.mediascheduler.webservice;

import javax.ws.rs.core.Response;

public class Resource {

    public Response ok() {
        return ok("OK");
    }

    public Response ok(int entity) {
        return ok(""+entity);
    }

    public Response ok(String entity) {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(entity)
                .build();
    }

    public Response error(String entity) {
        return Response.serverError()
                .header("Access-Control-Allow-Origin", "*")
                .entity(entity)
                .build();
    }


}
