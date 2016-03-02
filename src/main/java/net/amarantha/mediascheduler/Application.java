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
    @Inject private JsonEncoder loader;
    @Inject private WebService webService;

    public void startApplication() {

        System.out.println("Starting Up...");
        scheduler.startup();
        webService.startWebService();

        //testData();

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

            int cueList1 = 0;//scheduler.addCue(48, "Lucy In The Sky");
            int cueList2 = 1;//scheduler.addCue(49, "Fixing A Hole");
            int cueList3 = 2;//scheduler.addCue(50, "Taxman");


            scheduler.addEvent(new MediaEvent(cueList1,
                    "2016-03-01", "15:00", "16:00"));

            scheduler.addEvent(new MediaEvent(cueList2,
                    "2016-03-01", "16:30", "18:30"));

            scheduler.addEvent(new MediaEvent(cueList3,
                    "2016-03-01", "18:30", "23:00"));

            scheduler.addEvent(new MediaEvent(cueList1,
                    "2016-03-02", "08:00", "16:00"));

            scheduler.addEvent(new MediaEvent(cueList2,
                    "2016-03-03", "10:00", "13:30"));

            scheduler.addEvent(new MediaEvent(cueList3,
                    "2016-03-03", "15:00", "20:00", DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));


        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
