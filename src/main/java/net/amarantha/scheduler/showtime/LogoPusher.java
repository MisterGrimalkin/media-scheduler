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
    private boolean paused = false;

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if ( !paused ) {
                    fire();
                }
            }
        }, 7000, 60000);
        System.out.println("LogoPusher Active");
    }

    public void stop() {
        timer.cancel();
    }

    private boolean logo = true;

    private void fire() {
        if (logo) {
            System.out.println("Logo -->");
            fireScene("greenpeace-logo", "greenpeace-logo-big");
        } else {
            System.out.println("Flash Message -->");
            fireScene("single-message", "triple");
        }
        logo = !logo;
    }

    private void fireScene(String smallScene, String bigScene) {
        for ( String host : hosts.getHosts("logo") ) {
            System.out.println("--> "+host);
            http.postAsync(null, host, "lightboard/scene/"+smallScene+"/load", "");
        }
        for ( String host : hosts.getHosts("big-logo") ) {
            System.out.println("--> "+host);
            http.postAsync(null, host, "lightboard/scene/"+bigScene+"/load", "");
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }
}
