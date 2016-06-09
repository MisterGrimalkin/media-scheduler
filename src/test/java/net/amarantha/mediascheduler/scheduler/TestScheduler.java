package net.amarantha.mediascheduler.scheduler;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.mediascheduler.TestCase;
import net.amarantha.mediascheduler.TestModule;
import net.amarantha.mediascheduler.cue.Cue;
import net.amarantha.mediascheduler.cue.CueFactory;
import net.amarantha.mediascheduler.cue.HttpCue;
import net.amarantha.mediascheduler.cue.MidiCue;
import net.amarantha.mediascheduler.exception.*;
import net.amarantha.mediascheduler.http.MockHttpService;
import net.amarantha.mediascheduler.midi.MockMidiService;
import org.glassfish.grizzly.http.Method;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import static java.time.DayOfWeek.*;
import static javax.sound.midi.ShortMessage.NOTE_ON;
import static net.amarantha.mediascheduler.scheduler.Scheduler.MAX_PRIORITY;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestScheduler extends TestCase {

    @Inject private CueFactory cueFactory;

    private Cue cueFail;
    private Cue cueDupe;
    private Cue cue1;
    private Cue cue2;
    private Cue cue3;
    private Cue cue4;

    private void makeTestCues() {
        cueFail =   cueFactory.makeMidiCue(0, "This Will Fail",   NOTE_ON, 1,  0, 0);
        cueDupe =   cueFactory.makeMidiCue(1, "Duplicate",        NOTE_ON, 1, 99, 0);
        cue1 =      cueFactory.makeMidiCue(1, "Dragons",          NOTE_ON, 1,  1, 0);
        cue2 =      cueFactory.makeMidiCue(2, "Polar Bears",      NOTE_ON, 1,  2, 0);
        cue3 =      cueFactory.makeHttpCue(3, "Skinny Dips",      Method.GET, "host1", "path1", null);
        cue4 =      cueFactory.makeHttpCue(4, "Does Not Exist",   Method.POST, "host2", "path2", "payload");
    }

    @Story
    public void testCueTypes() {

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

    @Story
    public void testCueLists() {

        then_there_are_$1_cues(4);

        when_add_cue_$1(cueDupe, true);

        then_there_are_$1_cues(4);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue4, "2016-03-02", "10:00", "11:00", CueNotFoundException.class);

        when_add_cue_$1(cue4, false);
        then_there_are_$1_cues(5);

        Integer id =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue4, "2016-03-02", "10:00", "11:00");

        when_remove_cue_$1(cue4, true);
        then_there_are_$1_cues(5);

        when_remove_event_$1(id);

        when_remove_cue_$1(cue4, false);
        then_there_are_$1_cues(4);

    }

    @Story
    public void testScheduleCursor() {

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cueFail, "2016-03-01", "12:00", "11:00",
                IllegalArgumentException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue1, "2016-03-02", "10:00", "11:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-02", "12:00", "14:00");
        Integer id3 =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, "2016-03-04", "14:00", "18:00");

        then_event_$1_exists_$2(id3, true);
        then_event_$1_is_$2(id3, cue3);

        then_there_are_$1_events_between_$2_and_$3(3, "2016-03-01", "2016-03-08");

        when_date_is_$1("2016-03-01");
        then_there_are_$1_events_today(0);

        when_date_is_$1("2016-03-02");
        then_there_are_$1_events_today(2);

        when_time_is_$1("00:00");
        then_current_cue_is_$1(null);

        when_time_is_$1("09:59");
        then_current_cue_is_$1(null);

        when_time_is_$1("10:00");
        then_current_cue_is_$1(cue1);

        when_time_is_$1("10:59");
        then_current_cue_is_$1(cue1);

        when_time_is_$1("11:00");
        then_current_cue_is_$1(null);

        when_time_is_$1("12:00");
        then_current_cue_is_$1(cue2);

        when_time_is_$1("13:30");
        then_current_cue_is_$1(cue2);

        when_time_is_$1("14:00");
        then_current_cue_is_$1(null);

        when_date_is_$1("2016-03-04");
        then_there_are_$1_events_today(1);
        then_current_cue_is_$1(cue3);

        when_remove_event_$1(id3);
        then_event_$1_exists_$2(id3, false);
        then_there_are_$1_events_today(0);
        then_there_are_$1_events_between_$2_and_$3(2, "2016-03-01", "2016-03-08");
        then_current_cue_is_$1(null);

    }

    @Story
    public void testSchedulePriority() {

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(0, cueFail, "2016-03-01", "10:00", "11:00",
                PriorityOutOfBoundsException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(MAX_PRIORITY+1, cueFail, "2016-03-01", "10:00", "11:00",
                PriorityOutOfBoundsException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(2, cue1, "2016-03-03", "10:00", "12:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(2, cue2, "2016-03-03", "12:00", "14:00");

        when_date_is_$1("2016-03-03");

        then_there_are_$1_events_today(2);

        when_time_is_$1("11:30");
        then_current_cue_is_$1(cue1);

        when_time_is_$1("12:30");
        then_current_cue_is_$1(cue2);

        Integer id =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(3, cue3, "2016-03-03", "11:00", "13:00");

        then_there_are_$1_events_today(3);

        when_time_is_$1("10:30");
        then_current_cue_is_$1(cue1);

        when_time_is_$1("11:30");
        then_current_cue_is_$1(cue3);

        when_time_is_$1("12:30");
        then_current_cue_is_$1(cue3);

        when_switch_event_$1_to_priority_$2(id, 1);
        then_current_cue_is_$1(cue2);
        when_switch_event_$1_to_priority_$2(id, 3);
        then_current_cue_is_$1(cue3);
        when_switch_event_$1_to_priority_$2(id, -1, PriorityOutOfBoundsException.class);
        then_current_cue_is_$1(cue3);
        when_switch_event_$1_to_priority_$2(id, 2, ScheduleConflictException.class);
        then_current_cue_is_$1(cue3);

        when_time_is_$1("13:30");
        then_current_cue_is_$1(cue2);

    }

    @Story
    public void testConflicts() {

        String date = "2016-03-03";

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue1, date, "10:00", "12:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, date, "14:00", "16:00");

        when_date_is_$1(date);

        then_there_are_$1_events_today(2);

        // No conflicts

        Integer id = when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "09:00", "10:00");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);

        id = when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "12:00", "14:00");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);

        id = when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "16:00", "16:01");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);
        when_remove_event_$1(99);
        then_there_are_$1_events_today(2);

        // Conflicts

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "09:00", "11:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "10:30", "11:30", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "11:00", "13:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "13:00", "17:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

    }

    @Story
    public void testRepeats() {

        String date = "2016-03-14";         // a Monday

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue1, date, "10:00", "12:00", MONDAY, TUESDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, date, "12:00", "14:00", TUESDAY, WEDNESDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "14:00", "18:00", WEDNESDAY, FRIDAY, SATURDAY);

        then_there_are_$1_events_between_$2_and_$3(0, "2016-03-07", "2016-03-13");
        then_there_are_$1_events_between_$2_and_$3(7, "2016-03-14", "2016-03-20");
        then_there_are_$1_events_between_$2_and_$3(7, "2016-03-21", "2016-03-27");

        when_date_is_$1("2016-03-21");
        then_there_are_$1_events_today(1);
        when_time_is_$1("09:00");
        then_current_cue_is_$1(null);
        when_time_is_$1("11:00");
        then_current_cue_is_$1(cue1);

        when_date_is_$1("2016-03-29");
        then_there_are_$1_events_today(2);
        when_time_is_$1("11:00");
        then_current_cue_is_$1(cue1);
        when_time_is_$1("13:00");
        then_current_cue_is_$1(cue2);

        when_date_is_$1("2016-03-30");
        then_there_are_$1_events_today(2);
        when_time_is_$1("13:00");
        then_current_cue_is_$1(cue2);
        when_time_is_$1("14:00");
        then_current_cue_is_$1(cue3);

        when_date_is_$1("2016-03-31");
        then_there_are_$1_events_today(0);

    }

    @Story
    public void testRepeatsConflicts() {

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-14", "10:00", "12:00", MONDAY, SUNDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-21", "11:00", "13:00", ScheduleConflictException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-23", "21:00", "23:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-14", "21:00", "23:30", ScheduleConflictException.class, MONDAY, WEDNESDAY);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-17", "21:00", "23:00", THURSDAY, FRIDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-15", "21:00", "23:30", ScheduleConflictException.class, TUESDAY, FRIDAY);
    }

    @Story
    public void testMidnightFix() {
        Integer id =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-14", "10:00", "00:00");
        then_event_$1_end_time_id_$2(id, "23:59");
    }


    ///////////
    // Setup //
    ///////////

    @Before
    public void given_the_scheduler() {
        when_start_scheduler();
        makeTestCues();
        when_setup_cuelists();
        ((MockMidiService)midi).clearLastCommand();
        ((MockHttpService)http).clearLastCall();
    }

    @After
    public void when_shutdown() {
        when_stop_scheduler();
    }

    void when_setup_cuelists() {
        when_add_cue_$1(cueFail, false);
        when_add_cue_$1(cue1, false);
        when_add_cue_$1(cue2, false);
        when_add_cue_$1(cue3, false);
        then_there_are_$1_cues(4);
    }



}
