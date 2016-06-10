package net.amarantha.scheduler.cue;

import com.google.inject.Inject;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.http.Param;
import org.glassfish.grizzly.http.Method;

import java.util.ArrayList;
import java.util.List;

public class HttpCue extends Cue {

    @Inject private HttpService http;

    private List<String> hosts = new ArrayList<>();
    private String path;
    private Method method;
    private List<Param> params;
    private String payload;

    @Override
    public void start() {
        for ( String host : hosts ) {
            if (method == Method.POST) {
                http.postAsync(null, host, path, payload, (Param[]) params.toArray());
            } else if (method == Method.GET) {
                http.getAsync(null, host, path, (Param[]) params.toArray());
            }
        }
    }

    @Override
    public void stop() {}

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public void addHost(String host) {
        hosts.add(host);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public String getDescription() {
        String hostname = "multiple";
        if ( hosts.size()==1 ) {
            hostname = hosts.get(0);
        }
        String result = method + " - http://" + hostname + "/" + path;
        for (int i = 0; i < params.size(); i++) {
            result += (i == 0 ? "?" : "&") + params.get(i).getName() + "=" + params.get(i).getValue();
        }
        result += "{" + payload + "}";

        return result;
    }

    /////////////
    // Getters //
    /////////////


    public List<String> getHosts() {
        return hosts;
    }

    public String getPath() {
        return path;
    }

    public Method getMethod() {
        return method;
    }

    public List<Param> getParams() {
        return params;
    }

    public String getPayload() {
        return payload;
    }
}