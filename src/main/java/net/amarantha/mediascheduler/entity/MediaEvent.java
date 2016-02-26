package net.amarantha.mediascheduler.entity;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class MediaEvent implements Comparable<MediaEvent> {

    private long id;

    private CueList cueList;

    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private Set<DayOfWeek> repeatOn = new HashSet<>();

    public MediaEvent(long id, CueList cueList, String startDateStr, String startTimeStr, String endTimeStr, DayOfWeek... repeats) throws InvalidArgumentException {
        startTime = LocalTime.parse(startTimeStr);
        endTime  = LocalTime.parse(endTimeStr);
        if ( startTime.isAfter(endTime) ) {
            throw new InvalidArgumentException(new String[]{"End Time must be after Start Time"});
        }
        this.id = id;
        this.cueList = cueList;
        startDate = LocalDate.parse(startDateStr);
        repeatOn.addAll(Arrays.asList(repeats));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CueList getCueList() {
        return cueList;
    }

    public void setCueList(CueList cueList) {
        this.cueList = cueList;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Set<DayOfWeek> getRepeatOn() {
        return repeatOn;
    }

    public void setRepeatOn(Set<DayOfWeek> repeatOn) {
        this.repeatOn = repeatOn;
    }

    @Override
    public int compareTo(MediaEvent o) {
        if ( startDate.equals(o.getStartDate()) ) {
            return startTime.compareTo(o.getStartTime());
        } else {
            return startDate.compareTo(o.getStartDate());
        }
    }
}
