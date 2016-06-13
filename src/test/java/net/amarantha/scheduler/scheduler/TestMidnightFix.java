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
public class TestMidnightFix extends TestCase {

    @Story
    public void test() {

        when_start_scheduler();
        makeTestCues();
        when_setup_cues();

        Integer id =
                when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-14", "10:00", "00:00");
        then_event_$1_end_time_id_$2(id, "23:59");

    }

}
