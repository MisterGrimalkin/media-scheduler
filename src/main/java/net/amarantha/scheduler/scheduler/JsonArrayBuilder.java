package net.amarantha.scheduler.scheduler;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.util.Collection;

public class JsonArrayBuilder {

    public static JsonArrayBuilder jsonArray() {
        return new JsonArrayBuilder();
    }

    private JSONArray jsonArray = new JSONArray();

    public <T> JSONArray from(Collection<T> ts, JsonArrayIterator<T> iter) {
        for ( T t : ts ) {
            JSONObject jsonObject = iter.process(t).get();
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }

    public JSONArray get() {
        return jsonArray;
    }

    public interface JsonArrayIterator<T> {
        JsonBuilder process(T t);
    }


}
