package net.amarantha.scheduler.utility;

public class SchedulerProperties extends PropertyManager {

    public String getCuePackage() {
        return getString("cuePackage", "net.amarantha.scheduler.cue");
    }

}
