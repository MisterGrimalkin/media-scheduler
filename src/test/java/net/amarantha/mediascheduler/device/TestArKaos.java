package net.amarantha.mediascheduler.device;


import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.mediascheduler.TestModule;
import net.amarantha.mediascheduler.scheduler.CueList;
import net.amarantha.mediascheduler.midi.Midi;
import net.amarantha.mediascheduler.midi.MidiCommand;
import net.amarantha.mediascheduler.midi.MidiMock;
import org.junit.runner.RunWith;

import static net.amarantha.mediascheduler.device.ArKaosMidiCommand.BRIGHTNESS;
import static net.amarantha.mediascheduler.device.ArKaosMidiCommand.CONTRAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestArKaos {

    @Inject private ArKaos mediaServer;
    @Inject private Midi midi;

    @Story
    public void testCommands() {

        then_midi_device_is_open_$1(false);

        when_start_media_server();
        then_midi_device_is_open_$1(true);

//        when_trigger_cue_list_$1(0);
//        then_last_command_was_$1_value_$2(CUE_LIST, 0);

//        when_trigger_cue_list_$1(9);
//        then_last_command_was_$1_value_$2(CUE_LIST, 9);

        when_set_brightness_$1(50);
        then_last_command_was_$1_value_$2(BRIGHTNESS, 50);

        when_set_contrast_$1(25);
        then_last_command_was_$1_value_$2(CONTRAST, 25);

        when_stop_media_server();
        then_midi_device_is_open_$1(false);

    }

    void when_start_media_server() {
        mediaServer.startup();
    }

    void when_stop_media_server() {
        mediaServer.shutdown();
    }

    void when_trigger_cue_list_$1(CueList cueList) {
        mediaServer.startCueList(cueList);
    }

    void when_set_brightness_$1(int brightness) {
        mediaServer.setBrightness(brightness);
    }

    void when_set_contrast_$1(int contrast) {
        mediaServer.setContrast(contrast);
    }

    void then_midi_device_is_open_$1(boolean open) {
        assertEquals(open, ((MidiMock)midi).isDeviceOpen());
    }

    void then_last_command_was_$1_value_$2(ArKaosMidiCommand arCommand, int value) {
        MidiCommand command = arCommand.command;
        int[] lastCommand = ((MidiMock)midi).getLastCommand();
//        assertNotNull(lastCommand);
//        assertEquals(4, lastCommand.length);
//        assertEquals(command.getCommand(), lastCommand[0]);
//        assertEquals(command.getChannel(), lastCommand[1]);
//        assertEquals(command.getData1(), lastCommand[3]);
//        assertEquals(value, lastCommand[2]);
    }

}
