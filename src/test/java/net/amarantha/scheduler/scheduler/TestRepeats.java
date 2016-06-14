package net.amarantha.scheduler.scheduler;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestCase;
import net.amarantha.scheduler.TestModule;
import org.junit.runner.RunWith;

import static java.time.DayOfWeek.*;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestRepeats extends TestCase {

    @Story
    public void test() {

        when_start_scheduler();
        makeTestCues();
        when_setup_cues();

        String date = "2016-03-14";         // a Monday

        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue1, date, "10:00", "12:00", MONDAY, TUESDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue2, date, "12:00", "14:00", TUESDAY, WEDNESDAY);
        when_add_priority_$1_event_$2_on_$3_from_$4_to_$5(1, cue3, date, "14:00", "18:00", WEDNESDAY, FRIDAY, SATURDAY);

        then_there_are_$1_events_between_$2_and_$3(0, "2016-03-07", "2016-03-13");
        then_there_are_$1_events_between_$2_and_$3(7, "2016-03-14", "2016-03-20");
        then_there_are_$1_events_between_$2_and_$3(7, "2016-03-21", "2016-03-27");

        when_date_is_$1("2016-03-21");
        then_there_are_$1_events_today(1);
        when_time_is_$1("09:00");
        then_current_cue_is_$1(null);
        when_time_is_$1("11:00");
        then_current_cue_is_$1(cue1);

        when_date_is_$1("2016-03-29");
        then_there_are_$1_events_today(2);
        when_time_is_$1("11:00");
        then_current_cue_is_$1(cue1);
        when_time_is_$1("13:00");
        then_current_cue_is_$1(cue2);

        when_date_is_$1("2016-03-30");
        then_there_are_$1_events_today(2);
        when_time_is_$1("13:00");
        then_current_cue_is_$1(cue2);
        when_time_is_$1("14:00");
        then_current_cue_is_$1(cue3);

        when_date_is_$1("2016-03-31");
        then_there_are_$1_events_today(0);

    }

}
