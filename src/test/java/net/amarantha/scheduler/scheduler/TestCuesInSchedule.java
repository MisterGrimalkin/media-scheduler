package net.amarantha.scheduler.scheduler;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestCase;
import net.amarantha.scheduler.TestModule;
import net.amarantha.scheduler.exception.CueNotFoundException;
import org.junit.runner.RunWith;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestCuesInSchedule extends TestCase {

    @Story
    public void test() {

        scheduler.clearSchedules();

        when_start_scheduler();
        makeTestCues();
        when_setup_cues();

        then_there_are_$1_cues(4);
        then_there_are_$1_events_today(0);

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

}
