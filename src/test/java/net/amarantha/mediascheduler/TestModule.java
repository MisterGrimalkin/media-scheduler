package net.amarantha.mediascheduler;

import com.google.inject.AbstractModule;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.device.ProjectorMock;
import net.amarantha.mediascheduler.midi.Midi;
import net.amarantha.mediascheduler.midi.MidiMock;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
import net.amarantha.mediascheduler.scheduler.JsonEncoderMock;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Midi.class).to(MidiMock.class);
        bind(Projector.class).to(ProjectorMock.class);
        bind(JsonEncoder.class).to(JsonEncoderMock.class);
    }

}
