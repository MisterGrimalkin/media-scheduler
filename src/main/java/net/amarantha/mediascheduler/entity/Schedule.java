package net.amarantha.mediascheduler.entity;

import com.google.inject.Inject;
import net.amarantha.mediascheduler.utility.Now;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Schedule {

    @Inject
    private Now now;

    Map<DayOfWeek, Map<LocalDate, MediaEvent>> events = new HashMap<>();        // null key for repeats

    public Schedule() {

    }
}
