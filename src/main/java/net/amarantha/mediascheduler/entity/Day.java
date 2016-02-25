package net.amarantha.mediascheduler.entity;

public enum Day {

    MONDAY(1)
    ;


    private final int number;

    Day(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
