package com.lab.fx.library.conversation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lab.fx.library.R;

public class ConversationActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);
        createList (R.id.list);

    }


    private void createList(int p_resid) {
        RecyclerView list = (RecyclerView) findViewById(p_resid);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(new ConversationAdapter(this));
    }
}
