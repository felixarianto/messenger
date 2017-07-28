package com.lab.fx.library.conversation;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lab.fx.library.R;
import com.lab.fx.library.util.MediaUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by febri on 11/04/17.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.Holder> {

    private final ArrayList<String[]> mData = new ArrayList<>();
    private final Context mContext;
    public ConversationAdapter(Context p_context) {
        mContext = p_context;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ConversationAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.conversation_adapter, parent, false);
        return new ConversationAdapter.Holder(view);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(ConversationAdapter.Holder holder, int position) {
        String[] data = mData.get(position);
        holder.txt_1.setText(data.length > 0 ? data[0] : "");
        holder.txt_3.setText(data.length > 1 ? data[1] : "");

        Drawable drawable_left = null;
        if (data.length > 5) {
            String status = data[5];
            if (status.equals("send")) {
                drawable_left = MediaUtil.getDrawable(mContext, R.drawable.message_status_sending, R.dimen.text_content);
            }
            else if (status.equals("sent")) {
                drawable_left = MediaUtil.getDrawable(mContext, R.drawable.message_status_sent, R.dimen.text_content);
            }
            else if (status.equals("deliver")) {
                drawable_left = MediaUtil.getDrawable(mContext, R.drawable.message_status_delivered, R.dimen.text_content);
            }
            else if (status.equals("read")) {
                drawable_left = MediaUtil.getDrawable(mContext, R.drawable.message_status_read, R.dimen.text_content);
            }
            else if (status.equals("fail")) {
                drawable_left = MediaUtil.getDrawable(mContext, R.drawable.message_status_failed, R.dimen.text_content);
            }
        }
        holder.txt_3.setCompoundDrawables(drawable_left, null, null, null);
        holder.txt_3.setCompoundDrawablePadding(toDIP(1));
    }

    protected final int toDIP(float p_value) {
        return (int) ((p_value * mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public void add(String... p_item) {
        mData.add(p_item);
        notifyItemInserted(mData.size() - 1);
    }

    public void removeItem(int p_position) {
        if (p_position > mData.size()) {
            mData.remove(p_position);
            notifyItemRemoved(p_position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public ImageView img_1;
        public TextView  txt_1;
        public TextView  txt_2;
        public TextView  txt_3;
        public TextView  txt_4;

        public Holder(View p_view) {
            super(p_view);
            img_1 = (ImageView) p_view.findViewById(R.id.img_1);
            txt_1 = (TextView)  p_view.findViewById(R.id.txt_1);
            txt_2 = (TextView)  p_view.findViewById(R.id.txt_2);
            txt_3 = (TextView)  p_view.findViewById(R.id.txt_3);
            txt_4 = (TextView)  p_view.findViewById(R.id.txt_4);
        }
    }

}
