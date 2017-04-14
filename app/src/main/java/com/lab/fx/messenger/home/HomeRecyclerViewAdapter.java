package com.lab.fx.messenger.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lab.fx.library.conversation.ConversationAdapter;
import com.lab.fx.messenger.R;
import com.lab.fx.messenger.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link HomeFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HomeRecyclerViewAdapter extends ConversationAdapter {

    public HomeRecyclerViewAdapter(Context p_context) {
        super(p_context);
    }

}
