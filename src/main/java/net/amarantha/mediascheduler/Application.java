package net.amarantha.mediascheduler;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.devices.ArKaos;

public class Application {

    @Inject private ArKaos mediaServer;

    public void startApplication() {
        mediaServer.start();
        mediaServer.testMidi();
        mediaServer.stop();
    }

}
