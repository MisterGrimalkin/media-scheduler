package net.amarantha.mediascheduler.http;

import javax.ws.rs.core.Response;

public interface HttpCallback {
    void call(Response response);
}
