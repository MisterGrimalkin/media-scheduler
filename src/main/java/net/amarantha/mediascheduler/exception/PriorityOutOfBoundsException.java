package net.amarantha.mediascheduler.exception;

public class PriorityOutOfBoundsException extends SchedulerException {

    public PriorityOutOfBoundsException() {
    }

    public PriorityOutOfBoundsException(String message) {
        super(message);
    }

    public PriorityOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

}
