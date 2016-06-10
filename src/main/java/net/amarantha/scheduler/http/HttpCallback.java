package net.amarantha.scheduler.http;

import javax.ws.rs.core.Response;

public interface HttpCallback {
    void call(Response response);
}
