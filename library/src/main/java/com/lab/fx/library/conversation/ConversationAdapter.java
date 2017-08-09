package com.lab.fx.library.conversation;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.util.TimeUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lab.fx.library.R;
import com.lab.fx.library.contact.Mine;
import com.lab.fx.library.util.MediaUtil;
import com.lab.fx.library.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by febri on 11/04/17.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.Holder> {

    private final ArrayList<MessageHolder> mData = new ArrayList<>();
    private final Context mContext;
    public ConversationAdapter(Context p_context) {
        mContext = p_context;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public TextView  title;
        public TextView  content;
        public TextView  time;

        public Holder(View p_view) {
            super(p_view);
            title   = (TextView)  p_view.findViewById(R.id.title);
            content = (TextView)  p_view.findViewById(R.id.content);
            time    = (TextView)  p_view.findViewById(R.id.time);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageHolder msg = mData.get(position);
        if (msg.f_pin.equals(Mine.get().pin)) {
            return 1;
        }
        return 2;
    }

    @Override
    public ConversationAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.conversation_adapter_left, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.conversation_adapter_right, parent, false);
        }
        return new ConversationAdapter.Holder(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(ConversationAdapter.Holder holder, int position) {
        MessageHolder msg = mData.get(position);
        holder.title  .setText(msg.f_pin);
        holder.content.setText(msg.text);
        holder.time   .setText(TimeUtil.toHour(msg.created_time));

        if (msg.f_pin.equals(Mine.get().pin)) {
            Drawable drawable_status = null;
            if (msg.status.equals(MessageDB.STATUS_WAITING)) {
                drawable_status = MediaUtil.getDrawable(mContext, R.drawable.message_status_sending, R.dimen.text_content);
            }
            else if (msg.status.equals(MessageDB.STATUS_SENT)) {
                drawable_status = MediaUtil.getDrawable(mContext, R.drawable.message_status_sent, R.dimen.text_content);
            }
            else if (msg.status.equals(MessageDB.STATUS_DELIVERED)) {
                drawable_status = MediaUtil.getDrawable(mContext, R.drawable.message_status_delivered, R.dimen.text_content);
            }
            else if (msg.status.equals(MessageDB.STATUS_READ)) {
                drawable_status = MediaUtil.getDrawable(mContext, R.drawable.message_status_read, R.dimen.text_content);
            }
            else if (msg.status.equals(MessageDB.STATUS_FAILED)) {
                drawable_status = MediaUtil.getDrawable(mContext, R.drawable.message_status_failed, R.dimen.text_content);
            }
            else {
                holder.time.setText(msg.status + " " + holder.time.getText());
            }
            holder.time.setCompoundDrawables(drawable_status, null, null, null);
        }

    }


    public void addAll(ArrayList<MessageHolder> p_holders) {
        mData.addAll(p_holders);
    }
    public int add(MessageHolder p_holder) {
        return update(1, p_holder);
    }
    public int update(MessageHolder p_holder) {
        return update(2, p_holder);
    }
    public int remove(MessageHolder p_holder) {
        return update(3, p_holder);
    }

    private synchronized int update(int state, MessageHolder p_holder) {
        int index = -1;
        for (int i = 0; i < mData.size(); i++) {
            if (p_holder.message_id.equals(mData.get(i).message_id)) {
                index = i;
                break;
            }
        }
        switch (state) {
            case 1 : //ADD
                if (index != -1) {
                    mData.set(index, p_holder);
                    notifyItemChanged(index);
                }
                else {
                    mData.add(0, p_holder);
                    notifyItemInserted(index = 0);
                }
                break;
            case 2 : //UPDATE
                if (index != -1) {
                    mData.set(index, p_holder);
                    notifyItemChanged(index);
                }
                break;
            case 3 : //DELETE
                mData.remove(index);
                notifyItemRemoved(index);
                break;
        }
        return index;
    }

}
