package net.amarantha.scheduler;

import com.google.inject.Inject;

import java.util.Scanner;

public class Application {

    @Inject private MainSystem system;

    public void startApplication() {

        system.startup();

        System.out.println("Server is Online\nPress ENTER to quit...");
        Scanner sc = new Scanner(System.in);
        while( !sc.hasNextLine() ) {}

        system.shutdown();

    }

}