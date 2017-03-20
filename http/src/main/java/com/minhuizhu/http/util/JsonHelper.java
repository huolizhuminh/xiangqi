package com.minhuizhu.http.util;

import org.json.JSONObject;

/**
 * Created by rex.wei on 2016/8/31.
 */
public class JsonHelper {

    public static Builder getBuilder() {
        return new Builder();
    }

    public static JSONObject create(String json) {
        try {
            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder {
        JSONObject json = new JSONObject();

        public Builder put(String name, Object value) {
            try {
                json.put(name, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }

        public String getJson() {
            return json.toString();
        }

        public JSONObject getJSONObject() {
            return json;
        }
    }
}
