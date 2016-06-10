package net.amarantha.scheduler.scheduler;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JsonBuilder {

    public static JsonBuilder json() {
        return new JsonBuilder();
    }

    private JSONObject jsonObject = new JSONObject();

    public JsonBuilder put(String key, Object value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JSONObject get() {
        return jsonObject;
    }

    public String toJsonString() {
        return jsonObject.toString();
    }

}
