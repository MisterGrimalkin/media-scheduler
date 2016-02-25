package net.amarantha.mediascheduler.entity;

public class CueList {

    private int number;
    private String name;

    public CueList(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

}
