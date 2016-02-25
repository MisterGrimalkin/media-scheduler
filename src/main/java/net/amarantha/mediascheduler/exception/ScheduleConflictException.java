package net.amarantha.mediascheduler.exception;

public class ScheduleConflictException extends SchedulerException {

    public ScheduleConflictException() {
    }

    public ScheduleConflictException(String message) {
        super(message);
    }

    public ScheduleConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}
