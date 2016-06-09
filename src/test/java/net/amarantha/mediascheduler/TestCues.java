package net.amarantha.mediascheduler;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.mediascheduler.cue.CueFactory;
import net.amarantha.mediascheduler.cue.HttpCue;
import net.amarantha.mediascheduler.cue.MidiCue;
import net.amarantha.mediascheduler.http.MockHttpService;
import net.amarantha.mediascheduler.http.Param;
import net.amarantha.mediascheduler.midi.MockMidiService;
import org.junit.Before;
import org.junit.runner.RunWith;

import static javax.sound.midi.ShortMessage.CONTROL_CHANGE;
import static javax.sound.midi.ShortMessage.NOTE_ON;
import static org.glassfish.grizzly.http.Method.GET;
import static org.glassfish.grizzly.http.Method.POST;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestCues extends TestCase {

    @Inject private CueFactory cueFactory;

    private MidiCue midiCue1;
    private MidiCue midiCue2;

    private HttpCue httpCue1;
    private HttpCue httpCue2;

    @Story
    public void testMidiCues() {

        given_midi_device();
        given_midi_cues();

        then_last_midi_command_was_$1(null);

        when_trigger_cue_$1(midiCue1);
        then_last_midi_command_was_$1(midiCue1.getCommand());

        when_trigger_cue_$1(midiCue2);
        then_last_midi_command_was_$1(midiCue2.getCommand());

        when_trigger_cue_$1(midiCue1);
        then_last_midi_command_was_$1(midiCue1.getCommand());

    }

    @Story
    public void testHttpCues() {

        given_http_cues();

        then_last_http_call_was_$1(null);

        when_trigger_cue_$1(httpCue1);
        then_last_http_call_was_$1(httpCue1.getDescription());

        when_trigger_cue_$1(httpCue2);
        then_last_http_call_was_$1(httpCue2.getDescription());

        when_trigger_cue_$1(httpCue1);
        then_last_http_call_was_$1(httpCue1.getDescription());

    }

    private void given_http_cues() {
        httpCue1 = cueFactory.makeHttpCue(1, "HttpCue1", GET,  "testHost1", "path", null, new Param("page","1"));
        httpCue2 = cueFactory.makeHttpCue(2, "HttpCue2", POST, "testHost2", "other/path", "body");
    }

    void given_midi_cues() {
        midiCue1 = cueFactory.makeMidiCue(1, "MidiCue1", NOTE_ON, 1, 55, 55);
        midiCue2 = cueFactory.makeMidiCue(2, "MidiCue2", CONTROL_CHANGE, 3, 15, 25);
    }

    @Before
    public void given_the_services() {
        ((MockMidiService)midi).clearLastCommand();
        ((MockHttpService)http).clearLastCall();
    }

}
