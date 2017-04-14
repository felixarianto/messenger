package com.lab.fx.messenger;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.lab.fx.messenger.dummy.DummyContent;
import com.lab.fx.messenger.home.HomeFragment;
import com.lab.fx.messenger.notif.NotifFragment;
import com.lab.fx.messenger.person.PersonFragment;
import com.lab.fx.messenger.service.MyServices;

public class MainActivity extends AppCompatActivity implements PersonFragment.OnListFragmentInteractionListener
        , HomeFragment.OnListFragmentInteractionListener
        , NotifFragment.OnListFragmentInteractionListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMax(1000);
        new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                MyServices.create(MainActivity.this);
                int sleep = 0;
                while (!MyServices.isReady()) {
                    try {
                        sleep += 100;
                        publishProgress(sleep);
                        Thread.sleep(sleep > 1000 ? 1000 : sleep);
                    } catch (Exception e) {
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if (MyServices.isReady()) {
                    progress.cancel();
                    createView();
                }
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                progress.setProgress((int) values[0]);
            }
        }.execute();
        progress.show();
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
                    case R.id.navigation_friend:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.content, new PersonFragment())
                                .commit();
                        return true;
                    case R.id.navigation_chat:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.content, new HomeFragment())
                                .commit();
                        return true;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                .replace(R.id.content, new NotifFragment())
                                .commit();
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
