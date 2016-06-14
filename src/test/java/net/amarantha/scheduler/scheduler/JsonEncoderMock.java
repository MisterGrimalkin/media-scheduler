package net.amarantha.scheduler.scheduler;

import com.google.inject.Singleton;
import net.amarantha.scheduler.cue.Cue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class JsonEncoderMock extends JsonEncoderImpl {

    private Map<String, String> mockFiles = new HashMap<>();

    @Override
    public void encodeAllSchedulesToFile(String filename) {
        mockFiles.put(filename, encodeAllSchedules());
    }

    @Override
    public Map<Integer, Schedule> decodeSchedulesFromFile(String filename) {

        return new HashMap<>();
    }

    @Override
    public void encodeCuesToFile(String filename) {
        String json = encodeCues();
        mockFiles.put(filename, json);
    }

    @Override
    public Set<Cue> decodeCuesFromFile(String filename) {
        String json = mockFiles.get(filename);
        return decodeCues(json);
    }

}
