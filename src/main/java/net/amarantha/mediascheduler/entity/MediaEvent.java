package net.amarantha.mediascheduler.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class MediaEvent {

    private long id;

    private CueList cueList;

    private LocalTime startTime;
    private LocalTime endTime;

    private Date start;
    private Date end;
    private List<DayOfWeek> repeatOn;




}
