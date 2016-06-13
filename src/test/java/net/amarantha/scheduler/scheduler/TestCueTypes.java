package net.amarantha.scheduler.scheduler;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestCase;
import net.amarantha.scheduler.TestModule;
import net.amarantha.scheduler.cue.HttpCue;
import net.amarantha.scheduler.cue.MidiCue;
import net.amarantha.scheduler.http.MockHttpService;
import net.amarantha.scheduler.midi.MockMidiService;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestCueTypes extends TestCase {

    @Story
    public void test() {

        given_midi_device();

        when_start_scheduler();
        makeTestCues();
        when_setup_cues();

        when_add_cue_$1(cue4, false);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue1, "2016-06-09", "10:00", "11:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, "2016-06-09", "11:00", "12:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-06-09", "12:00", "13:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue4, "2016-06-09", "13:00", "14:00");

        when_date_is_$1("2016-06-09");
        when_time_is_$1("09:30");
        then_last_midi_command_was_$1(null);
        then_last_http_call_was_$1(null);

        when_date_is_$1("2016-06-09");
        when_time_is_$1("10:30");
        then_last_midi_command_was_$1(((MidiCue)cue1).getCommand());
        then_last_http_call_was_$1(null);

        when_date_is_$1("2016-06-09");
        when_time_is_$1("11:30");
        then_last_midi_command_was_$1(((MidiCue)cue1).getCommand());
        then_last_http_call_was_$1(((HttpCue)cue3).getDescription());

        when_date_is_$1("2016-06-09");
        when_time_is_$1("12:30");
        then_last_midi_command_was_$1(((MidiCue)cue2).getCommand());
        then_last_http_call_was_$1(((HttpCue)cue3).getDescription());

        when_date_is_$1("2016-06-09");
        when_time_is_$1("13:30");
        then_last_midi_command_was_$1(((MidiCue)cue2).getCommand());
        then_last_http_call_was_$1(((HttpCue)cue4).getDescription());

    }

    @Before
    public void given_the_services() {
        ((MockMidiService)midi).clearLastCommand();
        ((MockHttpService)http).clearLastCall();
    }


}
