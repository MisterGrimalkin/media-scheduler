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
    private String path = "";
    private Method method;
    private List<Param> params = new ArrayList<>();
    private String payload = "";

    @Override
    public void start() {
        System.out.println("Firing Cue: " + getName());
        for ( String host : hosts ) {
            Param[] paramsArray = new Param[params.size()];
            for ( int i=0; i<params.size(); i++ ) {
                paramsArray[i] = params.get(i);
            }
            path = path.replace("\\/", "\\");
            System.out.println(host + "/" + path);
            if (method == Method.POST) {
                http.postAsync(null, host, path, payload, paramsArray);
            } else if (method == Method.GET) {
                http.getAsync(null, host, path, paramsArray);
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
        if ( payload==null ) {
            payload = "";
        }
        this.payload = payload;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public void addParam(Param param) {
        params.add(param);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        HttpCue httpCue = (HttpCue) o;

        if (hosts != null ? !hosts.equals(httpCue.hosts) : httpCue.hosts != null) return false;
        if (path != null ? !path.equals(httpCue.path) : httpCue.path != null) return false;
        if (method != null ? !method.equals(httpCue.method) : httpCue.method != null) return false;
        if (params != null ? !params.equals(httpCue.params) : httpCue.params != null) return false;
        return payload != null ? payload.equals(httpCue.payload) : httpCue.payload == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (hosts != null ? hosts.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (params != null ? params.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }
}