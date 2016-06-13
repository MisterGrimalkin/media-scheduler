package net.amarantha.scheduler.showtime;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.utility.Now;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class ShowTimeManager {

    @Inject private Now now;

    private List<ShowTime> showTimes = new LinkedList<>();
    private int futureCount = 1;

    public void addShow(ShowTime showTime) {
        showTimes.add(showTime);
    }

    public List<ShowTime> getShows() {
        return showTimes;
    }

    public ShowTime getCurrentShow() {
        for ( ShowTime showTime : showTimes ) {
            LocalDate date = now.date();
            LocalTime time = now.time();
            if ( showTime.getDate().equals(date)
                    && (showTime.getStartTime().equals(time) || showTime.getStartTime().isBefore(time))
                    && (showTime.getEndTime().isAfter(time))
            ) {
                return showTime;
            }
        }
        return null;
    }

    public List<ShowTime> getFutureShows() {
        Collections.sort(showTimes);
        List<ShowTime> futureShows = new LinkedList<>();
        for ( ShowTime showTime : showTimes ) {
            if ( now.date().equals(showTime.getDate()) ) {
                if ( now.time().isBefore(showTime.getStartTime()) ) {
                    if ( futureShows.size()<futureCount
                            || showTime.isAlwaysShowOnDay()
                            || showTime.isAlwaysShow() ) {
                        futureShows.add(showTime);
                    }
                }
            } else if ( now.date().isBefore(showTime.getDate()) ) {
                if ( futureShows.size()<futureCount || showTime.isAlwaysShow() ) {
                    futureShows.add(showTime);
                }
            }
        }

        return futureShows;
    }


    public void setFutureCount(int futureCount) {
        this.futureCount = futureCount;
    }
}
