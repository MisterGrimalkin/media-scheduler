package net.amarantha.scheduler.showtime;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShowTime implements Comparable<ShowTime> {

    private int id;

    private String title;
    private String description1;
    private String description2;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private boolean alwaysShowOnDay;
    private boolean alwaysShow;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription1() {
        return description1;
    }

    public String getDescription2() {
        return description2;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isAlwaysShowOnDay() {
        return alwaysShowOnDay;
    }

    public boolean isAlwaysShow() {
        return alwaysShow;
    }

    ////////////
    // Setter //
    ////////////


    public ShowTime setId(int id) {
        this.id = id;
        return this;
    }

    public ShowTime setTitle(String title) {
        this.title = title;
        return this;
    }

    public ShowTime setDescription1(String description1) {
        this.description1 = description1;
        return this;
    }

    public ShowTime setDescription2(String description2) {
        this.description2 = description2;
        return this;
    }

    public ShowTime setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public ShowTime setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public ShowTime setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public ShowTime setAlwaysShowOnDay(boolean alwaysShowOnDay) {
        this.alwaysShowOnDay = alwaysShowOnDay;
        return this;
    }

    public ShowTime setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
        return this;
    }

    @Override
    public int compareTo(ShowTime o) {
        if ( o!=null ) {
            if ( getDate().equals(o.getDate()) ) {
                return getStartTime().compareTo(o.getStartTime());
            } else {
                return getDate().compareTo(o.getDate());
            }
        }
        return 0;
    }
}
