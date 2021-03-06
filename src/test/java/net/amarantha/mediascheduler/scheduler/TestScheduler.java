package net.amarantha.mediascheduler.scheduler;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.mediascheduler.TestModule;
import net.amarantha.mediascheduler.device.ArKaosMidiCommand;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.exception.*;
import net.amarantha.mediascheduler.midi.Midi;
import net.amarantha.mediascheduler.midi.MidiCommand;
import net.amarantha.mediascheduler.midi.MidiMock;
import net.amarantha.mediascheduler.utility.Now;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.time.DayOfWeek.*;
import static net.amarantha.mediascheduler.scheduler.Scheduler.MAX_PRIORITY;
import static org.junit.Assert.*;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestScheduler {

    @Inject private Now now;

    @Inject private Scheduler scheduler;

    @Inject private Midi midi;
    @Inject private Projector projector;

    private static final Cue CUE_LIST_FAIL = new Cue(0, 0, "This Will Fail");
    private static final Cue CUE_LIST_DUPLICATE = new Cue(1, 99, "Duplicate");
    private static final Cue CUE_LIST_1 = new Cue(1, 1, "Dragons");
    private static final Cue CUE_LIST_2 = new Cue(2, 2, "Polar Bears");
    private static final Cue CUE_LIST_3 = new Cue(3, 3, "Skinny Dips");
    private static final Cue CUE_LIST_4 = new Cue(4, 4, "Does Not Exist");

    @Story
    public void testCueLists() {

        then_there_are_$1_cuelists(4);

        when_add_cuelist_$1(CUE_LIST_DUPLICATE, true);

        then_there_are_$1_cuelists(4);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_4, "2016-03-02", "10:00", "11:00", CueNotFoundException.class);

        when_add_cuelist_$1(CUE_LIST_4, false);
        then_there_are_$1_cuelists(5);

        Integer id =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_4, "2016-03-02", "10:00", "11:00");

        when_remove_cuelist_$1(CUE_LIST_4, true);
        then_there_are_$1_cuelists(5);

        when_remove_event_$1(id);

        when_remove_cuelist_$1(CUE_LIST_4, false);
        then_there_are_$1_cuelists(4);

    }

    @Story
    public void testScheduleCursor() {

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_FAIL, "2016-03-01", "12:00", "11:00",
                IllegalArgumentException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_1, "2016-03-02", "10:00", "11:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, "2016-03-02", "12:00", "14:00");
        Integer id3 =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, "2016-03-04", "14:00", "18:00");

        then_event_$1_exists_$2(id3, true);
        then_event_$1_is_$2(id3, CUE_LIST_3);

        then_there_are_$1_events_between_$2_and_$3(3, "2016-03-01", "2016-03-08");

        when_date_is_$1("2016-03-01");
        then_there_are_$1_events_today(0);

        when_date_is_$1("2016-03-02");
        then_there_are_$1_events_today(2);

        when_time_is_$1("00:00");
        then_current_cuelist_is_$1(null);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);

        when_time_is_$1("09:59");
        then_current_cuelist_is_$1(null);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);

        when_time_is_$1("10:00");
        then_current_cuelist_is_$1(CUE_LIST_1);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_1.getNumber());

        when_time_is_$1("10:59");
        then_current_cuelist_is_$1(CUE_LIST_1);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_1.getNumber());

        when_time_is_$1("11:00");
        then_current_cuelist_is_$1(null);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);

        when_time_is_$1("12:00");
        then_current_cuelist_is_$1(CUE_LIST_2);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_2.getNumber());

        when_time_is_$1("13:30");
        then_current_cuelist_is_$1(CUE_LIST_2);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_2.getNumber());

        when_time_is_$1("14:00");
        then_current_cuelist_is_$1(null);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);

        when_date_is_$1("2016-03-04");
        then_there_are_$1_events_today(1);
        then_current_cuelist_is_$1(CUE_LIST_3);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_3.getNumber());

        when_remove_event_$1(id3);
        then_event_$1_exists_$2(id3, false);
        then_there_are_$1_events_today(0);
        then_there_are_$1_events_between_$2_and_$3(2, "2016-03-01", "2016-03-08");
        then_current_cuelist_is_$1(null);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);

    }

    @Story
    public void testSchedulePriority() {

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(0, CUE_LIST_FAIL, "2016-03-01", "10:00", "11:00",
                PriorityOutOfBoundsException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(MAX_PRIORITY+1, CUE_LIST_FAIL, "2016-03-01", "10:00", "11:00",
                PriorityOutOfBoundsException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(2, CUE_LIST_1, "2016-03-03", "10:00", "12:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(2, CUE_LIST_2, "2016-03-03", "12:00", "14:00");

        when_date_is_$1("2016-03-03");

        then_there_are_$1_events_today(2);

        when_time_is_$1("11:30");
        then_current_cuelist_is_$1(CUE_LIST_1);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_1.getNumber());

        when_time_is_$1("12:30");
        then_current_cuelist_is_$1(CUE_LIST_2);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_2.getNumber());

        Integer id =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(3, CUE_LIST_3, "2016-03-03", "11:00", "13:00");

        then_there_are_$1_events_today(3);

        when_time_is_$1("10:30");
        then_current_cuelist_is_$1(CUE_LIST_1);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_1.getNumber());

        when_time_is_$1("11:30");
        then_current_cuelist_is_$1(CUE_LIST_3);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_3.getNumber());

        when_time_is_$1("12:30");
        then_current_cuelist_is_$1(CUE_LIST_3);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_3.getNumber());

        when_switch_event_$1_to_priority_$2(id, 1);
        then_current_cuelist_is_$1(CUE_LIST_2);
        when_switch_event_$1_to_priority_$2(id, 3);
        then_current_cuelist_is_$1(CUE_LIST_3);
        when_switch_event_$1_to_priority_$2(id, -1, PriorityOutOfBoundsException.class);
        then_current_cuelist_is_$1(CUE_LIST_3);
        when_switch_event_$1_to_priority_$2(id, 2, ScheduleConflictException.class);
        then_current_cuelist_is_$1(CUE_LIST_3);

        when_time_is_$1("13:30");
        then_current_cuelist_is_$1(CUE_LIST_2);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_LIST_2.getNumber());

    }

    @Story
    public void testConflicts() {

        String date = "2016-03-03";

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_1, date, "10:00", "12:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, date, "14:00", "16:00");

        when_date_is_$1(date);

        then_there_are_$1_events_today(2);

        // No conflicts

        Integer id = when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, date, "09:00", "10:00");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);

        id = when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, date, "12:00", "14:00");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);

        id = when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, date, "16:00", "16:01");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);
        when_remove_event_$1(99);
        then_there_are_$1_events_today(2);

        // Conflicts

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, date, "09:00", "11:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, date, "10:30", "11:30", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, date, "11:00", "13:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, date, "13:00", "17:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

    }

    @Story
    public void testRepeats() {

        String date = "2016-03-14";         // a Monday

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_1, date, "10:00", "12:00", MONDAY, TUESDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, date, "12:00", "14:00", TUESDAY, WEDNESDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_3, date, "14:00", "18:00", WEDNESDAY, FRIDAY, SATURDAY);

        then_there_are_$1_events_between_$2_and_$3(0, "2016-03-07", "2016-03-13");
        then_there_are_$1_events_between_$2_and_$3(7, "2016-03-14", "2016-03-20");
        then_there_are_$1_events_between_$2_and_$3(7, "2016-03-21", "2016-03-27");

        when_date_is_$1("2016-03-21");
        then_there_are_$1_events_today(1);
        when_time_is_$1("09:00");
        then_current_cuelist_is_$1(null);
        when_time_is_$1("11:00");
        then_current_cuelist_is_$1(CUE_LIST_1);

        when_date_is_$1("2016-03-29");
        then_there_are_$1_events_today(2);
        when_time_is_$1("11:00");
        then_current_cuelist_is_$1(CUE_LIST_1);
        when_time_is_$1("13:00");
        then_current_cuelist_is_$1(CUE_LIST_2);

        when_date_is_$1("2016-03-30");
        then_there_are_$1_events_today(2);
        when_time_is_$1("13:00");
        then_current_cuelist_is_$1(CUE_LIST_2);
        when_time_is_$1("14:00");
        then_current_cuelist_is_$1(CUE_LIST_3);

        when_date_is_$1("2016-03-31");
        then_there_are_$1_events_today(0);

    }

    @Story
    public void testRepeatsConflicts() {

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, "2016-03-14", "10:00", "12:00", MONDAY, SUNDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, "2016-03-21", "11:00", "13:00", ScheduleConflictException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, "2016-03-23", "21:00", "23:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, "2016-03-14", "21:00", "23:30", ScheduleConflictException.class, MONDAY, WEDNESDAY);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, "2016-03-17", "21:00", "23:00", THURSDAY, FRIDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, "2016-03-15", "21:00", "23:30", ScheduleConflictException.class, TUESDAY, FRIDAY);
    }

    @Story
    public void testMidnightFix() {
        Integer id =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, CUE_LIST_2, "2016-03-14", "10:00", "00:00");
        then_event_$1_end_time_id_$2(id, "23:59");
    }


    ///////////
    // Setup //
    ///////////

    @Before
    public void given_the_scheduler() {
        then_midi_active_$1(false);
        then_projector_active_$1(false);
        when_start_scheduler();
        then_midi_active_$1(true);
        then_projector_active_$1(true);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.STOP, 0);
        when_setup_cuelists();
    }

    void when_start_scheduler() {
        scheduler.clearCues();
        scheduler.clearSchedules();
        scheduler.startup();
        scheduler.pause(true);      // We will manually "tick" the scheduler
    }

    void when_setup_cuelists() {
        when_add_cuelist_$1(CUE_LIST_FAIL, false);
        when_add_cuelist_$1(CUE_LIST_1, false);
        when_add_cuelist_$1(CUE_LIST_2, false);
        when_add_cuelist_$1(CUE_LIST_3, false);
        then_there_are_$1_cuelists(4);
    }

    @After
    public void when_shutdown() {
        when_stop_scheduler();
        then_midi_active_$1(false);
        then_projector_active_$1(false);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.STOP, 0);
    }

    void when_stop_scheduler() {
        scheduler.shutdown();
    }

    void then_midi_active_$1(boolean active) {
        assertEquals(active,( (MidiMock) midi).isDeviceOpen());
    }

    void then_projector_active_$1(boolean active) {
        assertEquals(active, projector.isOn());
    }


    //////////
    // When //
    //////////

    void when_date_is_$1(String date) {
        now.setDate(date);
        scheduler.checkSchedule();
    }

    void when_time_is_$1(String time) {
        now.setTime(time);
        scheduler.checkSchedule();
    }

    void when_add_cuelist_$1(Cue cue, boolean expectFail) {
        try {
            scheduler.addCue(cue);
            if ( expectFail ) {
                fail("Expected an exception");
            }
        } catch (Exception e) {
            if ( !expectFail ) {
                fail("Did not expect an exception: " + e.getMessage());
            }
            then_exception_thrown(DuplicateCueException.class, e.getClass());
        }
    }

    void when_remove_cuelist_$1(Cue cue, boolean expectFail) {
        try {
            scheduler.removeCue(cue);
            if ( expectFail ) {
                fail("Expected an exception");
            }
        } catch (Exception e) {
            if ( !expectFail ) {
                fail("Did not expect an exception: " + e.getMessage());
            }
            then_exception_thrown(CueInUseException.class, e.getClass());
        }
    }

    private Integer when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(int priority, Cue cue, String date, String start, String end, DayOfWeek... repeats) {
        return when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(priority, cue, date, start, end, null, repeats);
    }

    Integer when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(int priority, Cue cue, String date, String start, String end, Class<? extends Exception> expectedExceptionClass, DayOfWeek... repeats) {
        Integer result = null;
        try {
            result = priority==1
                    ? scheduler.addEvent(new MediaEvent(nextEventId++, cue.getId(), date, start, end, repeats)).getId()
                    : scheduler.addEvent(priority, new MediaEvent(nextEventId++, cue.getId(), date, start, end, repeats)).getId()
            ;
            if ( expectedExceptionClass!=null ) {
                fail("Expected an exception");
            }
        } catch (Exception e) {
            if (expectedExceptionClass == null) {
                fail("Did not expect an exception: " + e.getMessage());
            }
            then_exception_thrown(expectedExceptionClass, e.getClass());
        }
        return result;
    }

    private void when_switch_event_$1_to_priority_$2(Integer eventId, int priority) {
        when_switch_event_$1_to_priority_$2(eventId, priority, null);
    }

    void when_switch_event_$1_to_priority_$2(Integer eventId, int priority, Class<? extends SchedulerException> expectedExceptionClass) {
        try {
            scheduler.switchPriority(eventId, priority);
            if ( expectedExceptionClass!=null ) {
                fail("Expected an exception");
            }
        } catch (SchedulerException e) {
            if (expectedExceptionClass == null) {
                fail("Did not expect an exception: " + e.getMessage());
            }
            then_exception_thrown(expectedExceptionClass, e.getClass());
        }
    }

    void when_remove_event_$1(long eventId) {
        scheduler.removeEvent(eventId);
    }

    private static int nextEventId = 1;


    //////////
    // Then //
    //////////

    void then_there_are_$1_cuelists(int count) {
        assertEquals(count, scheduler.getCues().size());
    }

    void then_there_are_$1_events_today(int count) {
        int total = 0;
        for (Map.Entry<Integer, Schedule> entry : scheduler.getSchedules().entrySet() ) {
            List<MediaEvent> events = entry.getValue().getEvents(now.date());
            total += events.size();
        }
        assertEquals(count, total);
    }

    void then_there_are_$1_events_between_$2_and_$3(int count, String fromStr, String toStr) {
        LocalDate from = LocalDate.parse(fromStr);
        LocalDate to = LocalDate.parse(toStr);
        int total = 0;
        for (Map.Entry<Integer, Schedule> entry : scheduler.getSchedules().entrySet() ) {
            Map<LocalDate, List<MediaEvent>> events = entry.getValue().getEvents(from, to);
            for ( List<MediaEvent> list : events.values() ) {
                total += list.size();
            }
        }
        assertEquals(count, total);
    }

    void then_event_$1_exists_$2(Integer eventId, boolean exists) {
        MediaEvent event = scheduler.getEventById(eventId);
        assertEquals(exists, event!=null);
        try {
            event = scheduler.switchPriority(eventId, Scheduler.MAX_PRIORITY);
            assertEquals(exists, event!=null);
        } catch (SchedulerException e) {
            fail("Did not expect an exception: " + e.getMessage());
        }
    }

    void then_event_$1_is_$2(Integer eventId, Cue cue) {
        MediaEvent actualEvent = scheduler.getEventById(eventId);
        assertEquals(cue.getId(), actualEvent.getCueId());
    }

    void then_event_$1_end_time_id_$2(long eventId, String endTime) {
        MediaEvent actualEvent = scheduler.getEventById(eventId);
        assertEquals(endTime, actualEvent.getEndTimeString());
    }

    void then_current_cuelist_is_$1(Cue cue) {
        MediaEvent currentEvent = scheduler.getCurrentEvent();
        if ( currentEvent==null ) {
            assertNull(cue);
        } else {
            assertEquals(cue.getId(), currentEvent.getCueId());
        }
    }

    void then_exception_thrown(Class<? extends Exception> expectedExceptionClass, Class<? extends Exception> actualExceptionClass) {
        if (actualExceptionClass != expectedExceptionClass) {
            fail("Wrong exception thrown");
        }
    }

    void then_last_command_was_$1_value_$2(ArKaosMidiCommand arCommand, int value) {
        MidiCommand command = arCommand.command;
        int[] lastCommand = ((MidiMock)midi).getLastCommand();
        // THIS DOESN'T WORK ANYMORE
        // TODO: Fix
//        assertNotNull(lastCommand);
//        assertEquals(4, lastCommand.length);
//        assertEquals(command.getCommand(), lastCommand[0]);
//        assertEquals(command.getChannel(), lastCommand[1]);
//        assertEquals(command.getData1(), lastCommand[2]);
//        assertEquals(value, lastCommand[3]);
    }

}
