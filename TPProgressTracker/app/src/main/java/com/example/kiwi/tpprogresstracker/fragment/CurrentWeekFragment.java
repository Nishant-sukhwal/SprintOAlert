package com.example.kiwi.tpprogresstracker.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kiwi.tpprogresstracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeekFragment extends Fragment {


    public CurrentWeekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_week, container, false);
    }

}
