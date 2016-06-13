package net.amarantha.scheduler.utility;

public interface FileService {
    String readFromFile(String filename);

    boolean writeToFile(String filename, String content);
}
