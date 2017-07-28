package com.lab.fx.messenger.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lab.fx.library.conversation.ConversationPreviewAdapter;
import com.lab.fx.messenger.R;
import com.lab.fx.messenger.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HomeFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ConversationPreviewAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_home_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        view.setAdapter(mAdapter = new HomeRecyclerViewAdapter(view.getContext()));
        mAdapter.add("", "Diana Fianty",        "Good luck bro",    "14:00", "", "sent");
        mAdapter.add("", "Febri Arianto",       "Im late, plz dont come early", "13:50", "1");
        mAdapter.add("", "Rohmat Julianto",     "Im sorry bos",     "13:40", "10");
        mAdapter.add("", "Khanza Adelia",       "Have you buy ticket",  "12:33", "", "read");
        mAdapter.add("", "Danis Alvaro",        "Look for this one dude :)",    "10:11", "", "deliver");
        mAdapter.add("", "Mark Zukenberg",      "Not for sale sob",     "10:12", "", "send");
        mAdapter.add("", "Nada Nabila Azzahra", "Hey we gotta hang out right?", "09:04", "99⁺");
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
