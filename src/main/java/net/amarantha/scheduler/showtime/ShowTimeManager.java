package net.amarantha.scheduler.showtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.http.HostManager;
import net.amarantha.scheduler.http.HttpService;
import net.amarantha.scheduler.scheduler.JsonEncoderImpl;
import net.amarantha.scheduler.utility.FileService;
import net.amarantha.scheduler.utility.Now;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static net.amarantha.scheduler.scheduler.JsonEncoderImpl.createMapper;

@Singleton
public class ShowTimeManager {

    @Inject private Now now;
    @Inject private FileService files;
    @Inject private HostManager hostManager;
    @Inject private HttpService http;

    private List<ShowTime> showTimes = new LinkedList<>();
    private int futureCount = 3;

    private static int nextId = 1;

    private Timer timer = new Timer();

    public void start() {
        loadShows();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if ( !paused ) {
                    fire();
                }
            }
        }, 5000, 5000);
        System.out.println("ShowTime Manager online");
        postMessage("{green}SERVER;;{green}ONLINE;;---;;---;;---");
    }

    public void stop() {
        timer.cancel();
        postMessage("{red}SERVER;;{red}OFFLINE;;---;;---;;---");
    }

    private String lastEntireMessage = "";

    private void fire() {
        System.out.println("Show Times -->");
        String entireMessage = "";
        ShowTime currentShow = getCurrentShow();
        List<ShowTime> showTimes = getFutureShows();
        List<String> messages = new LinkedList<>();
        if ( currentShow!=null ) {
            String msg = currentShow.getMessage(true);
            entireMessage += msg;
            messages.add(msg);
        }
        for ( ShowTime showTime : showTimes ) {
            String msg = showTime.getMessage();
            entireMessage += msg;
            messages.add(msg);
        }
        if ( !lastEntireMessage.equals(entireMessage) ) {
            List<String> hosts = hostManager.getHosts("events");
            if ( hosts!=null ) {
                for (String host : hosts) {
                    System.out.println("--> "+host);
                    http.post(host, "lightboard/scene/events/group/events/clear", null);
                    for (String message : messages) {
                        http.post(host, "lightboard/scene/events/group/events/add", message);
                    }
                    http.post(host, "lightboard/scene/reload", null);
                    lastEntireMessage = entireMessage;
                }
            }
            lastEntireMessage = entireMessage;
        }
    }

    public void postMessage(String message) {
        List<String> hosts = hostManager.getHosts("events");
        if ( hosts!=null ) {
            for (String host : hosts) {
                http.post(host, "lightboard/scene/events/group/events/clear", null);
                http.post(host, "lightboard/scene/events/group/events/add", message);
                http.post(host, "lightboard/scene/reload", null);
            }
        }
    }

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
        files.writeToFile("data/show-times.json", encodeShows());
    }

    public void loadShows() {
        showTimes = decodeShows(files.readFromFile("data/show-times.json"));
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

    private boolean paused = false;

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }
}
