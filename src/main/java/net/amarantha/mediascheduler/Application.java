package net.amarantha.mediascheduler;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.device.ArKaos;
import net.amarantha.mediascheduler.scheduler.Scheduler;

import java.util.Scanner;

public class Application {

    @Inject private Scheduler scheduler;

    public void startApplication() {

        System.out.println("Starting Up");
        scheduler.startup();

        scheduler.testMidi();

        System.out.println("Press ENTER to quit...");
        Scanner sc = new Scanner(System.in);
        while( !sc.hasNextLine() ) {}

        System.out.println("Shutting Down");
        scheduler.shutdown();

    }

}
