package net.amarantha.scheduler.scheduler;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestCase;
import net.amarantha.scheduler.TestModule;
import org.junit.runner.RunWith;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestScheduler extends TestCase {

    @Story
    public void test() {

        when_start_scheduler();
        makeTestCues();
        when_setup_cues();

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cueFail, "2016-03-01", "12:00", "11:00",
                IllegalArgumentException.class);

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue1, "2016-03-02", "10:00", "11:00");
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, "2016-03-02", "12:00", "14:00");
        Integer id3 =
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, "2016-03-04", "14:00", "18:00");

        then_event_$1_exists_$2(id3, true);
        then_event_$1_is_$2(id3, cue3);

        then_there_are_$1_events_between_$2_and_$3(3, "2016-03-01", "2016-03-08");

        when_date_is_$1("2016-03-01");
        then_there_are_$1_events_today(0);

        when_date_is_$1("2016-03-02");
        then_there_are_$1_events_today(2);

        when_time_is_$1("00:00");
        then_current_cue_is_$1(null);

        when_time_is_$1("09:59");
        then_current_cue_is_$1(null);

        when_time_is_$1("10:00");
        then_current_cue_is_$1(cue1);

        when_time_is_$1("10:59");
        then_current_cue_is_$1(cue1);

        when_time_is_$1("11:00");
        then_current_cue_is_$1(null);

        when_time_is_$1("12:00");
        then_current_cue_is_$1(cue2);

        when_time_is_$1("13:30");
        then_current_cue_is_$1(cue2);

        when_time_is_$1("14:00");
        then_current_cue_is_$1(null);

        when_date_is_$1("2016-03-04");
        then_there_are_$1_events_today(1);
        then_current_cue_is_$1(cue3);

        when_remove_event_$1(id3);
        then_event_$1_exists_$2(id3, false);
        then_there_are_$1_events_today(0);
        then_there_are_$1_events_between_$2_and_$3(2, "2016-03-01", "2016-03-08");
        then_current_cue_is_$1(null);

    }

}
