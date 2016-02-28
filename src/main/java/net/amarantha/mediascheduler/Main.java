package net.amarantha.mediascheduler;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

    public static void main(String[] args) {

        Injector injector = Guice.createInjector(new ApplicationModule());

        if ( args.length>0 && "testmidi".equals(args[0]) ) {
            injector.getInstance(TestMidi.class).testMidi();
        } else {
            injector.getInstance(Application.class).startApplication();
        }

    }

}
