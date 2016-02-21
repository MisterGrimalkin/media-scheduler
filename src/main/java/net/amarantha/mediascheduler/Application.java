package net.amarantha.mediascheduler;

import com.google.inject.Guice;
import com.google.inject.Inject;
import net.amarantha.mediascheduler.devices.ArKaos;

public class Application {

    @Inject private ArKaos mediaServer;

    public void startApplication() {
        mediaServer.testMidi();
    }

    public static void main(String[] args) {
        Guice.createInjector(new ApplicationModule())
                .getInstance(Application.class)
                    .startApplication();
    }

}
