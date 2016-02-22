package net.amarantha.mediascheduler;

import com.google.inject.Guice;

public class Main {

    public static void main(String[] args) {
        Guice.createInjector(new ApplicationModule())
            .getInstance(Application.class)
                .startApplication();
    }

}
