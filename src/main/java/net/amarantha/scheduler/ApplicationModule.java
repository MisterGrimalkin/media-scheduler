package net.amarantha.scheduler;

import com.google.inject.AbstractModule;
import net.amarantha.scheduler.device.Projector;
import net.amarantha.scheduler.device.ProjectorMock;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.http.HttpServiceImpl;
import net.amarantha.scheduler.midi.MidiService;
import net.amarantha.scheduler.midi.MidiServiceImpl;
import net.amarantha.scheduler.scheduler.JsonEncoder;
import net.amarantha.scheduler.scheduler.JsonEncoderImpl;
import net.amarantha.scheduler.utility.FileService;
import net.amarantha.scheduler.utility.FileServiceImpl;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MidiService.class).to(MidiServiceImpl.class);
        bind(HttpService.class).to(HttpServiceImpl.class);
        bind(Projector.class).to(ProjectorMock.class);
        bind(JsonEncoder.class).to(JsonEncoderImpl.class);
        bind(FileService.class).to(FileServiceImpl.class);
    }

}
