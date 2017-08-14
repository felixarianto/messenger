package com.lab.fx.messenger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.lab.fx.library.app.App;
import com.lab.fx.library.app.AppUICallback;
import com.lab.fx.library.service.MyServices;
import com.lab.fx.messenger.flixgw.ListFragment;
import com.lab.fx.messenger.flixgw.ProfileFragment;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity implements AppUICallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
        App.setUICallback(this);
        if (!MyServices.isPermissionsGranted(this)) {
            MyServices.requestPermissions(this, 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        OneSignal.clearOneSignalNotifications();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.setUICallback(null);
    }

    private void createView() {
        setContentView(R.layout.activity_main);
        setNavigation();
    }

    private BottomNavigationView mNavigation;
    private void setNavigation() {
        mNavigation = (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, mCurrentFragment = new ProfileFragment())
                                .commit();
                        setTitle("Profil");
                        return true;
                    case R.id.pending:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content, mCurrentFragment = new ListFragment())
                                .commit();
                        return true;
                }
                return false;
            }

        });
        mNavigation.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction()
        .replace(R.id.content, mCurrentFragment = new ListFragment())
        .commit();
    }
    private Fragment mCurrentFragment;

    @Override
    public void incomingData(String p_code, Object p_data) {
        OneSignal.clearOneSignalNotifications();
        if (mCurrentFragment == null) {

        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
