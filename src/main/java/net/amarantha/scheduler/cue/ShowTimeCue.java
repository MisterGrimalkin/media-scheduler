package net.amarantha.scheduler.cue;

import com.google.inject.Inject;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.showtime.ShowTime;
import net.amarantha.scheduler.showtime.ShowTimeManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ShowTimeCue extends Cue {

    @Inject private ShowTimeManager manager;
    @Inject private HttpService http;

    private List<String> hosts = new ArrayList<>();

    private String lastEntireMessage = "";

    @Override
    public void start() {
        System.out.println("Updating ShowTimes...");
        String entireMessage = "";
        ShowTime currentShow = manager.getCurrentShow();
        List<ShowTime> showTimes = manager.getFutureShows();
        List<String> messages = new LinkedList<>();
        if ( currentShow!=null ) {
            String msg = currentShow.getMessage(true);
            entireMessage += msg;
            messages.add(msg);
        }
        for ( ShowTime showTime : showTimes ) {
            String msg = showTime.getMessage();
            entireMessage += msg;
            messages.add(msg);
        }
        if ( !lastEntireMessage.equals(entireMessage) ) {
            for ( String host : hosts ) {
                http.post(host, "lightboard/scene/events/group/events/clear", null);
                for ( String message : messages ) {
                    http.post(host, "lightboard/scene/events/group/events/add", message);
                }
                http.post(host, "lightboard/scene/reload", null);
                lastEntireMessage = entireMessage;
            }
        }
    }

    @Override
    public void stop() {

    }

    public void setHosts(List<String> hosts) {
        System.out.println("SETTING HOSTS");
        this.hosts = hosts;
    }

    public void addHost(String host) {
        hosts.add(host);
    }

    public List<String> getHosts() {
        return hosts;
    }
}
