package com.example.kiwi.tpprogresstracker.ui;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kiwi.tpprogresstracker.R;

public class    TodayListItem extends AppCompatActivity {

    TextView txtProjectName, txtSprintName, txtStoriesCount, txtStoriesNotDoneCount, txtBugCount, txtBugNotDoneCount,txtCurrentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_list_item);
        txtProjectName = (TextView) findViewById(R.id.txtProjectName);
        txtSprintName = (TextView) findViewById(R.id.txtSprintName);
        txtStoriesCount = (TextView) findViewById(R.id.txtStoriesCount);
        txtStoriesNotDoneCount = (TextView) findViewById(R.id.txtStoriesOpenCount);
        txtBugCount = (TextView) findViewById(R.id.txtBugsCount);
        txtBugNotDoneCount = (TextView) findViewById(R.id.txtBugsOpenCount);
        txtCurrentDay = (TextView) findViewById(R.id.txtCurrentDay);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/RobotoSlab-Regular.ttf");
        Typeface typefaceLight = Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Semilight.ttf");
        txtProjectName.setTypeface(typeface);
        txtSprintName.setTypeface(typefaceLight);
        txtStoriesCount.setTypeface(typefaceLight);
        txtStoriesNotDoneCount.setTypeface(typefaceLight);
        txtBugCount.setTypeface(typefaceLight);
        txtBugNotDoneCount.setTypeface(typefaceLight);
        txtProjectName.setTypeface(typefaceLight);
        txtCurrentDay.setTypeface(typeface);
    }
}
