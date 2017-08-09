package com.lab.fx.messenger.conversation;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.lab.fx.library.R;
import com.lab.fx.library.app.App;
import com.lab.fx.library.app.AppUICallback;
import com.lab.fx.library.contact.PersonHolder;
import com.lab.fx.library.conversation.ConversationAdapter;
import com.lab.fx.library.conversation.MessageDB;
import com.lab.fx.library.conversation.MessageHolder;
import com.lab.fx.library.data.MessageBank;
import com.lab.fx.library.data.MessageCode;

import java.util.ArrayList;

public class ConversationActivity extends AppCompatActivity implements AppUICallback, View.OnClickListener{

    private PersonHolder mPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setUICallback(this);
        setTitle("Compose Message");
        setContentView(R.layout.conversation_activity);
        createView();
        fill(getIntent());
        load();
    }

    private RecyclerView mList;
    private ConversationAdapter mAdapter;
    private void createView() {
        mList = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lm.setReverseLayout(true);
        mList.setLayoutManager(lm);
        mList.setAdapter(mAdapter = new ConversationAdapter(this));
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
        mPerson = new PersonHolder();
        mPerson.pin = to;
        mPerson.first_name = to;
    }

    private void load() {
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                ArrayList<MessageHolder> records = MessageDB.getRecords(
                                 MessageDB.FIELD_F_PIN + "='" + mPerson.pin + "'" +
                        " OR " + MessageDB.FIELD_L_PIN + "='" + mPerson.pin + "'"
                        , MessageDB.FIELD_CREATED_TIME + " DESC ", null);
                mAdapter.addAll(records);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mAdapter.notifyDataSetChanged();
            }

        }.execute("");
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
                    ((EditText)findViewById(R.id.input)).setText("");
//                    App.process(MessageBank.msgRequest(to, text, "sms"));
                    App.process(MessageBank.msgRequest(to, text, "svr"));
                }
                break;
        }
    }

    @Override
    public void incomingData(String p_code, Object p_data) {
        switch (p_code) {
            case MessageCode.MSG_REQUEST :
            case MessageCode.MSG_SEND :
            {
                int add = mAdapter.add((MessageHolder) p_data);
                if (add != -1) {
                    mList.scrollToPosition(0);
                }
            }
            break;
            case MessageCode.MSG_UPDATE :
            {
                mAdapter.update((MessageHolder) p_data);
            }
            break;
        }
    }
}
