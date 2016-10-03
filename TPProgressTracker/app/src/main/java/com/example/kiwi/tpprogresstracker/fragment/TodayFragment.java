package com.example.kiwi.tpprogresstracker.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.adapter.TodayListAdapter;
import com.example.kiwi.tpprogresstracker.classes.ProjectInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    RecyclerView rvTodaySprint;
    TodayListAdapter todayListAdapter;

    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        initViews(view);
        todayListAdapter = new TodayListAdapter(getContext(), ProjectInfo.getInstance().getProjectList());
        rvTodaySprint.setAdapter(todayListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTodaySprint.setLayoutManager(linearLayoutManager);
        todayListAdapter.notifyDataSetChanged();
        // Inflate the layout for this fragment
        return view;
    }

    private void initViews(View view) {
        rvTodaySprint = (RecyclerView) view.findViewById(R.id.rvTodaySprints);
    }


    public void doWork() {
        if(!isAdded()) return;
        if(todayListAdapter != null){
            todayListAdapter.notifyDataSetChanged();
        }
    }
}
