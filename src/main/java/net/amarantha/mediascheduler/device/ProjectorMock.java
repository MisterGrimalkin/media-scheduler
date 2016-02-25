package net.amarantha.mediascheduler.device;

import com.google.inject.Singleton;

@Singleton
public class ProjectorMock implements Projector {

    private boolean on = false;

    public ProjectorMock() {
    }

    @Override
    public void switchOn(boolean on) {
        this.on = on;
    }

    @Override
    public boolean isOn() {
        return on;
    }

}
