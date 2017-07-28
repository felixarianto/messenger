package com.lab.fx.library.connector;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by febri on 15/07/17.
 */

public class Connector {

    public static final String TAG = "Connector";

    private String  mName;
    private String  mAddress;
    private int     mPort;
    private int     mTimeout;
    private java.net.Socket  mSocket;
    private Thread  mThread;
    private boolean mRunning;
    private boolean mConnected;
    private OutputStream mWriter;
    private InputStream  mReader;
    private int mEOL = 0x0A;
    private Log mLog;
    private ConnectorListener mConnectorListener;
    private int mConnectInterval;
    private Thread mHbThread;
    private String mHbString;
    private int mHbInterval;
    private boolean mHbEnabled;
    private String mCharset;

    public static final int DEFAULT_SOCKET_TIMEOUT = 0;
    public static final int DEFAULT_HB_INTERVAL = 1000 * 60;
    public static final int DEFAULT_CONNECT_INTERVAL = 1000 * 1;
    public static final String DEFAULT_HB_STRING = "H";

    private final Object WRITE_SYNC = new Object();
    /*
     *
     */
    private Connector() {
        mName = "";
        mAddress = "";
        mPort = 0;
        mTimeout = DEFAULT_SOCKET_TIMEOUT;
        mSocket = null;
        mThread = null;
        mRunning = false;
        mConnected = false;
        mWriter = null;
        mReader = null;
        mLog = null;
        mConnectorListener = null;
        mConnectInterval = DEFAULT_CONNECT_INTERVAL;
        mHbThread = null;
        mHbString = DEFAULT_HB_STRING;
        mHbInterval = DEFAULT_HB_INTERVAL;
        mHbEnabled = false;
        mCharset = null;
    }
    public Connector(String p_name, String p_address, int p_port) {
        this ();
        mName = p_name == null ? "" : p_name;
        mAddress = p_address == null ? "" : p_address;
        mPort = p_port < 0 ? 0 : p_port;
    }
    public final String getCharset() {
        return mCharset;
    }
    public final String getName() {
        return mName;
    }
    public final String getAddress() {
        return mAddress;
    }
    public final int getPort() {
        return mPort;
    }
    /*
     *
     */
    public void setHbEnabled(boolean p_enabled) {
        mHbEnabled = p_enabled;
    }
    public final boolean isHbEnabled() {
        return mHbEnabled;
    }
    public void setHbInterval(int p_interval) {
        mHbInterval = p_interval < 0 ? 0 : p_interval;
    }
    public void setHbString(String p_string) {
        mHbString = p_string == null ? "" : p_string;
    }
    public final String getHbString() {
        return mHbString;
    }
    public final int getHbInterval() {
        return mHbInterval;
    }
    private Thread getHbThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (mRunning) {
                    try {
                        Thread.sleep(mHbInterval);
                    } catch (InterruptedException ie) {
                        break;
                    }
                    try {
                        if (mConnected && mHbEnabled) {
                            write(mHbString);
                        }
                    } catch (Error | Exception e) {
                        e(e);
                    }
                }
            }
        });
    }
    /*
     *
     */
    public boolean write(String p_data, boolean p_eol) {
        boolean write = false;
        if (p_data != null && mWriter != null) {
            try {
                byte[] b = mCharset == null ? p_data.getBytes() : p_data.getBytes(mCharset);
                synchronized (WRITE_SYNC) {
                    mWriter.write(b);
                    if (p_eol) {
                        mWriter.write(mEOL);
                    }
                    mWriter.flush();
                }
                write = true;
            } catch (IOException e) {
                e(e);
                disconnect();
            } catch (Error | Exception e) {
                e(e);
            }
        }
        return write;
    }

    public boolean write(String p_data) {
        return write(p_data, false);
    }

    public boolean writeLn(String p_data) {
        return write(p_data, true);
    }
    /*
     *
     */
    public void setConnectorListener(ConnectorListener p_listener) {
        mConnectorListener = p_listener;
    }
    public final ConnectorListener getConnectorListener() {
        return mConnectorListener;
    }
    public void setSoTimeout(int p_timeout) {
        mTimeout = p_timeout;
    }
    public final int getSoTimeout() {
        return mTimeout;
    }
    public void setEOL(int p_eol) {
        mEOL = p_eol;
    }
    public final int getEOL() {
        return mEOL;
    }
    public void setLog(Log p_log) {
        mLog = p_log;
    }
    public final Log getLog() {
        return mLog;
    }
    public void setConnectInterval(int p_interval) {
        mConnectInterval = p_interval < 0 ? 0 : p_interval;
    }
    public final int getConnectInterval() {
        return mConnectInterval;
    }
    /*
     *
     */
    public final boolean isRunning() {
        return mRunning;
    }
    public final boolean isConnected() {
        return mConnected;
    }
    private void disconnect() {
        mConnected = false;
        if (mSocket != null) {
            try { mSocket.close(); } catch (IOException ioe) {}
        }
        mSocket = null;
        mReader = null;
        mWriter = null;
    }
    public final synchronized void stop() {
        mRunning = false;
        try {
            if (mHbThread != null) {
                mHbThread.interrupt();
            }
        } catch (Error | Exception e) {
            e(e);
        }
        disconnect();
    }
    public final synchronized void start() {
        if (mRunning) {

        }
        else {
            mRunning = true;
            mThread = getSocketThread();
            mThread.start();
            mHbThread = getHbThread();
            mHbThread.start();
        }
    }
    private void onReading() {
        while (mRunning && mConnected) {
            try {
                int read;
                ByteArrayOutputStream data = new ByteArrayOutputStream();
                boolean reading = true;
                while (reading) {
                    read = mReader.read();
                    if (read == -1) {
                        reading = false;
                        disconnect();
                    }
                    else if (read == mEOL) {
                        reading = false;
                    }
                    else {
                        data.write(read);
                    }
                }
                if (mConnectorListener != null) {
                    try {
                        mConnectorListener.onReceiveData(Connector.this, mCharset == null ? data.toString() : data.toString(mCharset));
                    } catch (Error | Exception e) {
                        e(e);
                    }
                }
            } catch (Error | Exception e) {
                e(e);
                disconnect();
            }
        }
        if (mConnectorListener != null) {
            try {
                mConnectorListener.onConnectionStateChanged(this, false);
            } catch (Error | Exception e) {
                e(e);
            }
        }
    }
    private void onConnect() {
        while (mRunning && !mConnected) {
            try {
                mSocket = new java.net.Socket(mAddress, mPort);
                mSocket.setSoTimeout(mTimeout);
                mWriter = mSocket.getOutputStream();
                mReader = mSocket.getInputStream();
                mConnected = true;
                if (mConnectorListener != null) {
                    mConnectorListener.onConnectionStateChanged(this, true);
                }
            } catch (Error | Exception e) {
                e(e);
            }
            if (!mConnected) {
                mSocket = null;
                mWriter = null;
                mReader = null;
                try {
                    Thread.sleep(mConnectInterval);
                } catch (InterruptedException ie) {
                }
            }
        }
    }
    private void onStart() {
        if (mConnectorListener != null) {
            try {
                mConnectorListener.onStart(this);
            } catch (Exception | Error e) {
                e(e);
            }
        }
    }
    private void onStop() {
        if (mConnectorListener != null) {
            try {
                mConnectorListener.onStop(this);
            } catch (Exception | Error e) {
                e(e);
            }
        }
    }
    public Thread getSocketThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                onStart();
                while (mRunning) {
                    onConnect();
                    onReading();
                }
                onStop();
            }
        });
    }
    /*
     *
     */
    private void e(Throwable p_t) {
        if (mLog != null) {
            mLog.e(TAG, mName, p_t);
        }
    }

}
