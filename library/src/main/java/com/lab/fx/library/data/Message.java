package com.lab.fx.library.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by febri on 15/07/17.
 */

public class Message extends JSONObject {

    public Message() {

    }
    public Message(String p_string) throws JSONException {
        super(p_string);
    }

    public double getDouble(String p_key, double p_default) throws JSONException {
        try {
            if (!isNull(p_key)) {
                return super.getDouble(p_key);
            }
        } catch (Exception e) {}
        return p_default;
    }

    public String getString(String p_key, String p_default) {
        try {
            if (!isNull(p_key)) {
                return super.getString(p_key);
            }
        } catch (Exception e) {}
        return p_default;
    }

    public int getInt(String p_key, int p_default) {
        try {
            if (!isNull(p_key)) {
                return super.getInt(p_key);
            }
        } catch (Exception e) {}
        return p_default;
    }

    public long getLong(String p_key, long p_default) {
        try {
            if (!isNull(p_key)) {
                return super.getLong(p_key);
            }
        } catch (Exception e) {}
        return p_default;
    }

}
