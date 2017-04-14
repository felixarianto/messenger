package com.lab.fx.messenger.person;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lab.fx.library.contact.ContactAdapter;
import com.lab.fx.library.db.DBHolder;
import com.lab.fx.messenger.R;
import com.lab.fx.messenger.dummy.DummyContent;
import com.lab.fx.messenger.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PersonFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PersonFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_person_list, container, false);
        try {
            ContactAdapter      adapter = new ContactAdapter(getContext());
            ArrayList<DBHolder> records = PersonDB.getRecords(null);
            for (DBHolder record : records) {
                adapter.add(
                        record.getString(PersonDB.FIELD_IMAGE_ID)
                        , record.getString(PersonDB.FIELD_FIRST_NAME) + " " + record.getString(PersonDB.FIELD_LAST_NAME)
                );
            }
            if (adapter.getItemCount() == 0) {
                adapter.add("", "Nada Nabila Azzahra", "Avaliable");
                adapter.add("", "Danis Alvaro", "Ultraman Biru");
                adapter.add("", "Khanza Adelia", "Hey there..");
                adapter.add("", "Rohmat Julianto", "Dolan bae");
                adapter.add("", "Febri Arianto", "Avaliable");
                adapter.add("", "Diana Fianty", "Avaliable");
                adapter.add("", "Mark Zukenberg", "Avaliable");
            }
            view.setLayoutManager(new LinearLayoutManager(getContext()));
            view.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        void onListFragmentInteraction(DummyItem item);
    }
}
