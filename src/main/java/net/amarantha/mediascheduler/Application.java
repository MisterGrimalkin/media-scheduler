package net.amarantha.mediascheduler;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.device.ArKaos;

public class Application {

    @Inject private ArKaos mediaServer;

    public void startApplication() {
        mediaServer.open();
        mediaServer.testMidi();
        mediaServer.close();
    }

}
