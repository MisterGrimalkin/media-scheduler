package net.amarantha.scheduler.scheduler;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestCase;
import net.amarantha.scheduler.TestModule;
import net.amarantha.scheduler.exception.PriorityOutOfBoundsException;
import net.amarantha.scheduler.exception.ScheduleConflictException;
import org.junit.runner.RunWith;

import static net.amarantha.scheduler.scheduler.Scheduler.MAX_PRIORITY;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestScheduleConflicts extends TestCase {

    @Story
    public void testSchedulePriority() {

        when_start_scheduler();
        makeTestCues();
        when_setup_cues();

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

}
