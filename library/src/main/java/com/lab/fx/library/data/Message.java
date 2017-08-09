package com.lab.fx.library.data;

import com.lab.fx.library.contact.Mine;

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

    @Override
    public double getDouble(String name) {
        if (!isNull(name)) {
            try {
                return super.getDouble(name);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public double getDouble(String p_key, double p_default) throws JSONException {
        try {
            if (!isNull(p_key)) {
                return super.getDouble(p_key);
            }
        } catch (Exception e) {
        }
        return p_default;
    }

    @Override
    public String getString(String name) {
        if (!isNull(name)) {
            try {
                return super.getString(name);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public String getString(String p_key, String p_default) {
        try {
            if (!isNull(p_key)) {
                return super.getString(p_key);
            }
        } catch (Exception e) {
        }
        return p_default;
    }

    @Override
    public int getInt(String name) {
        if (!isNull(name)) {
            try {
                return super.getInt(name);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public int getInt(String p_key, int p_default) {
        try {
            if (!isNull(p_key)) {
                return super.getInt(p_key);
            }
        } catch (Exception e) {
        }
        return p_default;
    }

    @Override
    public long getLong(String name) {
        if (!isNull(name)) {
            try {
                return super.getLong(name);
            } catch (Exception e) {
            }
        }
        return 0;
    }

    public long getLong(String p_key, long p_default) {
        try {
            if (!isNull(p_key)) {
                return super.getLong(p_key);
            }
        } catch (Exception e) {
        }
        return p_default;
    }

    private static long mId = System.currentTimeMillis();

    public static synchronized String generateId() {
        return Mine.get().pin + (mId++);
    }

}
