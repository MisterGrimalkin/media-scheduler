package net.amarantha.scheduler;

import com.google.inject.AbstractModule;
import net.amarantha.scheduler.device.Projector;
import net.amarantha.scheduler.device.ProjectorMock;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.http.MockHttpService;
import net.amarantha.scheduler.midi.MidiService;
import net.amarantha.scheduler.midi.MockMidiService;
import net.amarantha.scheduler.scheduler.JsonEncoder;
import net.amarantha.scheduler.scheduler.JsonEncoderMock;
import net.amarantha.scheduler.utility.FileService;
import net.amarantha.scheduler.utility.MockFileService;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MidiService.class).to(MockMidiService.class);
        bind(HttpService.class).to(MockHttpService.class);
        bind(Projector.class).to(ProjectorMock.class);
        bind(FileService.class).to(MockFileService.class);
        bind(JsonEncoder.class).to(JsonEncoderMock.class);
    }

}
