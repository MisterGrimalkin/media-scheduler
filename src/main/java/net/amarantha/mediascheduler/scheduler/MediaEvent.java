package net.amarantha.mediascheduler.scheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MediaEvent implements Comparable<MediaEvent> {

    static int nextId = 0;

    private long id;

    private int cueId;

    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private Set<DayOfWeek> repeatOn = new HashSet<>();

    @Inject private Scheduler scheduler;

    @JsonCreator
    public MediaEvent(
            @JsonProperty("cueId") int cueId,
            @JsonProperty("startDate") String startDateStr,
            @JsonProperty("startTime") String startTimeStr,
            @JsonProperty("endTime") String endTimeStr,
            @JsonProperty("repeatOn") DayOfWeek... repeats) throws IllegalArgumentException {
        this(nextId, cueId, startDateStr, startTimeStr, endTimeStr, repeats);
        nextId++;
    }

    public MediaEvent(int id, int cueId, String startDateStr, String startTimeStr, String endTimeStr, DayOfWeek... repeats) throws IllegalArgumentException {
        startTime = LocalTime.parse(startTimeStr);
        endTime  = LocalTime.parse(endTimeStr);
        if ( startTime.isAfter(endTime) ) {
            throw new IllegalArgumentException("End Time must be after Start Time");
        }
        this.id = id;
        this.cueId = cueId;
        startDate = LocalDate.parse(startDateStr);
        repeatOn.addAll(Arrays.asList(repeats));
    }


    /////////////
    // Getters //
    /////////////

    public long getId() {
        return id;
    }

    public int getCueId() {
        return cueId;
    }

    @JsonIgnore
    public LocalDate getStartDate() {
        return startDate;
    }

    @JsonIgnore
    public LocalTime getStartTime() {
        return startTime;
    }

    @JsonIgnore
    public LocalTime getEndTime() {
        return endTime;
    }

    public Set<DayOfWeek> getRepeatOn() {
        return repeatOn;
    }

    public boolean isRepeating() {
        return !repeatOn.isEmpty();
    }

    @JsonProperty("startDate")
    String getStartDateString() {
        return startDate.toString();
    }

    @JsonProperty("startTime")
    String getStartTimeString() {
        return startTime.toString();
    }

    @JsonProperty("endTime")
    String getEndTimeString() {
        return endTime.toString();
    }


    /////////////
    // Setters //
    /////////////

    public void setId(long id) {
        this.id = id;
    }

    public void setCueList(int cueListId) {
        this.cueId = cueListId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setRepeatOn(Set<DayOfWeek> repeatOn) {
        this.repeatOn = repeatOn;
    }


    ////////////////
    // Comparison //
    ////////////////

    @Override
    public int compareTo(MediaEvent o) {
        if ( startDate.equals(o.getStartDate()) ) {
            return startTime.compareTo(o.getStartTime());
        } else {
            return startDate.compareTo(o.getStartDate());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaEvent event = (MediaEvent) o;

        if (id != event.id) return false;
        if (startDate != null ? !startDate.equals(event.startDate) : event.startDate != null) return false;
        if (startTime != null ? !startTime.equals(event.startTime) : event.startTime != null) return false;
        if (endTime != null ? !endTime.equals(event.endTime) : event.endTime != null) return false;
        return repeatOn != null ? repeatOn.equals(event.repeatOn) : event.repeatOn == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (repeatOn != null ? repeatOn.hashCode() : 0);
        return result;
    }

}
