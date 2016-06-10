package net.amarantha.scheduler.exception;

public class PriorityOutOfBoundsException extends SchedulerException {

    public PriorityOutOfBoundsException() {
        super("Priority out of bounds");
    }

    public PriorityOutOfBoundsException(String message) {
        super(message);
    }

    public PriorityOutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

}
