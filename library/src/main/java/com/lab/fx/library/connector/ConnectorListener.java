package com.lab.fx.library.connector;

/**
 * Created by febri on 15/07/17.
 */

public interface ConnectorListener {

    public void onStart(Connector p_connector);
    public void onReceiveData(Connector p_connector, String p_data);
    public void onConnectionStateChanged(Connector p_connector, boolean p_connected);
    public void onStop(Connector p_connector);

}
