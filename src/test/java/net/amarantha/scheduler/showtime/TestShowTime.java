package net.amarantha.scheduler.showtime;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.scheduler.TestModule;
import net.amarantha.scheduler.utility.Now;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(StoryRunner.class) @Modules(TestModule.class)
public class TestShowTime {

    @Inject private Now now;

    @Inject private ShowTimeManager manager;

    @Story
    public void test() {

        then_there_are_$1_shows(0);

        ShowTime st1 = given_show_$1_on_$2_at_$3(1, "2016-06-13", "12:00", "14:00", false, false);
        ShowTime st2 = given_show_$1_on_$2_at_$3(2, "2016-06-13", "15:00", "16:00", false, false);
        ShowTime st3 = given_show_$1_on_$2_at_$3(3, "2016-06-13", "16:00", "18:00", false, false);
        ShowTime st4 = given_show_$1_on_$2_at_$3(4, "2016-06-14", "12:00", "14:00", false, false);
        ShowTime st5 = given_show_$1_on_$2_at_$3(5, "2016-06-14", "15:00", "16:00", false, false);
        ShowTime st6 = given_show_$1_on_$2_at_$3(6, "2016-06-14", "16:00", "18:00", true, false);
        ShowTime st7 = given_show_$1_on_$2_at_$3(7, "2016-06-15", "12:00", "14:00", false, true);
        ShowTime st8 = given_show_$1_on_$2_at_$3(8, "2016-06-15", "15:00", "16:00", false, false);
        ShowTime st9 = given_show_$1_on_$2_at_$3(9, "2016-06-15", "16:00", "18:00", false, false);

        when_add_show(st1);
        when_add_show(st2);
        when_add_show(st3);
        when_add_show(st4);
        when_add_show(st5);
        when_add_show(st6);
        when_add_show(st7);
        when_add_show(st8);
        when_add_show(st9);

        then_there_are_$1_shows(9);

        when_date_is_$1("2016-06-13");
        when_time_is_$1("10:00");
        then_current_show_is_$1(null);

        when_schedule_future_count_is_$1(1);
        then_future_shows_are_$1(st1.getId(), st7.getId());
        when_schedule_future_count_is_$1(6);
        then_future_shows_are_$1(st1.getId(), st2.getId(), st3.getId(), st4.getId(), st5.getId(), st6.getId(), st7.getId());
        when_schedule_future_count_is_$1(15);
        then_future_shows_are_$1(st1.getId(), st2.getId(), st3.getId(), st4.getId(), st5.getId(), st6.getId(), st7.getId(), st8.getId(), st9.getId());
        when_schedule_future_count_is_$1(2);
        then_future_shows_are_$1(st1.getId(), st2.getId(), st7.getId());

        when_time_is_$1("13:00");
        then_current_show_is_$1(st1.getId());
        then_future_shows_are_$1(st2.getId(), st3.getId(), st7.getId());

        when_time_is_$1("14:30");
        then_current_show_is_$1(null);
        then_future_shows_are_$1(st2.getId(), st3.getId(), st7.getId());

        when_time_is_$1("15:30");
        then_current_show_is_$1(st2.getId());
        then_future_shows_are_$1(st3.getId(), st4.getId(), st7.getId());

        when_time_is_$1("17:00");
        then_current_show_is_$1(st3.getId());
        then_future_shows_are_$1(st4.getId(), st5.getId(), st7.getId());

        when_time_is_$1("19:00");
        then_current_show_is_$1(null);
        then_future_shows_are_$1(st4.getId(), st5.getId(), st7.getId());

        when_date_is_$1("2016-06-14");
        when_time_is_$1("10:00");
        then_current_show_is_$1(null);
        then_future_shows_are_$1(st4.getId(), st5.getId(), st6.getId(), st7.getId());

        when_time_is_$1("13:00");
        then_current_show_is_$1(st4.getId());
        then_future_shows_are_$1(st5.getId(), st6.getId(), st7.getId());

        when_time_is_$1("14:30");
        then_current_show_is_$1(null);
        then_future_shows_are_$1(st5.getId(), st6.getId(), st7.getId());

        when_time_is_$1("15:30");
        then_current_show_is_$1(st5.getId());
        then_future_shows_are_$1(st6.getId(), st7.getId());

        when_time_is_$1("17:00");
        then_current_show_is_$1(st6.getId());
        then_future_shows_are_$1(st7.getId(), st8.getId());

        when_time_is_$1("19:00");
        then_current_show_is_$1(null);
        then_future_shows_are_$1(st7.getId(), st8.getId());

        when_date_is_$1("2016-06-15");
        when_time_is_$1("10:00");
        then_current_show_is_$1(null);
        then_future_shows_are_$1(st7.getId(), st8.getId());

        when_time_is_$1("12:00");
        then_current_show_is_$1(st7.getId());
        then_future_shows_are_$1(st8.getId(), st9.getId());

        when_time_is_$1("14:30");
        then_current_show_is_$1(null);
        then_future_shows_are_$1(st8.getId(), st9.getId());

        when_time_is_$1("15:59");
        then_current_show_is_$1(st8.getId());
        then_future_shows_are_$1(st9.getId());

        when_time_is_$1("17:00");
        then_current_show_is_$1(st9.getId());
        then_there_are_no_future_shows();

        when_time_is_$1("18:00");
        then_current_show_is_$1(null);
        then_there_are_no_future_shows();

    }

    ShowTime given_show_$1_on_$2_at_$3(int id, String date, String startTime, String endTime, boolean alwaysShowOnDay, boolean alwaysShow) {
        return new ShowTime()
                .setId(id)
                .setTitle("Test"+id)
                .setDate(LocalDate.parse(date))
                .setStartTime(LocalTime.parse(startTime))
                .setEndTime(LocalTime.parse(endTime))
                .setDescription1("Test"+id+"Desc1")
                .setDescription2("Test"+id+"Desc2")
                .setAlwaysShowOnDay(alwaysShowOnDay)
                .setAlwaysShow(alwaysShow);
    }

    void when_schedule_future_count_is_$1(int count) {
        manager.setFutureCount(count);
    }

    void when_date_is_$1(String date) {
        now.setDate(date);
    }

    void when_time_is_$1(String time) {
        now.setTime(time);
    }

    void then_there_are_no_future_shows() {
        assertEquals(0, manager.getFutureShows().size());
    }

    void then_future_shows_are_$1(int... ids) {
        List<ShowTime> futureShows = manager.getFutureShows();
        assertEquals(ids.length, futureShows.size());
        List<Integer> showIds = new ArrayList<>();
        for ( ShowTime show : futureShows ) {
            showIds.add(show.getId());
        }
        for ( int id : ids ) {
            assertTrue(showIds.contains(id));
        }
    }

    void then_current_show_is_$1(Integer id) {
        ShowTime currentShow = manager.getCurrentShow();
        if ( currentShow==null ) {
            assertNull(id);
        } else {
            assertNotNull(id);
            assertEquals(id.intValue(), currentShow.getId());
        }
    }

    void then_there_are_$1_shows(int count) {
        Assert.assertEquals(count, manager.getShows().size());
    }

    void when_add_show(ShowTime showTime) {
        manager.addShow(showTime);
    }

}
