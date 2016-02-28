package net.amarantha.mediascheduler.scheduler;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CueList {

    private int number;
    private String name;

    @JsonCreator
    public CueList(@JsonProperty("number") int number, @JsonProperty("name") String name) {
        this.number = number;
        this.name = name;
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

        CueList cueList = (CueList) o;

        if (number != cueList.number) return false;
        return name.equals(cueList.name);

    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return number + ":" + name;
    }
}
