package com.lab.fx.library.contact;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Holder> {

    private final ArrayList<String[]> mData = new ArrayList<>();
    private final Context mContext;
    public ContactAdapter(Context p_context) {
        mContext = p_context;
    }

    @Override
    public ContactAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.contact_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.Holder holder, int position) {
        String[] data = mData.get(position);
        Bitmap bitmap = MediaUtil.getImage(mContext, data.length > 0 ? data[0] : null);
        if (bitmap != null) {
            holder.img_1.setImageBitmap(bitmap);
        }
        else {
            holder.img_1.setImageResource(R.drawable.ic_person_white);
        }
        holder.txt_1.setText(data.length > 1 ? data[1] : "");
        holder.txt_2.setText(data.length > 2 ? data[2] : "");
        holder.txt_3.setText(data.length > 3 ? data[3] : "");
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

        public Holder(View p_view) {
            super(p_view);
            img_1 = (ImageView) p_view.findViewById(R.id.img_1);
            txt_1 = (TextView)  p_view.findViewById(R.id.txt_1);
            txt_2 = (TextView)  p_view.findViewById(R.id.txt_2);
            txt_3 = (TextView)  p_view.findViewById(R.id.txt_3);
        }
    }

}
