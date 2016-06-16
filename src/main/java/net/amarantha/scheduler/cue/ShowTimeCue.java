package net.amarantha.scheduler.cue;

import com.google.inject.Inject;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.showtime.ShowTime;
import net.amarantha.scheduler.showtime.ShowTimeManager;

import java.util.LinkedList;
import java.util.List;

public class ShowTimeCue extends Cue {

    @Inject private ShowTimeManager showTimeManager;
    @Inject private HttpService http;
    @Inject private HostManager hostManager;

    private String hostGroup = "";
    private String lastEntireMessage = "";

    @Override
    public void start() {
        System.out.println("Updating ShowTimes...");
        String entireMessage = "";
        ShowTime currentShow = showTimeManager.getCurrentShow();
        List<ShowTime> showTimes = showTimeManager.getFutureShows();
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
            List<String> hosts = hostManager.getHosts(hostGroup);
            if ( hosts!=null ) {
                for (String host : hosts) {
                    http.post(host, "lightboard/scene/events/group/events/clear", null);
                    for (String message : messages) {
                        http.post(host, "lightboard/scene/events/group/events/add", message);
                    }
                    http.post(host, "lightboard/scene/reload", null);
                    lastEntireMessage = entireMessage;
                }
            }
        }
    }

    @Override
    public void stop() {

    }

    public void setHostGroup(String hostGroup) {
        this.hostGroup = hostGroup;
    }

    public String getHostGroup() {
        return hostGroup;
    }
}
