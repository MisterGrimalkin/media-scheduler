package net.amarantha.scheduler.scheduler;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestCase;
import net.amarantha.scheduler.TestModule;
import net.amarantha.scheduler.exception.ScheduleConflictException;
import org.junit.runner.RunWith;

import static java.time.DayOfWeek.*;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestRepeatsConflicts extends TestCase {

    @Story
    public void test() {

        when_start_scheduler();
        makeTestCues();
        when_setup_cues();

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-14", "10:00", "12:00", MONDAY, SUNDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-21", "11:00", "13:00", ScheduleConflictException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-23", "21:00", "23:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-14", "21:00", "23:30", ScheduleConflictException.class, MONDAY, WEDNESDAY);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-17", "21:00", "23:00", THURSDAY, FRIDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-15", "21:00", "23:30", ScheduleConflictException.class, TUESDAY, FRIDAY);

    }

}
