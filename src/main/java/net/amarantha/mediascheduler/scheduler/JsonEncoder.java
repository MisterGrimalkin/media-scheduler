package net.amarantha.mediascheduler.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.amarantha.mediascheduler.cue.Cue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface JsonEncoder {

    String encodeAllSchedules();

    void encodeAllSchedulesToFile(String filename);

    Map<Integer, Schedule> decodeSchedulesFromFile(String filename);

    String encodeSchedule(int priority, LocalDate date);

    String encodeCues();

    void encodeCuesToFile(String filename);

    Cue decodeCue(String json);

    Set<Cue> decodeCuesFromFile(String filename);

    String encodeMediaEvent(MediaEvent event) throws JsonProcessingException;

    MediaEvent decodeMediaEvent(String json) throws IOException;

    void saveSchedules();
}
