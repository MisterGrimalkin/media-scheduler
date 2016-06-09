package net.amarantha.mediascheduler;

import com.google.inject.AbstractModule;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.device.ProjectorMock;
import net.amarantha.mediascheduler.http.HttpService;
import net.amarantha.mediascheduler.http.MockHttpService;
import net.amarantha.mediascheduler.midi.MidiService;
import net.amarantha.mediascheduler.midi.MockMidiService;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
import net.amarantha.mediascheduler.scheduler.JsonEncoderMock;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MidiService.class).to(MockMidiService.class);
        bind(HttpService.class).to(MockHttpService.class);
        bind(Projector.class).to(ProjectorMock.class);
        bind(JsonEncoder.class).to(JsonEncoderMock.class);
    }

}
