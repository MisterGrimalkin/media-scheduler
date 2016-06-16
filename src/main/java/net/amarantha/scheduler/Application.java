package net.amarantha.scheduler;

import com.google.inject.Inject;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.cue.CueFactory;
import net.amarantha.scheduler.exception.CueNotFoundException;
import net.amarantha.scheduler.exception.DuplicateCueException;
import net.amarantha.scheduler.exception.PriorityOutOfBoundsException;
import net.amarantha.scheduler.exception.ScheduleConflictException;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.scheduler.MediaEvent;
import net.amarantha.scheduler.scheduler.Scheduler;
import net.amarantha.scheduler.showtime.ShowTime;
import net.amarantha.scheduler.showtime.ShowTimeManager;
import net.amarantha.scheduler.webservice.WebService;
import org.glassfish.admin.rest.client.RestClientBase;
import org.glassfish.grizzly.http.Method;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {

    @Inject private Scheduler scheduler;
    @Inject private WebService webService;

    @Inject private ShowTimeManager showTimeManager;

    @Inject private CueFactory cueFactory;

    @Inject private HostManager hostManager;

    public void startApplication() {

        showTimeManager.loadShows();
        hostManager.loadHosts();

        System.out.println("Starting Scheduler...");

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