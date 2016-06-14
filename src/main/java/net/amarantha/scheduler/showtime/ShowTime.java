package net.amarantha.scheduler.showtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    @JsonCreator
    public ShowTime() {
    }

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

    @JsonProperty("date")
    public String getDateStr() { return date.toString(); }

    @JsonIgnore
    public LocalTime getStartTime() {
        return startTime;
    }

    @JsonProperty("startTime")
    public String getStartTimeStr() { return startTime.toString(); }

    @JsonIgnore
    public LocalTime getEndTime() {
        return endTime;
    }

    @JsonProperty("endTime")
    public String getEndTimeStr() { return endTime.toString(); }

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

    @JsonProperty("date")
    public ShowTime setDateStr(String date) {
        return setDate(LocalDate.parse(date));
    }

    @JsonIgnore
    public ShowTime setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    @JsonProperty("startTime")
    public ShowTime setStartTimeStr(String startTime) {
        return setStartTime(LocalTime.parse(startTime));
    }

    @JsonIgnore
    public ShowTime setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    @JsonProperty("endTime")
    public ShowTime setEndTimeStr(String endTime) {
        return setEndTime(LocalTime.parse(endTime));
    }

    @JsonIgnore
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

    @JsonIgnore
    public String getMessage() {
        return getMessage(false);
    }

    @JsonIgnore
    public String getMessage(boolean isNow) {
        String result = "";
        result += isNow ? "SHOWING" : date.format(DateTimeFormatter.ofPattern("EEEE"));
        result += ";;" + (isNow ? "NOW" : startTime.format(DateTimeFormatter.ofPattern("h:mma")).toLowerCase());
        result += ";;" + title;
        result += ";;" + description1;
        result += ";;" + description2;
        System.out.println(result);
        return result;
    }


}
