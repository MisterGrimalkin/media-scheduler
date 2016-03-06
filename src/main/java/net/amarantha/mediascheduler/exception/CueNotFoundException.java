package net.amarantha.mediascheduler.exception;

public class CueNotFoundException extends SchedulerException {

    public CueNotFoundException() {
        super("Cue not found");
    }

    public CueNotFoundException(String message) {
        super(message);
    }

    public CueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
