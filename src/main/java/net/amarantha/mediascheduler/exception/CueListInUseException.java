package net.amarantha.mediascheduler.exception;

public class CueListInUseException extends SchedulerException {

    public CueListInUseException() {
    }

    public CueListInUseException(String message) {
        super(message);
    }

    public CueListInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
