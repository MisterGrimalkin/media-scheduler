package net.amarantha.scheduler.exception;

import net.amarantha.scheduler.scheduler.MediaEvent;

public class ScheduleConflictException extends SchedulerException {

    private MediaEvent conflictingEvent;

    public ScheduleConflictException(MediaEvent conflictingEvent) {
        super("Schedule Conflict");
        this.conflictingEvent = conflictingEvent;
    }



    public MediaEvent getConflictingEvent() {
        return conflictingEvent;
    }
}
