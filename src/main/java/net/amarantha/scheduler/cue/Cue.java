package net.amarantha.scheduler.cue;

public abstract class Cue {

    private int id;
    private String name;
    private boolean selfStopping;

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

    public boolean isSelfStopping() {
        return selfStopping;
    }

    public void setSelfStopping(boolean selfStopping) {
        this.selfStopping = selfStopping;
    }

    @Override
    public String toString() {
        return "[" + id + "-" + name + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cue cue = (Cue) o;

        if (id != cue.id) return false;
        return name.equals(cue.name);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }
}