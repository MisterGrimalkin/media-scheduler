package net.amarantha.mediascheduler.scheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Cue {

    private int id;
    private int number;
    private String name;

    @JsonCreator
    public Cue(@JsonProperty("id") int id, @JsonProperty("number") int number, @JsonProperty("name") String name) {
        this.id = id;
        this.number = number;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cue cue = (Cue) o;

        return id == cue.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return id + "-" + number + ":" + name;
    }
}
