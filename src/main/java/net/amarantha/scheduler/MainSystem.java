package net.amarantha.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.scheduler.Scheduler;
import net.amarantha.scheduler.showtime.LogoPusher;
import net.amarantha.scheduler.showtime.ShowTimeManager;
import net.amarantha.scheduler.webservice.ShowerResource;
import net.amarantha.scheduler.webservice.WebService;

import java.util.Timer;
import java.util.TimerTask;

import static net.amarantha.scheduler.webservice.ShowerResource.modeScene;

@Singleton
public class MainSystem {

//    @Inject private Scheduler scheduler;
    @Inject private WebService webService;
    @Inject private ShowTimeManager showTimeManager;
    @Inject private LogoPusher logoPusher;
    @Inject private HostManager hostManager;
    @Inject private HttpService http;

    public void startup() {
        System.out.println("Starting Server...");
        hostManager.loadHosts();
        webService.startWebService();
        showTimeManager.start();
        logoPusher.start();
    }

    public void shutdown() {
        System.out.println("Shutting Down...");
        webService.stopWebService();
        showTimeManager.stop();
        logoPusher.stop();

        long now = System.currentTimeMillis();
        while ( System.currentTimeMillis() - now < 3000 ) {}

        System.out.println("Goodbye");
        System.exit(0);
    }

    private Timer pauseTimer;

    public void pauseThreads(long duration) {
        showTimeManager.pause();
        logoPusher.pause();
        if ( pauseTimer!=null ) {
            pauseTimer.cancel();
        }
        pauseTimer = new Timer();
        pauseTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showTimeManager.resume();
                logoPusher.resume();
                switch (modeScene) {
                    case "events":
                    case "showers":
                        for ( String host : hostManager.getHosts("big-events") ) {
                            http.post(host, "/lightboard/scene/events-big/load", "");
                        }
                        for ( String host : hostManager.getHosts(modeScene) ) {
                            http.post(host, "/lightboard/scene/"+modeScene+"/load", "");
                        }
                        break;
                }

            }
        }, duration);
    }

}
