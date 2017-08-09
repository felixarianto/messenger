package com.lab.fx.library.service;

import android.util.Log;

import com.lab.fx.library.app.App;
import com.lab.fx.library.connector.Connector;
import com.lab.fx.library.connector.ConnectorListener;
import com.lab.fx.library.data.Message;

/**
 * Created by febri on 15/07/17.
 */

public class MyConnector extends Connector implements ConnectorListener {

    public MyConnector(String p_address, int p_port) {
        super("MyConnector", p_address, p_port);
    }

    @Override
    public void onStart(Connector p_connector) {
        Log.d(TAG, "START");
    }

    @Override
    public void onReceiveData(Connector p_connector, String p_data) {
        try {
            Log.d(TAG, "[T0] " + p_data);
            App.process(new Message(p_data));
        } catch (Exception e) {
            Log.e(getName(), "", e);
        }
    }

    @Override
    public void onConnectionStateChanged(Connector p_connector, boolean p_connected) {
        try {
            Log.d(TAG, (p_connected ? "CONNECTED" : "DISCONNECT"));
        } catch (Exception e) {
            Log.e(getName(), "", e);
        }
    }

    @Override
    public void onStop(Connector p_connector) {
        Log.d(TAG, "STOP");
    }
}
