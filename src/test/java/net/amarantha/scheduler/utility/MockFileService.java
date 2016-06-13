package net.amarantha.scheduler.utility;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class MockFileService implements FileService {

    private Map<String, String> mockFiles = new HashMap<>();

    @Override
    public String readFromFile(String filename) {
        return mockFiles.get(filename);
    }

    @Override
    public boolean writeToFile(String filename, String content) {
        mockFiles.put(filename, content);
        return true;
    }
}
