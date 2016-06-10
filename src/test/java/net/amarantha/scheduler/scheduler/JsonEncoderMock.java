package net.amarantha.scheduler.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.amarantha.scheduler.cue.Cue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class JsonEncoderMock implements JsonEncoder {

    @Override
    public String encodeAllSchedules() {
        return null;
    }

    @Override
    public void encodeAllSchedulesToFile(String filename) {

    }

    @Override
    public Map<Integer, Schedule> decodeSchedulesFromFile(String filename) {
        return new HashMap<>();
    }

    @Override
    public String encodeSchedule(int priority, LocalDate date) {
        return null;
    }

    @Override
    public String encodeCues() {
        return null;
    }

    @Override
    public void encodeCuesToFile(String filename) {

    }

    @Override
    public Cue decodeCue(String json) {
        return null;
    }

    @Override
    public Set<Cue> decodeCuesFromFile(String filename) {
        return new HashSet<Cue>();
    }

    @Override
    public String encodeMediaEvent(MediaEvent event) throws JsonProcessingException {
        return null;
    }

    @Override
    public MediaEvent decodeMediaEvent(String json) throws IOException {
        return null;
    }

    @Override
    public void saveSchedules() {

    }
}
