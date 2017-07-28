package com.lab.fx.messenger.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lab.fx.messenger.conversation.ConversationActivity;
import com.lab.fx.library.conversation.ConversationPreviewAdapter;
import com.lab.fx.messenger.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link HomeFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HomeRecyclerViewAdapter extends ConversationPreviewAdapter {

    public HomeRecyclerViewAdapter(Context p_context) {
        super(p_context);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ConversationActivity.class);
                intent.putExtra("to",   mData.get(position)[1]);
                intent.putExtra("text", mData.get(position)[2]);
                v.getContext().startActivity(intent);
            }
        });
    }
}
