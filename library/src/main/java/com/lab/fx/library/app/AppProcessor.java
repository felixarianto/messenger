package com.lab.fx.library.app;


import com.lab.fx.library.data.Message;

import org.json.JSONObject;

/**
 * Created by febri on 23/05/17.
 */

public interface AppProcessor {

    public String[] getId();
    public void process(String p_code, Message p_message);

}
