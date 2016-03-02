package net.amarantha.mediascheduler.exception;

public class DuplicateCueException extends SchedulerException {

    public DuplicateCueException() {
        super("Duplicate Cue ID");
    }

    public DuplicateCueException(String message) {
        super(message);
    }

    public DuplicateCueException(String message, Throwable cause) {
        super(message, cause);
    }
}
