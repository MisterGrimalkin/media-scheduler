package net.amarantha.mediascheduler;

import com.google.inject.AbstractModule;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.device.ProjectorMock;
import net.amarantha.mediascheduler.midi.Midi;
import net.amarantha.mediascheduler.midi.MidiImpl;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Midi.class).to(MidiImpl.class);
        bind(Projector.class).to(ProjectorMock.class);
    }

}
