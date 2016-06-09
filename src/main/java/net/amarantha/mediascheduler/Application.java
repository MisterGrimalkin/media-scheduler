package net.amarantha.mediascheduler;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.cue.Cue;
import net.amarantha.mediascheduler.cue.CueFactory;
import net.amarantha.mediascheduler.exception.SchedulerException;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
import net.amarantha.mediascheduler.scheduler.MediaEvent;
import net.amarantha.mediascheduler.scheduler.Scheduler;
import net.amarantha.mediascheduler.webservice.WebService;
import org.glassfish.grizzly.http.Method;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {

    @Inject private Scheduler scheduler;
    @Inject private WebService webService;

    @Inject private CueFactory cueFactory;

    public void startApplication() {

        List<String> hosts = new ArrayList<>();
        hosts.add("192.168.0.18:8001");
        hosts.add("192.168.0.12:8001");

        Cue cue = cueFactory.makeHttpCue(1, "test", Method.POST, hosts, "lightboard/scene/logo/load", null);
        cue.start();

//        System.out.println("Starting Up...");
//        scheduler.startup();
//        webService.startWebService();
//
//        System.out.println("Media Scheduler is online\nPress ENTER to quit...");
//        Scanner sc = new Scanner(System.in);
//        while( !sc.hasNextLine() ) {}
//
//        System.out.println("Shutting Down...");
//        scheduler.shutdown();
//        webService.stopWebService();
//
//        System.out.println("Goodbye");

    }

}
