package net.amarantha.mediascheduler.scheduler;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.mediascheduler.TestModule;
import net.amarantha.mediascheduler.device.Projector;
import net.amarantha.mediascheduler.exception.DuplicateEventException;
import net.amarantha.mediascheduler.exception.ScheduleConflictException;
import net.amarantha.mediascheduler.exception.SchedulerException;
import net.amarantha.mediascheduler.midi.Midi;
import net.amarantha.mediascheduler.midi.MidiMock;
import net.amarantha.mediascheduler.utility.Now;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestScheduler {

    @Inject private Now now;

    @Inject private Scheduler scheduler;

    @Inject private Midi midi;
    @Inject private Projector projector;

    @Story
    public void test() {

//        given_date_$1("01/03/2016");
//        given_time_$1("09:00");
//
//        when_add_event_$2(1, "Test 1", "01/03/2016 10:00", "01/03/2016 13:00");
//        then_there_are_$1_events(1);
//        when_add_event_$2(2, "Test 1", "01/03/2016 13:00", "01/03/2016 15:00", DuplicateEventException.class);
//        then_there_are_$1_events(1);

        LocalDateTime start = LocalDateTime.parse("2016-03-01T10:50:00");
        System.out.println(start.toLocalDate());





    }

    void given_date_$1(String date) {
        now.setDate(date);
    }

    void given_time_$1(String time) {
        now.setTime(time);
    }

    void when_start_scheduler() {
        scheduler.startup();
    }

    void when_stop_scheduler() {
        scheduler.shutdown();
    }

    private Long when_add_event_$2(int cuelist, String description, String start, String end) {
        return when_add_event_$2(cuelist, description, start, end, null);
    }
    Long when_add_event_$2(int cuelist, String description, String start, String end, Class<? extends SchedulerException> expectedExceptionClass) {
        Long result = null;
//        try {
//            result = scheduler.createEvent(cuelist, description, start, end).getId();
//            if ( expectedExceptionClass!=null ) {
//                fail("Expected an exception");
//            }
//        } catch (SchedulerException e) {
//            if ( expectedExceptionClass==null ) {
//                fail("Did not expect an exception");
//            }
//            if ( e.getClass()!=expectedExceptionClass ) {
//                fail("Wrong exception thrown");
//            }
//        }
        return result;
    }

    void then_there_are_$1_events(int count) {
        assertEquals(count, scheduler.getEvents().size());
    }

    void then_midi_active_$1(boolean active) {
        assertEquals(active,( (MidiMock) midi).isDeviceOpen());
    }

    void then_projector_active_$1(boolean active) {
        assertEquals(active, projector.isOn());
    }


}
