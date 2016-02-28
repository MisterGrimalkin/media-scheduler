package net.amarantha.mediascheduler.exception;

import net.amarantha.mediascheduler.scheduler.MediaEvent;

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
