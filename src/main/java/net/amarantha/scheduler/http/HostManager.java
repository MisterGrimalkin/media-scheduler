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
        List<String> result = new ArrayList<>();
        if ( allHosts.containsKey(groupName) ) {
            result = allHosts.get(groupName);
        }
        return result;
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
        fileService.writeToFile("data/hosts.json", encodeHosts());
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
        System.out.println("Loading Hosts...");
        allHosts = decodeHosts(fileService.readFromFile("data/hosts.json"));
        for ( Map.Entry<String, List<String>> entry : allHosts.entrySet() ) {
            System.out.println(entry.getKey());
            for ( String h : entry.getValue() ) {
                System.out.println("\t"+h);
            }
        }
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
