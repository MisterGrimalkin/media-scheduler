package net.amarantha.scheduler;

import com.google.inject.Inject;
import net.amarantha.scheduler.cue.Cue;
import net.amarantha.scheduler.cue.CueFactory;
import net.amarantha.scheduler.exception.CueInUseException;
import net.amarantha.scheduler.exception.DuplicateCueException;
import net.amarantha.scheduler.exception.SchedulerException;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.http.MockHttpService;
import net.amarantha.scheduler.midi.MidiCommand;
import net.amarantha.scheduler.midi.MidiService;
import net.amarantha.scheduler.midi.MockMidiService;
import net.amarantha.scheduler.scheduler.MediaEvent;
import net.amarantha.scheduler.scheduler.Schedule;
import net.amarantha.scheduler.scheduler.Scheduler;
import net.amarantha.scheduler.utility.Now;
import org.glassfish.grizzly.http.Method;
import org.testng.Assert;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static javax.sound.midi.ShortMessage.NOTE_ON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class TestCase {

    protected Cue cueFail;
    protected Cue cueDupe;
    protected Cue cue1;
    protected Cue cue2;
    protected Cue cue3;
    protected Cue cue4;


    @Inject protected Now now;
    @Inject protected Scheduler scheduler;

    @Inject protected MidiService midi;
    @Inject protected HttpService http;

    @Inject protected CueFactory cueFactory;

    protected void given_midi_device() {
        midi.openDevice();
    }

    protected void then_last_http_call_was_$1(String call) {
        if ( call==null ) {
            assertNull(((MockHttpService)http).getLastHttpCall());
        } else {
            assertEquals(call, ((MockHttpService) http).getLastHttpCall());
        }
    }

    protected void when_trigger_cue_$1(Cue cue) {
        cue.start();
    }

    protected void then_last_midi_command_was_$1(MidiCommand command) {
        if ( command==null ) {
            assertNull(((MockMidiService)midi).getLastMidiCommand());
        } else {
            Assert.assertEquals(command, ((MockMidiService) midi).getLastMidiCommand());
        }
    }

    //////////
    // When //
    //////////

    protected void when_start_scheduler() {
        scheduler.clearCues();
        scheduler.clearSchedules();
        scheduler.startup();
        scheduler.pause(true);      // We will manually "tick" the scheduler
    }


    protected void when_stop_scheduler() {
        scheduler.shutdown();
    }

    protected void when_date_is_$1(String date) {
        now.setDate(date);
        scheduler.checkSchedule();
    }

    protected void when_time_is_$1(String time) {
        now.setTime(time);
        scheduler.checkSchedule();
    }

    protected void when_add_cue_$1(Cue cue, boolean expectFail) {
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

    protected void when_remove_cue_$1(Cue cue, boolean expectFail) {
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

    protected Integer when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(int priority, Cue cue, String date, String start, String end, DayOfWeek... repeats) {
        return when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(priority, cue, date, start, end, null, repeats);
    }

    protected Integer when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(int priority, Cue cue, String date, String start, String end, Class<? extends Exception> expectedExceptionClass, DayOfWeek... repeats) {
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

    protected void when_switch_event_$1_to_priority_$2(Integer eventId, int priority) {
        when_switch_event_$1_to_priority_$2(eventId, priority, null);
    }

    protected void when_switch_event_$1_to_priority_$2(Integer eventId, int priority, Class<? extends SchedulerException> expectedExceptionClass) {
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

    protected void when_remove_event_$1(long eventId) {
        scheduler.removeEvent(eventId);
    }

    private static int nextEventId = 1;


    //////////
    // Then //
    //////////

    protected void then_there_are_$1_cues(int count) {
        assertEquals(count, scheduler.getCues().size());
    }

    protected void then_there_are_$1_events_today(int count) {
        int total = 0;
        for (Map.Entry<Integer, Schedule> entry : scheduler.getSchedules().entrySet() ) {
            List<MediaEvent> events = entry.getValue().getEvents(now.date());
            total += events.size();
        }
        assertEquals(count, total);
    }

    protected void then_there_are_$1_events_between_$2_and_$3(int count, String fromStr, String toStr) {
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

    protected void then_event_$1_exists_$2(Integer eventId, boolean exists) {
        MediaEvent event = scheduler.getEventById(eventId);
        assertEquals(exists, event!=null);
        try {
            event = scheduler.switchPriority(eventId, Scheduler.MAX_PRIORITY);
            assertEquals(exists, event!=null);
        } catch (SchedulerException e) {
            fail("Did not expect an exception: " + e.getMessage());
        }
    }

    protected void then_event_$1_is_$2(Integer eventId, Cue cue) {
        MediaEvent actualEvent = scheduler.getEventById(eventId);
        assertEquals(cue.getId(), actualEvent.getCueId());
    }

    protected void then_event_$1_end_time_id_$2(long eventId, String endTime) {
        MediaEvent actualEvent = scheduler.getEventById(eventId);
        assertEquals(endTime, actualEvent.getEndTimeString());
    }

    protected void then_current_cue_is_$1(Cue cue) {
        MediaEvent currentEvent = scheduler.getCurrentEvent();
        if ( currentEvent==null ) {
            assertNull(cue);
        } else {
            assertEquals(cue.getId(), currentEvent.getCueId());
        }
    }

    protected void then_exception_thrown(Class<? extends Exception> expectedExceptionClass, Class<? extends Exception> actualExceptionClass) {
        if (actualExceptionClass != expectedExceptionClass) {
            fail("Wrong exception thrown");
        }
    }

    protected void makeTestCues() {
        cueFail =   cueFactory.makeMidiCue(0, "This Will Fail",   NOTE_ON, 1,  0, 0);
        cueDupe =   cueFactory.makeMidiCue(1, "Duplicate",        NOTE_ON, 1, 99, 0);
        cue1 =      cueFactory.makeMidiCue(1, "Dragons",          NOTE_ON, 1,  1, 0);
        cue2 =      cueFactory.makeMidiCue(2, "Polar Bears",      NOTE_ON, 1,  2, 0);
        cue3 =      cueFactory.makeHttpCue(3, "Skinny Dips",      Method.GET, "host1", "path1", null);
        cue4 =      cueFactory.makeHttpCue(4, "Does Not Exist",   Method.POST, "host2", "path2", "payload");
    }

    protected void when_setup_cues() {
        when_add_cue_$1(cueFail, false);
        when_add_cue_$1(cue1, false);
        when_add_cue_$1(cue2, false);
        when_add_cue_$1(cue3, false);
        then_there_are_$1_cues(4);
    }



}
