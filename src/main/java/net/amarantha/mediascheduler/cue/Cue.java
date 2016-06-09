package net.amarantha.mediascheduler.cue;

public abstract class Cue {

    private int id;
    private String name;

    //////////////
    // Abstract //
    //////////////

    public abstract void start();

    public abstract void stop();

    ///////////////////////
    // Getters & Setters //
    ///////////////////////

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[" + id + "-" + name + "]";
    }
}