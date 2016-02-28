package net.amarantha.mediascheduler;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.exception.SchedulerException;
import net.amarantha.mediascheduler.scheduler.MediaEvent;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
import net.amarantha.mediascheduler.scheduler.Scheduler;
import net.amarantha.mediascheduler.webservice.WebService;

import java.util.Scanner;

public class Application {

    @Inject private Scheduler scheduler;
    @Inject private JsonEncoder loader;
    @Inject private WebService webService;

    public void startApplication() {

        System.out.println("Starting Up...");
        scheduler.startup();
        webService.startWebService();

        System.out.println("Media Scheduler is online\nPress ENTER to quit...");
        Scanner sc = new Scanner(System.in);
        while( !sc.hasNextLine() ) {}

        System.out.println("Shutting Down...");
        scheduler.shutdown();
        webService.stopWebService();

        System.out.println("Goodbye");

    }

    private void testData() {
        try {
            String start = "23:18";

            scheduler.addEvent(new MediaEvent(scheduler.addCueList(60, "1"),
                    "2016-02-27", start, start+":01"));

            scheduler.addEvent(new MediaEvent(scheduler.addCueList(64, "2"),
                    "2016-02-27", start+":01", start+":02"));

            scheduler.addEvent(new MediaEvent(scheduler.addCueList(67, "3"),
                    "2016-02-27", start+":02", start+":06"));

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
