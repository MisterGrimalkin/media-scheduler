package net.amarantha.scheduler.showtime;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.http.HttpService;

import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class LogoPusher {

    @Inject private HttpService http;
    @Inject private HostManager hosts;

    private Timer timer = new Timer();

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fire();
            }
        }, 0, 120000);
    }

    public void stop() {
        timer.cancel();
    }

    private boolean lastWasLogo = false;

    private void fire() {
        if ( lastWasLogo ) {
            fireScene("greenpeace-logo");
        } else {
            fireScene("single-message");
        }
        lastWasLogo = !lastWasLogo;
    }

    private void fireScene(String scene) {
//        System.out.println("Firing "+scene);
        for ( String host : hosts.getHosts("logo") ) {
            http.postAsync(null, host, "lightboard/scene/"+scene+"/load", "");
        }
    }

}
