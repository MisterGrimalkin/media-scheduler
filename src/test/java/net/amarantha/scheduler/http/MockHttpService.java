package net.amarantha.scheduler.http;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.cue.CueFactory;
import net.amarantha.scheduler.cue.HttpCue;
import org.glassfish.grizzly.http.Method;

@Singleton
public class MockHttpService implements HttpService {

    @Inject private CueFactory cueFactory;

    private String lastHttpCall;

    @Override
    public String get(String host, String path, Param... params) {
        HttpCue cue = cueFactory.makeHttpCue(0, "", Method.GET, host, path, null, params);
        lastHttpCall = cue.getDescription();
        return "";
    }

    @Override
    public void getAsync(HttpCallback callback, String host, String path, Param... params) {
        get(host, path, params);
    }

    @Override
    public String post(String host, String path, String payload, Param... params) {
        HttpCue cue = cueFactory.makeHttpCue(0, "", Method.POST, host, path, payload, params);
        lastHttpCall = cue.getDescription();
        return "";
    }

    @Override
    public void postAsync(HttpCallback callback, String host, String path, String payload, Param... params) {
        post(host, path, payload, params);
    }

    public String getLastHttpCall() {
        return lastHttpCall;
    }

    public void clearLastCall() {
        lastHttpCall = null;
    }
}
