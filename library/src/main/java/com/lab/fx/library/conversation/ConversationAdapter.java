package com.lab.fx.library.conversation;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lab.fx.library.R;
import com.lab.fx.library.util.MediaUtil;

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
    public ConversationAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(parent);
    }

    @Override
    public void onBindViewHolder(ConversationAdapter.Holder holder, int position) {
        String[] data = mData.get(position);
        holder.img_1.setImageBitmap(MediaUtil.getImage(mContext, data[0]));
        holder.txt_1.setText(data[1]);
        holder.txt_2.setText(data[2]);
        holder.txt_3.setText(data[3]);
    }

    public void addItem(String[] p_item) {
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

        public Holder(ViewGroup parent) {
            super(RelativeLayout.inflate(parent.getContext(), R.layout.conversation_adapter, parent));
            img_1 = (ImageView) itemView.findViewById(R.id.img_1);
            txt_1 = (TextView)  itemView.findViewById(R.id.txt_1);
            txt_2 = (TextView)  itemView.findViewById(R.id.txt_2);
            txt_3 = (TextView)  itemView.findViewById(R.id.txt_3);
        }
    }

}
