package com.lab.fx.messenger.flixgw;

import android.content.Context;
import android.view.View;

import com.lab.fx.library.conversation.ConversationPreviewAdapter;
import com.lab.fx.library.conversation.MessageDB;
import com.lab.fx.library.conversation.MessageHolder;
import com.lab.fx.library.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by febri on 12/08/17.
 */

public class ListAdapter extends ConversationPreviewAdapter {

    public ListAdapter(Context p_context) {
        super(p_context);
    }

    public ArrayList<MessageHolder> DATA = new ArrayList<>();
    @Override
    public int getItemCount() {
        return DATA.size();
    }

    @Override
    public void onBindViewHolder(ConversationPreviewAdapter.Holder holder, int position) {
        MessageHolder data = DATA.get(position);
        holder.txt_1.setText(data.f_pin);
        holder.txt_2.setText(data.text);
        holder.txt_3.setText(TimeUtil.toHour(data.created_time));

        if (!data.status.equals(MessageDB.STATUS_READ)) {
            holder.txt_3.setTextColor(mContext.getResources().getColor(com.lab.fx.library.R.color.color_accent));
            holder.txt_4.setVisibility(View.VISIBLE);
            holder.txt_4.setText("N");
        }
        else {
            holder.txt_3.setTextColor(mContext.getResources().getColor(com.lab.fx.library.R.color.text_content));
            holder.txt_4.setVisibility(View.GONE);
        }

    }




}
