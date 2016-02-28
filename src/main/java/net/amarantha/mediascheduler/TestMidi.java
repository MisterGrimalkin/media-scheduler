package net.amarantha.mediascheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.mediascheduler.device.ArKaos;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

@Singleton
public class TestMidi {

    @Inject private ArKaos mediaServer;

    public void testMidi() {

        System.out.println("Start MIDI Test");

        mediaServer.startup();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                while ( true ) {
                    mediaServer.glissandoFifths();
                }
            }
        }, 10);

        System.out.println("Running Test\nPress ENTER to quit...");

        Scanner sc = new Scanner(System.in);
        while( !sc.hasNextLine() ) {}

        System.out.println("Shutting Down...");

        timer.cancel();
        mediaServer.shutdown();

        System.out.println("Goodbye");

        System.exit(0);
    }
}
