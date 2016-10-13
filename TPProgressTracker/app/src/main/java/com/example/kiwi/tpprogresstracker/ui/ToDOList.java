package com.example.kiwi.tpprogresstracker.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.adapter.ActionItemAdapter;
import com.example.kiwi.tpprogresstracker.model.ActionItemInfo;
import com.example.kiwi.tpprogresstracker.model.ActionItems;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;

import java.util.ArrayList;

public class ToDOList extends AppCompatActivity {

    RecyclerView rvActionItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_dolist);
        rvActionItem = (RecyclerView) findViewById(R.id.rvActionItem);
        ActionItemInfo.actionItems.clear();
        for (int i = 1; i < 4; i++) {
            ActionItems actionItems = new ActionItems();
            actionItems.setDay(String.valueOf(i));
            actionItems.setCurrent(true);
            ArrayList<InnerActionItems> lstString = new ArrayList<>();
            for (int j = 1; j < 5; j++) {
                InnerActionItems innerItems = new InnerActionItems(String.valueOf(j), actionItems.getDay(), "item" + j);
                lstString.add(innerItems);
            }
            actionItems.setItems(lstString);
            ActionItemInfo.actionItems.add(actionItems);
        }
        ActionItemAdapter actionItemAdapter = new ActionItemAdapter(this, ActionItemInfo.getInstance().getActionItems());
        rvActionItem.setAdapter(actionItemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvActionItem.setLayoutManager(linearLayoutManager);

        actionItemAdapter.notifyDataSetChanged();
        rvActionItem.setClickable(true);
    }
}
