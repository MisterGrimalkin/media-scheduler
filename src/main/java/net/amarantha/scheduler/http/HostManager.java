package net.amarantha.scheduler.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.scheduler.scheduler.JsonEncoderImpl;
import net.amarantha.scheduler.utility.FileService;

import java.io.IOException;
import java.util.*;

@Singleton
public class HostManager {

    @Inject private FileService fileService;

    private Map<String, List<String>> allHosts = new HashMap<>();

    public List<String> getHosts(String groupName) {
        return allHosts.get(groupName);
    }

    public HostManager addHosts(String groupName, String... hosts) {
        List<String> hostsList = Arrays.asList(hosts);
        List<String> currentGroup = allHosts.get(groupName);
        if ( currentGroup==null ) {
            currentGroup = new ArrayList<>();
            allHosts.put(groupName, currentGroup);
        }
        currentGroup.addAll(hostsList);
        return this;
    }

    public void saveHosts() {
        fileService.writeToFile("hosts.json", encodeHosts());
    }

    public String encodeHosts() {
        try {
            return JsonEncoderImpl.createMapper().writeValueAsString(allHosts);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadHosts() {
        allHosts = decodeHosts(fileService.readFromFile("hosts.json"));
    }

    public Map<String, List<String>> decodeHosts(String json) {
        try {
            return JsonEncoderImpl.createMapper().readValue(json, new TypeReference<Map<String, List<String>>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
