package net.amarantha.mediascheduler;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.exception.SchedulerException;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
import net.amarantha.mediascheduler.scheduler.MediaEvent;
import net.amarantha.mediascheduler.scheduler.Scheduler;
import net.amarantha.mediascheduler.webservice.WebService;

import java.time.DayOfWeek;
import java.util.Scanner;

public class Application {

    @Inject private Scheduler scheduler;
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

}
