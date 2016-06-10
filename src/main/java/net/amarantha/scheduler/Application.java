package net.amarantha.scheduler;

import com.google.inject.Inject;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.cue.CueFactory;
import net.amarantha.scheduler.exception.DuplicateCueException;
import net.amarantha.scheduler.http.Param;
import net.amarantha.scheduler.scheduler.Scheduler;
import net.amarantha.scheduler.webservice.WebService;
import org.glassfish.grizzly.http.Method;

import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static javax.sound.midi.ShortMessage.NOTE_ON;

public class Application {

    @Inject private Scheduler scheduler;
    @Inject private WebService webService;

    @Inject private CueFactory cueFactory;

    public void startApplication() {

        try {

            List<String> hosts = new ArrayList<>();
            hosts.add("192.168.0.18:8001");
            hosts.add("192.168.0.12:8001");

            Cue cue1 = cueFactory.makeHttpCue(1, "Logo", Method.POST, hosts, "lightboard/scene/logo/load", "What?");
            Cue cue2 = cueFactory.makeHttpCue(2, "OneMessage", Method.GET, hosts, "lightboard/scene/onezone/load", null, new Param("thingy","wotsit"));
            Cue cue3 = cueFactory.makeMidiCue(3, "C-Sharp", NOTE_ON, 1, 60, 100);

            scheduler.addCue(cue1);
            scheduler.addCue(cue2);
            scheduler.addCue(cue3);


        } catch (DuplicateCueException e) {
            e.printStackTrace();
        }


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
