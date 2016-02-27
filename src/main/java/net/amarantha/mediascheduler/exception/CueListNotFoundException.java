package net.amarantha.mediascheduler.exception;

public class CueListNotFoundException extends SchedulerException {

    public CueListNotFoundException() {
    }

    public CueListNotFoundException(String message) {
        super(message);
    }

    public CueListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
