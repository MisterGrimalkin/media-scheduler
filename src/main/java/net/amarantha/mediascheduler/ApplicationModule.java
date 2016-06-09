package net.amarantha.mediascheduler;

import com.google.inject.AbstractModule;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.device.ProjectorMock;
import net.amarantha.mediascheduler.http.HttpService;
import net.amarantha.mediascheduler.http.HttpServiceImpl;
import net.amarantha.mediascheduler.midi.MidiService;
import net.amarantha.mediascheduler.midi.MidiServiceImpl;
import net.amarantha.mediascheduler.scheduler.JsonEncoder;
import net.amarantha.mediascheduler.scheduler.JsonEncoderImpl;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MidiService.class).to(MidiServiceImpl.class);
        bind(HttpService.class).to(HttpServiceImpl.class);
        bind(Projector.class).to(ProjectorMock.class);
        bind(JsonEncoder.class).to(JsonEncoderImpl.class);
    }

}
