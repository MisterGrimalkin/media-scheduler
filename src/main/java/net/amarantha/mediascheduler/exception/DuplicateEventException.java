package net.amarantha.mediascheduler.exception;

public class DuplicateEventException extends SchedulerException {

    public DuplicateEventException() {
    }

    public DuplicateEventException(String message) {
        super(message);
    }

    public DuplicateEventException(String message, Throwable cause) {
        super(message, cause);
    }
}
