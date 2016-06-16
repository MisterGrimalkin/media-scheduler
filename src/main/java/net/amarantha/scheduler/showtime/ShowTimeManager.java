package net.amarantha.scheduler.showtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.scheduler.JsonEncoderImpl;
import net.amarantha.scheduler.utility.FileService;
import net.amarantha.scheduler.utility.Now;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static net.amarantha.scheduler.scheduler.JsonEncoderImpl.createMapper;

@Singleton
public class ShowTimeManager {

    @Inject private Now now;
    @Inject private FileService files;

    private List<ShowTime> showTimes = new LinkedList<>();
    private int futureCount = 3;

    private static int nextId = 1;

    public void addShow(ShowTime showTime) {
        if ( showTime.getId()<=0 ) {
            showTime.setId(nextId++);
        } else {
            nextId = Math.max(showTime.getId()+1, nextId);
        }
        showTimes.add(showTime);
        saveShows();
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

    public ShowTime getShowTime(int id) {
        for ( ShowTime showTime : showTimes ) {
            if ( showTime.getId()==id ) {
                return showTime;
            }
        }
        return null;
    }

    public String encodeShow(int id) {
        ShowTime showTime = getShowTime(id);
        if ( showTime != null ) {
            try {
                return createMapper().writeValueAsString(showTime);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String encodeShows() {
        Collections.sort(showTimes);
        try {
            return createMapper().writeValueAsString(showTimes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveShows() {
        files.writeToFile("show-times.json", encodeShows());
    }

    public void loadShows() {
        showTimes = decodeShows(files.readFromFile("show-times.json"));
    }

    public List<ShowTime> decodeShows(String json) {
        try {
            List<ShowTime> shows = createMapper().readValue(json, new TypeReference<List<ShowTime>>(){});
            if ( shows!=null ) {
                return shows;
            }
        } catch (Exception e) {
            e.printStackTrace();
            saveShows();
        }
        return new ArrayList<>();
    }

    public ShowTime decodeShow(String json) {
        try {
            return createMapper().readValue(json, new TypeReference<ShowTime>(){});
        } catch (IOException e) {
            e.printStackTrace();
            saveShows();
        }
        return null;
    }



    public void setFutureCount(int futureCount) {
        this.futureCount = futureCount;
    }

    public boolean deleteShow(int id) {
        ShowTime showTime = getShowTime(id);
        boolean removed = false;
        if ( showTime!=null ) {
            removed = showTimes.remove(showTime);
            saveShows();
        }
        return removed;
    }
}
