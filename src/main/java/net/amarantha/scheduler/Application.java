package net.amarantha.scheduler;

import com.google.inject.Inject;
import net.amarantha.scheduler.cue.CueFactory;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.scheduler.Scheduler;
import net.amarantha.scheduler.showtime.LogoPusher;
import net.amarantha.scheduler.showtime.ShowTimeManager;
import net.amarantha.scheduler.webservice.WebService;

import java.util.Scanner;

public class Application {

    @Inject private Scheduler scheduler;
    @Inject private WebService webService;

    @Inject private ShowTimeManager showTimeManager;

    @Inject private CueFactory cueFactory;

    @Inject private LogoPusher logoPusher;

    @Inject private HostManager hostManager;

    public void startApplication() {

        System.out.println("Starting Server...");

        hostManager.loadHosts();
        showTimeManager.start();
        webService.startWebService();
        logoPusher.start();

        System.out.println("Server is Online\nPress ENTER to quit...");
        Scanner sc = new Scanner(System.in);
        while( !sc.hasNextLine() ) {}

        System.out.println("Shutting Down...");
        scheduler.shutdown();
        webService.stopWebService();
        logoPusher.stop();

        long now = System.currentTimeMillis();
        while ( System.currentTimeMillis() - now < 3000 ) {}

        System.out.println("Goodbye");

        System.exit(0);

    }

}