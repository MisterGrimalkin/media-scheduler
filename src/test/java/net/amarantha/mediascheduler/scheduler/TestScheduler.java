package net.amarantha.mediascheduler.scheduler;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.mediascheduler.TestModule;
import net.amarantha.mediascheduler.device.ArKaosMidiCommand;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.entity.CueList;
import net.amarantha.mediascheduler.entity.MediaEvent;
import net.amarantha.mediascheduler.exception.PriorityOutOfBoundsException;
import net.amarantha.mediascheduler.exception.ScheduleConflictException;
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

import static net.amarantha.mediascheduler.scheduler.Scheduler.MAX_PRIORITY;
import static org.junit.Assert.*;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestScheduler {

    @Inject private Now now;

    @Inject private Scheduler scheduler;

    @Inject private Midi midi;
    @Inject private Projector projector;

    private static final int CUE_1 = 1;
    private static final String CUE_NAME_1 = "Dragons";
    private static final int CUE_2 = 2;
    private static final String CUE_NAME_2 = "Polar Bears";
    private static final int CUE_3 = 3;
    private static final String CUE_NAME_3 = "Skinny Dips";

    @Story
    public void testScheduleCursor() {

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, 1, "This Will Fail", "2016-03-01", "12:00", "11:00", IllegalArgumentException.class);

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_1, CUE_NAME_1, "2016-03-02", "10:00", "11:00");
        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_2, CUE_NAME_2, "2016-03-02", "12:00", "14:00");
            Long id3 =
        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_3, CUE_NAME_3, "2016-03-04", "14:00", "18:00");

        then_event_$1_is_$2(id3, CUE_NAME_3);

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
        then_current_cuelist_is_$1(CUE_NAME_1);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_1);

        when_time_is_$1("10:59");
        then_current_cuelist_is_$1(CUE_NAME_1);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_1);

        when_time_is_$1("11:00");
        then_current_cuelist_is_$1(null);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);

        when_time_is_$1("12:00");
        then_current_cuelist_is_$1(CUE_NAME_2);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_2);

        when_time_is_$1("13:30");
        then_current_cuelist_is_$1(CUE_NAME_2);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_2);

        when_time_is_$1("14:00");
        then_current_cuelist_is_$1(null);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);

        when_date_is_$1("2016-03-04");
        then_there_are_$1_events_today(1);
        then_current_cuelist_is_$1(CUE_NAME_3);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_3);

        when_remove_event_$1(id3);
        then_there_are_$1_events_today(0);
        then_current_cuelist_is_$1(null);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);

    }

    @Story
    public void testSchedulePriority() {

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(0, 1, "This Will Fail", "2016-03-01", "10:00", "11:00", PriorityOutOfBoundsException.class);
        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(MAX_PRIORITY+1, 1, "This Also Will Fail", "2016-03-01", "10:00", "11:00", PriorityOutOfBoundsException.class);

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_1, CUE_NAME_1, "2016-03-03", "10:00", "12:00");
        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_2, CUE_NAME_2, "2016-03-03", "12:00", "14:00");

        when_date_is_$1("2016-03-03");

        then_there_are_$1_events_today(2);

        when_time_is_$1("11:30");
        then_current_cuelist_is_$1(CUE_NAME_1);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_1);

        when_time_is_$1("12:30");
        then_current_cuelist_is_$1(CUE_NAME_2);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_2);

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(2, CUE_3, CUE_NAME_3, "2016-03-03", "11:00", "13:00");

        then_there_are_$1_events_today(3);

        when_time_is_$1("10:30");
        then_current_cuelist_is_$1(CUE_NAME_1);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_1);

        when_time_is_$1("11:30");
        then_current_cuelist_is_$1(CUE_NAME_3);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_3);

        when_time_is_$1("12:30");
        then_current_cuelist_is_$1(CUE_NAME_3);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_3);

        when_time_is_$1("13:30");
        then_current_cuelist_is_$1(CUE_NAME_2);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, CUE_2);

    }

    @Story
    public void testConflicts() {

        String date = "2016-03-03";

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_1, CUE_NAME_1, date, "10:00", "12:00");
        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_2, CUE_NAME_2, date, "14:00", "16:00");

        when_date_is_$1(date);

        then_there_are_$1_events_today(2);

        // No conflicts

        Long id = when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_3, CUE_NAME_3, date, "09:00", "10:00");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);

        id = when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_3, CUE_NAME_3, date, "12:00", "14:00");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);

        id = when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_3, CUE_NAME_3, date, "16:00", "16:01");
        then_there_are_$1_events_today(3);
        when_remove_event_$1(id);
        then_there_are_$1_events_today(2);

        // Conflicts

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_3, CUE_NAME_3, date, "09:00", "11:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_3, CUE_NAME_3, date, "10:30", "11:30", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_3, CUE_NAME_3, date, "11:00", "13:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

        when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(1, CUE_3, CUE_NAME_3, date, "13:00", "17:00", ScheduleConflictException.class);
        then_there_are_$1_events_today(2);

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
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);
    }

    void when_start_scheduler() {
        scheduler.clearSchedules();
        scheduler.startup();
        scheduler.pause();      // We will manually "tick" the scheduler
    }

    @After
    public void when_shutdown() {
        when_stop_scheduler();
        then_midi_active_$1(false);
        then_projector_active_$1(false);
        then_last_command_was_$1_value_$2(ArKaosMidiCommand.CUE_LIST, 0);
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

    private Long when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(int priority, int cueListId, String cueListName, String date, String start, String end, DayOfWeek... repeats) {
        return when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(priority, cueListId, cueListName, date, start, end, null, repeats);
    }

    Long when_add_priority_$1_event_$2_$3_on_$4_from_$5_to_$6(int priority, int cueListId, String cueListName, String date, String start, String end, Class<? extends Exception> expectedExceptionClass, DayOfWeek... repeats) {
        Long result = null;
        try {
            result = priority==1
                    ? scheduler.addEvent(new MediaEvent(nextEventId++, new CueList(cueListId, cueListName), date, start, end, repeats)).getId()
                    : scheduler.addEvent(priority, new MediaEvent(nextEventId++, new CueList(cueListId, cueListName), date, start, end, repeats)).getId()
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

    void when_remove_event_$1(long eventId) {
        assertTrue(scheduler.removeEvent(eventId));
    }

    private static int nextEventId = 1;


    //////////
    // Then //
    //////////

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

    void then_event_$1_is_$2(long eventId, String cueName) {
        MediaEvent actualEvent = scheduler.getEventById(eventId);
        assertEquals(cueName, actualEvent.getCueList().getName());
    }

    void then_current_cuelist_is_$1(String name) {
        MediaEvent currentEvent = scheduler.getCurrentEvent();
        if ( currentEvent==null ) {
            assertNull(name);
        } else {
            assertEquals(name, currentEvent.getCueList().getName());
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
        assertNotNull(lastCommand);
        assertEquals(4, lastCommand.length);
        assertEquals(command.command, lastCommand[0]);
        assertEquals(command.channel, lastCommand[1]);
        assertEquals(command.data1, lastCommand[2]);
        assertEquals(value, lastCommand[3]);
    }


}
