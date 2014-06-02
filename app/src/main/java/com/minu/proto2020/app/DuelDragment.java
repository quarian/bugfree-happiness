package com.minu.proto2020.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DuelDragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DuelDragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */

public class DuelDragment extends Fragment {

    private OnFragmentInteractionListener mListener;


    private TextView mLifePickerOne;
    private TextView mLifePickerTwo;
    private TextView mPoisonPickerOne;
    private TextView mPoisonPickerTwo;

    private boolean mPoisonShowing;
    private ImageButton mPoisonButton;

    private boolean mSpun;

    private float mPickerY;

    public static DuelDragment newInstance() {
        DuelDragment fragment = new DuelDragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public DuelDragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_duel_dragment, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
