package com.lab.fx.messenger;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.lab.fx.messenger.dummy.DummyContent;
import com.lab.fx.messenger.flixgw.PendingFragment;
import com.lab.fx.messenger.flixgw.ProfileFragment;
import com.lab.fx.messenger.flixgw.SentFragment;
import com.lab.fx.messenger.home.HomeFragment;
import com.lab.fx.messenger.notif.NotifFragment;
import com.lab.fx.messenger.person.PersonFragment;
import com.lab.fx.messenger.service.MyServices;

public class MainActivity extends AppCompatActivity implements
          PendingFragment.OnListFragmentInteractionListener
        , SentFragment.OnListFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
    }

    private void createView() {
        setContentView(R.layout.activity_main);
        setNavigation();
        setFragment();
    }

    private void setNavigation() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.content, new ProfileFragment())
                                .commit();
                        setTitle("Profil");
                        return true;
                    case R.id.pending:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.content, new PendingFragment())
                                .commit();
                        setTitle("Pending (11)");
                        return true;
                    case R.id.sent:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.content, new SentFragment())
                                .commit();
                        setTitle("Sent (7)");
                        return true;
                }
                return false;
            }

        });
    }

    private void setFragment() {
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

}
