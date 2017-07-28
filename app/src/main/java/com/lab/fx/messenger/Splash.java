package com.lab.fx.messenger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.lab.fx.messenger.service.MyServices;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Splash extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Splash";
    private static int  REQUEST_PERMISSION = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        if (!MyServices.isPermissionsGranted(Splash.this)) {
            MyServices.requestPermissions(Splash.this, REQUEST_PERMISSION);
        }
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] params) {
                MyServices.create(Splash.this);
                try { Thread.sleep(3000); } catch (Exception e) {}
                int sleep = 0;
                while (!MyServices.isReady()) {
                    try {
                        sleep += 100;
                        publishProgress(sleep);
                        Thread.sleep(sleep > 1000 ? 1000 : sleep);
                    } catch (Exception e) {
                        Log.e(TAG, "", e);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (MyServices.isLogin()) {
                    startActivity(new Intent(Splash.this, MainActivity.class));
                    finish();
                }
                else {
                    if (MyServices.isReady()) {
                        findViewById(R.id.connecting).setVisibility(View.GONE);
                        findViewById(R.id.layout_login).setVisibility(View.VISIBLE);
                        findViewById(R.id.register).setVisibility(View.VISIBLE);

                        findViewById(R.id.signin).setOnClickListener(Splash.this);
                        findViewById(R.id.register).setOnClickListener(Splash.this);
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register :
                startActivity(new Intent(Splash.this, Register.class));
                finish();
                break;
            case R.id.signin :
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
