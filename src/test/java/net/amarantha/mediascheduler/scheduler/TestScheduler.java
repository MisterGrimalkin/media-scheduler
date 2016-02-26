package net.amarantha.mediascheduler.scheduler;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import com.sun.javaws.exceptions.InvalidArgumentException;
import net.amarantha.mediascheduler.TestModule;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.entity.CueList;
import net.amarantha.mediascheduler.entity.MediaEvent;
import net.amarantha.mediascheduler.midi.Midi;
import net.amarantha.mediascheduler.midi.MidiMock;
import net.amarantha.mediascheduler.utility.Now;
import org.junit.runner.RunWith;

import java.time.DayOfWeek;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestScheduler {

    @Inject private Now now;

    @Inject private Scheduler scheduler;

    @Inject private Midi midi;
    @Inject private Projector projector;

    @Story
    public void testScheduleCursor() {

        given_the_scheduler();

        String cueList1 = "Dragons";
        String cueList2 = "Polar Bears";

        // Test invalid arguments
        when_add_event_$1_on_$2_from_$3_to_$4(cueList1, "2016-03-01", "12:00", "11:00", InvalidArgumentException.class);

        when_add_event_$1_on_$2_from_$3_to_$4(cueList1, "2016-03-02", "10:00", "11:00");
        when_add_event_$1_on_$2_from_$3_to_$4(cueList2, "2016-03-02", "12:00", "14:00");

        when_date_is_$1("2016-03-01");
        then_schedule_for_today_has_$1_events(0);

        when_date_is_$1("2016-03-02");
        then_schedule_for_today_has_$1_events(2);

        when_time_is_$1("00:00");   then_current_cuelist_is_$1(null);
        when_time_is_$1("09:59");   then_current_cuelist_is_$1(null);
        when_time_is_$1("10:00");   then_current_cuelist_is_$1(cueList1);
        when_time_is_$1("10:59");   then_current_cuelist_is_$1(cueList1);
        when_time_is_$1("11:00");   then_current_cuelist_is_$1(null);
        when_time_is_$1("12:00");   then_current_cuelist_is_$1(cueList2);
        when_time_is_$1("13:30");   then_current_cuelist_is_$1(cueList2);
        when_time_is_$1("14:00");   then_current_cuelist_is_$1(null);

        when_shutdown();

    }

    void then_current_cuelist_is_$1(String name) {
        MediaEvent currentEvent = scheduler.getCurrentEvent();
        if ( currentEvent==null ) {
            assertNull(name);
        } else {
            assertEquals(name, currentEvent.getCueList().getName());
        }
    }

    void then_schedule_for_today_has_$1_events(int count) {
        assertEquals(count, scheduler.getSchedule(1).getEvents(now.date()).size());
    }

    void given_the_scheduler() {
        then_midi_active_$1(false);
        then_projector_active_$1(false);
        when_start_scheduler();
        then_midi_active_$1(true);
        then_projector_active_$1(true);
    }

    void when_shutdown() {
        when_stop_scheduler();
        then_midi_active_$1(false);
        then_projector_active_$1(false);
    }

    void when_date_is_$1(String date) {
        now.setDate(date);
    }

    void when_time_is_$1(String time) {
        now.setTime(time);
    }

    void when_start_scheduler() {
        scheduler.startup();
    }

    void when_stop_scheduler() {
        scheduler.shutdown();
    }

    private static int nextEventId = 1;
    private static int nextCueListId = 1;

    private Long when_add_event_$1_on_$2_from_$3_to_$4(String cueListName, String date, String start, String end, DayOfWeek... repeats) {
        return when_add_event_$1_on_$2_from_$3_to_$4(cueListName, date, start, end, null, repeats);
    }

    Long when_add_event_$1_on_$2_from_$3_to_$4(String cueListName, String date, String start, String end, Class<? extends Exception> expectedExceptionClass, DayOfWeek... repeats) {
        Long result = null;
        try {
            result = scheduler.addEvent(new MediaEvent(nextEventId++, new CueList(nextCueListId++, cueListName), date, start, end, repeats)).getId();
            if ( expectedExceptionClass!=null ) {
                fail("Expected an exception");
            }
        } catch (Exception e) {
            if (expectedExceptionClass == null) {
                fail("Did not expect an exception: " + e.getMessage());
            }
            if (e.getClass() != expectedExceptionClass) {
                fail("Wrong exception thrown");
            }
        }
        return result;
    }

    void then_midi_active_$1(boolean active) {
        assertEquals(active,( (MidiMock) midi).isDeviceOpen());
    }

    void then_projector_active_$1(boolean active) {
        assertEquals(active, projector.isOn());
    }


}
