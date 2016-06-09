package net.amarantha.mediascheduler.cue;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.amarantha.mediascheduler.http.Param;
import net.amarantha.mediascheduler.midi.MidiCommand;
import org.glassfish.grizzly.http.Method;

import java.util.Arrays;
import java.util.List;

public class CueFactory {

    @Inject private Injector injector;

    public MidiCue makeMidiCue(int id, String name, int command, int channel, int data1, int data2) {
        MidiCue cue = injector.getInstance(MidiCue.class);
        cue.setId(id);
        cue.setName(name);
        cue.setCommand(new MidiCommand(command, channel, data1, data2));
        return cue;
    }

    public HttpCue makeHttpCue(int id, String name, Method method, String host, String path, String payload, Param... params) {
        HttpCue cue = injector.getInstance(HttpCue.class);
        cue.setId(id);
        cue.setName(name);
        cue.addHost(host);
        cue.setPath(path);
        cue.setPayload(payload);
        cue.setMethod(method);
        cue.setParams(Arrays.asList(params));
        return cue;
    }

    public HttpCue makeHttpCue(int id, String name, Method method, List<String> hosts, String path, String payload, Param... params) {
        HttpCue cue = injector.getInstance(HttpCue.class);
        cue.setId(id);
        cue.setName(name);
        cue.setHosts(hosts);
        cue.setPath(path);
        cue.setPayload(payload);
        cue.setMethod(method);
        cue.setParams(Arrays.asList(params));
        return cue;
    }

}
