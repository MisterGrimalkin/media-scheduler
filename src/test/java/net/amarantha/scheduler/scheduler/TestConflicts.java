package net.amarantha.scheduler.scheduler;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestCase;
import net.amarantha.scheduler.TestModule;
import net.amarantha.scheduler.exception.ScheduleConflictException;
import org.junit.runner.RunWith;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestConflicts extends TestCase {

    @Story
    public void test() {

        when_start_scheduler();
        makeTestCues();
        when_setup_cues();

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

}
