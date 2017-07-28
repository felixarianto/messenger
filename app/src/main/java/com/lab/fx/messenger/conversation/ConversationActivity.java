package com.lab.fx.messenger.conversation;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lab.fx.library.R;
import com.lab.fx.library.conversation.ConversationAdapter;
import com.lab.fx.messenger.service.MyServices;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Compose Message");
        setContentView(R.layout.conversation_activity);
        createView();
        fill(getIntent());


    }

    private ConversationAdapter mAdapter;
    private void createView() {
        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(mAdapter = new ConversationAdapter(this));
        findViewById(R.id.send).setOnClickListener(this);
    }

    private void fill(Intent p_intent) {
        String to = p_intent.getStringExtra("to");
        if (to != null && isNumeric(to)) {
            ((EditText)findViewById(R.id.to)).setText(to);
        }
        String text = p_intent.getStringExtra("text");
        if (text != null ) {
            ((EditText)findViewById(R.id.input)).setText(text);
        }
    }

    public final static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send :
                String to   = ((EditText)findViewById(R.id.to)).getText().toString();
                String text = ((EditText)findViewById(R.id.input)).getText().toString();
                if (!to.isEmpty() && !text.isEmpty()) {
                    MyServices.sms(to, System.currentTimeMillis() + "", text);
                    mAdapter.add(text, new java.text.SimpleDateFormat("hh:mm").format(System.currentTimeMillis()));
                    ((EditText)findViewById(R.id.input)).setText("");
                }
                break;
        }
    }
}
