package net.amarantha.mediascheduler.exception;

public class CueInUseException extends SchedulerException {

    public CueInUseException() {
        super("Cue in use");
    }

    public CueInUseException(String message) {
        super(message);
    }

    public CueInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
