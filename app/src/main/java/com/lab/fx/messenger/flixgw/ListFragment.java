package com.lab.fx.messenger.flixgw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lab.fx.library.app.App;
import com.lab.fx.library.app.AppUICallback;
import com.lab.fx.library.conversation.ConversationPreviewAdapter;
import com.lab.fx.library.conversation.MessageDB;
import com.lab.fx.library.conversation.MessageHolder;
import com.lab.fx.library.data.Message;
import com.lab.fx.library.data.MessageCode;
import com.lab.fx.library.data.MessageKey;
import com.lab.fx.library.util.TimeUtil;
import com.lab.fx.messenger.MainActivity;
import com.lab.fx.messenger.MainFragment;
import com.lab.fx.messenger.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class ListFragment extends MainFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ListAdapter mAdapter;
    private RecyclerView mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mList = (RecyclerView) inflater.inflate(R.layout.fragment_home_list, container, false);
        mList.setLayoutManager(new LinearLayoutManager(mList.getContext()));
        mAdapter = new ListAdapter(mList.getContext()) {
            @Override
            public void onBindViewHolder(Holder holder,final int position) {
                super.onBindViewHolder(holder, position);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onListClick(mAdapter.DATA.get(position));

                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return onListLongClick(mAdapter.DATA.get(position));
                    }
                });
            }
        };
        mList.setAdapter(mAdapter);
        load();
        return mList;
    }

    private void load() {
        new AsyncTask<String[], String[], ArrayList<MessageHolder>>() {
            @Override
            protected ArrayList<MessageHolder> doInBackground(String[]... params) {
                ArrayList<MessageHolder> records = null;
                try {
                    records = MessageDB.getRecords(null, MessageDB.FIELD_CREATED_TIME + " DESC", null);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "", e);
                }
                return records;
            }

            @Override
            protected void onPostExecute(ArrayList<MessageHolder> result) {
                try {
                    super.onPostExecute(result);
                    if (result != null) {
                        mAdapter.DATA.clear();
                        mAdapter.DATA.addAll(result);
                        mAdapter.notifyDataSetChanged();
                        reCounter();
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "", e);
                }
            }
        }.execute();
    }

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(mContext = context);
        App.putUICallback(getClass().getName(), this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void incomingData(String p_code, Object p_data) {
        if (p_code.equals(MessageCode.MSG_SEND)) {
            MessageHolder data = (MessageHolder) p_data;
            MessageHolder h;
            boolean updated = false;
            for (int i = 0; i < mAdapter.DATA.size(); i++) {
                h = mAdapter.DATA.get(i);
                if (!data.message_id.equals(h.message_id)) {
                    continue;
                }
                mAdapter.DATA.set(i, data);
                mAdapter.notifyItemChanged(i);
                updated = true;
                break;
            }
            if (!updated) {
                mAdapter.DATA.add(0, data);
                mAdapter.notifyItemInserted(0);
                mList.scrollToPosition(0);
            }
            reCounter();
        }
        else if (p_code.equals(MessageCode.MSG_DELETE)) {
            MessageHolder data = (MessageHolder) p_data;
            MessageHolder h;
            for (int i = 0; i < mAdapter.DATA.size(); i++) {
                h = mAdapter.DATA.get(i);
                if (!data.message_id.equals(h.message_id)) {
                    continue;
                }
                mAdapter.DATA.remove(i);
                mAdapter.notifyItemRemoved(i);
                break;
            }
            reCounter();
        }
        else if (p_code.equals(MessageCode.MSG_UPDATE)) {
            MessageHolder data = (MessageHolder) p_data;
            MessageHolder h;
            for (int i = 0; i < mAdapter.DATA.size(); i++) {
                h = mAdapter.DATA.get(i);
                if (!data.message_id.equals(h.message_id)) {
                    continue;
                }
                mAdapter.DATA.set(i, data);
                mAdapter.notifyItemChanged(i);
                break;
            }
            reCounter();
        }
    }

    private void reCounter() {
        String title = "No Updates";
        int count = MessageDB.count(MessageDB.FIELD_STATUS + "!='" + MessageDB.STATUS_READ + "'");
        if (count > 0) {
            title = "Updates (" + count + ")";
        }
        ((Activity) mContext).setTitle(title);
    }

    public void onListClick(MessageHolder holder) {
        try {
            Message m = new Message();
            m.put(MessageKey.MESSAGE_CODE, MessageCode.MSG_UPDATE);
            m.put(MessageKey.STATUS,       MessageDB.STATUS_READ);
            m.put(MessageKey.MESSAGE_ID,   holder.message_id);
            App.process(m);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
        if (holder.link == null || !holder.link.startsWith("http")) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.link));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean onListLongClick(final MessageHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(new String[]{"Remove", "Open"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    try {
                        Message m = new Message();
                        m.put(MessageKey.MESSAGE_CODE, MessageCode.MSG_DELETE);
                        m.put(MessageKey.MESSAGE_ID,   holder.message_id);
                        App.process(m);
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "", e);
                    }
                }
                else if (which == 1) {
                    onListClick(holder);
                }
            }
        });
        builder.create().show();
        return true;
    }
}
