package net.amarantha.mediascheduler.exception;

public class SchedulerException extends Exception {

    public SchedulerException() {
    }

    public SchedulerException(String message) {
        super(message);
    }

    public SchedulerException(String message, Throwable cause) {
        super(message, cause);
    }
}
