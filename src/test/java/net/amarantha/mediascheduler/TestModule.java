package net.amarantha.mediascheduler;

import com.google.inject.AbstractModule;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.device.ProjectorMock;
import net.amarantha.mediascheduler.midi.Midi;
import net.amarantha.mediascheduler.midi.MidiMock;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Midi.class).to(MidiMock.class);
        bind(Projector.class).to(ProjectorMock.class);
    }

}
