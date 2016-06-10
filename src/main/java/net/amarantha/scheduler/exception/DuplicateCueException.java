package net.amarantha.scheduler.exception;

public class DuplicateCueException extends SchedulerException {

    public DuplicateCueException() {
        super("Cue already exists");
    }

    public DuplicateCueException(String message) {
        super(message);
    }

    public DuplicateCueException(String message, Throwable cause) {
        super(message, cause);
    }
}
