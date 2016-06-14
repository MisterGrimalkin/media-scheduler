package net.amarantha.scheduler;

import com.google.inject.Inject;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.cue.CueFactory;
import net.amarantha.scheduler.exception.CueNotFoundException;
import net.amarantha.scheduler.exception.DuplicateCueException;
import net.amarantha.scheduler.exception.PriorityOutOfBoundsException;
import net.amarantha.scheduler.exception.ScheduleConflictException;
import net.amarantha.scheduler.scheduler.MediaEvent;
import net.amarantha.scheduler.scheduler.Scheduler;
import net.amarantha.scheduler.showtime.ShowTime;
import net.amarantha.scheduler.showtime.ShowTimeManager;
import net.amarantha.scheduler.webservice.WebService;
import org.glassfish.admin.rest.client.RestClientBase;
import org.glassfish.grizzly.http.Method;

import javax.print.attribute.standard.Media;
import java.util.Scanner;

public class Application {

    @Inject private Scheduler scheduler;
    @Inject private WebService webService;

    @Inject private ShowTimeManager showTimeManager;

    @Inject private CueFactory cueFactory;

    public void startApplication() {

        createShowTimes();

        System.out.println("Starting Scheduler...");
        showTimeManager.loadShows();

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

    private void createShowTimes() {

        showTimeManager.setFutureCount(2);

        String show1Start = "17:23";
        String show2Start = "17:30";
        String show3Start = "17:45";
        String show4Start = "18:00";
        String show4End =   "18:30";

        showTimeManager.addShow(new ShowTime()
                .setTitle("First Thing")
                .setDescription1("The first thing that happens")
                .setDescription2("Before all the other things")
                .setDateStr("2016-06-14").setStartTimeStr(show1Start).setEndTimeStr(show2Start));

        showTimeManager.addShow(new ShowTime()
                .setTitle("Second Thing")
                .setDescription1("The Thing that happens")
                .setDescription2("After the first thing")
                .setDateStr("2016-06-14").setStartTimeStr(show2Start).setEndTimeStr(show3Start));

        showTimeManager.addShow(new ShowTime()
                .setTitle("Third Thing")
                .setDescription1("The Third Event")
                .setDescription2("aka penultimate!")
                .setDateStr("2016-06-14").setStartTimeStr(show3Start).setEndTimeStr(show4Start));

        showTimeManager.addShow(new ShowTime()
                .setTitle("Lunch Time")
                .setDescription1("The Fourth Event")
                .setDescription2("Which comes last")
                .setDateStr("2016-06-14").setStartTimeStr(show4Start).setEndTimeStr(show4End)
                .setAlwaysShowOnDay(true));

        try {

            int cueId = (int)scheduler.addCue(cueFactory.makeShowTimeCue(1, "ShowTimeUpdater", "192.168.1.71:8001"));
            MediaEvent showTimeEvent = new MediaEvent(cueId, "2016-06-14", "09:53", "18:00");
            showTimeEvent.setPeriodic(true);
            showTimeEvent.setPeriod(20000);
            scheduler.addEvent(4, showTimeEvent);

            Cue eventsCue = cueFactory.makeHttpCue(2, "Events", Method.POST, "192.168.1.71:8001", "lightboard/scene/events/load", null);
            scheduler.addCue(eventsCue);
            MediaEvent eventsEvent = new MediaEvent(eventsCue.getId(), "2016-06-14", "09:00", "18:00");
            scheduler.addEvent(1, eventsEvent);

            Cue logoCue = cueFactory.makeHttpCue(10, "Logo", Method.POST, "192.168.1.71:8001", "lightboard/scene/logo/load", null);
            logoCue.setSelfStopping(true);
            scheduler.addCue(logoCue);
            MediaEvent logoEvent = new MediaEvent(logoCue.getId(), "2016-06-14", "09:00", "18:00");
            logoEvent.setPeriodic(true);
            logoEvent.setPeriod(60000);
            scheduler.addEvent(2, logoEvent);

            Cue messageCue = cueFactory.makeHttpCue(11, "Messages", Method.POST, "192.168.1.71:8001", "lightboard/scene/onezone/load", null);
            messageCue.setSelfStopping(true);
            scheduler.addCue(messageCue);
            MediaEvent messageEvent = new MediaEvent(messageCue.getId(), "2016-06-14", "09:00", "18:00");
            messageEvent.setPeriodic(true);
            messageEvent.setPeriod(40000);
            scheduler.addEvent(3, messageEvent);

        } catch (DuplicateCueException | CueNotFoundException | ScheduleConflictException | PriorityOutOfBoundsException e) {
            e.printStackTrace();
        }

        showTimeManager.saveShows();


    }

}