package net.amarantha.scheduler.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.amarantha.scheduler.cue.Cue;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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

    JsonBuilder encodeCue(Cue cue);

    void encodeCuesToFile(String filename);

    Set<Cue> decodeCues(String json);

    Cue decodeCue(JSONObject cueObj) throws JSONException, ClassNotFoundException ;

    Set<Cue> decodeCuesFromFile(String filename);

    String encodeMediaEvent(MediaEvent event) throws JsonProcessingException;

    MediaEvent decodeMediaEvent(String json) throws IOException;

    void saveSchedules();
}
