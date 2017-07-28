package com.lab.fx.messenger.flixgw;

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
import com.lab.fx.messenger.home.HomeRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PendingFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PendingFragment() {
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
        mAdapter.add("", "Diana Fianty",      "OL Store SALE TERBESAR – HEMAT hingga 70%",    "14:00", "");
        mAdapter.add("", "Febri Arianto",     "Promo Cashback Rp 50.000* dengan Kartu Debit / Kredit BNI", "13:50", "2");
        mAdapter.add("", "Rohmat Julianto",   "KOLEKSI GAYA Remaja – Diskon mulai 10%-70%",     "13:40", "2");
        mAdapter.add("", "Khanza Adelia",     "Store Busana PRIA Terkini HEMAT mulai 10% – 70%",  "12:33", "");
        mAdapter.add("", "Danis Alvaro",      "BUY 1 GET 1 FREE untuk Koleksi Exit, Exit Girls dan Expand",    "10:11", "");
        mAdapter.add("", "085423445512",      "LOTTEMART RETAIL Pesta Elektronik - Electro Shock! periode 27-30 Juli 2017",     "10:12", "");
        mAdapter.add("", "089123445566",      "BESOK!!! Promo Launching Fuwa Pillow Cake", "09:04", "3");
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
