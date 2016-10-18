package com.example.kiwi.tpprogresstracker.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.model.SprintInfo;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kiwi on 9/28/2016.
 */
public class TodayListAdapter extends RecyclerView.Adapter<TodayListAdapter.TodayListViewHolder> {

    Context m_context;
    ArrayList<SprintInfo> m_SprintInfo;
    MyClickListener myClickListener;
    public static final int PARENT_ITEM = 0;
    public static final int CHILD_ITEM = 1;

    public TodayListAdapter(Context context, ArrayList<SprintInfo> sprintInfoArrayList) {
        super();
        m_context = context;
        m_SprintInfo = sprintInfoArrayList;
    }

    @Override
    public TodayListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_today_list_item, parent, false);
        TodayListViewHolder todayListViewHolder = new TodayListViewHolder(view);
        return todayListViewHolder;
    }

    @Override
    public void onBindViewHolder(TodayListViewHolder holder, int position) {
        holder.txtProjectName.setText(m_SprintInfo.get(position).getProjectName());
        holder.txtSprintName.setText(m_SprintInfo.get(position).getSprintName());
        holder.txtStoriesCount.setText(m_SprintInfo.get(position).getStoriesCount());
        holder.txtStoriesOpenCount.setText(String.valueOf(m_SprintInfo.get(position).getStoriesOpen() + m_SprintInfo.get(position).getStoriesInDevelopment() + m_SprintInfo.get(position).getStoriesInDesign()));
        holder.txtStoriesInProgressCount.setText(String.valueOf(m_SprintInfo.get(position).getStoriesInTesting()));
        holder.txtStoriesDone.setText(String.valueOf(m_SprintInfo.get(position).getStoriesDone()));
        holder.txtBugCount.setText(m_SprintInfo.get(position).getBugsCount());
        holder.txtBugOpenCount.setText(String.valueOf(m_SprintInfo.get(position).getBugsOpen() + m_SprintInfo.get(position).getBugsInDevelopment()));
        holder.txtBugsInTesting.setText(String.valueOf(m_SprintInfo.get(position).getBugsInTesting()));
        holder.txtBugsDone.setText(String.valueOf(m_SprintInfo.get(position).getBugsDone()));
        holder.txtCurrentDay.setText(m_SprintInfo.get(position).getCurrentDay());
        holder.txtTotalDaysOfSprint.setText(m_SprintInfo.get(position).getTotalDaysOfSprint());
        if (m_SprintInfo.get(position).getStoriesCount() == null) {
            m_SprintInfo.get(position).setStoriesCount("0");
        }

        float fltOpenGraph = (m_SprintInfo.get(position).getStoriesOpen() + m_SprintInfo.get(position).getStoriesInDevelopment() + m_SprintInfo.get(position).getStoriesInDesign()) / Float.parseFloat(m_SprintInfo.get(position).getStoriesCount());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100);
        lp.weight = fltOpenGraph;
        holder.layoutStoryOpenGraph.setLayoutParams(lp);
        float fltInTestingGraph = (m_SprintInfo.get(position).getStoriesInTesting()) / Float.parseFloat(m_SprintInfo.get(position).getStoriesCount());
        LinearLayout.LayoutParams lpInTestingGraph = new LinearLayout.LayoutParams(0, 100);
        lpInTestingGraph.weight = fltInTestingGraph;
        holder.layoutStoryInProgressGraph.setLayoutParams(lpInTestingGraph);
        float fltStoryDoneGraph = (float) m_SprintInfo.get(position).getStoriesDone() / Float.parseFloat(m_SprintInfo.get(position).getStoriesCount());
        LinearLayout.LayoutParams lpDoneGraph = new LinearLayout.LayoutParams(0, 100);
        lpDoneGraph.weight = fltStoryDoneGraph;
        holder.layoutStoryDoneGraph.setLayoutParams(lpDoneGraph);

        if (m_SprintInfo.get(position).getBugsCount() == null) {
            m_SprintInfo.get(position).setBugsCount("0");
        }
        float fBugsOpenGraph = (m_SprintInfo.get(position).getBugsOpen() + m_SprintInfo.get(position).getBugsInDevelopment()) / Float.parseFloat(m_SprintInfo.get(position).getBugsCount());
        LinearLayout.LayoutParams lpOpenBugs = new LinearLayout.LayoutParams(0, 100);
        lpOpenBugs.weight = fBugsOpenGraph;
        holder.layoutBugsOpenGraph.setLayoutParams(lpOpenBugs);
        float fBugsInTestingGraph = (m_SprintInfo.get(position).getBugsInTesting()) / Float.parseFloat(m_SprintInfo.get(position).getBugsCount());
        if (m_SprintInfo.get(position).getBugsInTesting() == 0 && Float.parseFloat(m_SprintInfo.get(position).getBugsCount()) == 0) {
            fBugsInTestingGraph = 0;
        }
        LinearLayout.LayoutParams lpBugsInTestingGraph = new LinearLayout.LayoutParams(0, 100);
        lpBugsInTestingGraph.weight = fBugsInTestingGraph;
        holder.layoutBugsInTestingGraph.setLayoutParams(lpBugsInTestingGraph);

        float fBugsDoneGraph = (float) m_SprintInfo.get(position).getBugsDone() / Float.parseFloat(m_SprintInfo.get(position).getBugsCount());
        LinearLayout.LayoutParams lpBugsDoneGraph = new LinearLayout.LayoutParams(0, 100);
        lpBugsDoneGraph.weight = fBugsDoneGraph;
        holder.layoutBugsDoneGraph.setLayoutParams(lpBugsDoneGraph);


        LinearLayout.LayoutParams lpOverallOpenGraph = new LinearLayout.LayoutParams(0, 100);
        lpOverallOpenGraph.weight = (fltOpenGraph + fBugsOpenGraph) / 2;
        holder.layoutOverallOpenGraph.setLayoutParams(lpOverallOpenGraph);
        LinearLayout.LayoutParams lpOverallInTestingGraph = new LinearLayout.LayoutParams(0, 100);
        lpOverallInTestingGraph.weight = (fltInTestingGraph + fBugsInTestingGraph) / 2;
        holder.layoutOverallInTestingGraph.setLayoutParams(lpOverallInTestingGraph);
        LinearLayout.LayoutParams lpOverallDoneGraph = new LinearLayout.LayoutParams(0, 100);
        lpOverallDoneGraph.weight = (fltStoryDoneGraph + fBugsDoneGraph) / 2;
        holder.layoutOverallDoneGraph.setLayoutParams(lpOverallDoneGraph);


        if (m_SprintInfo.get(position).getSprintStartDate() != null && m_SprintInfo.get(position).getSprintStartDate() > 0) {
            Date date = new Date(m_SprintInfo.get(position).getSprintStartDate());
            Format format = new SimpleDateFormat("dd/MM/yyyy");
            holder.txtStartDate.setText(format.format(date));
        } else {
            holder.txtStartDate.setText(null);
        }
        if (m_SprintInfo.get(position).getSprintEndDate() != null && m_SprintInfo.get(position).getSprintEndDate() > 0) {
            Date date = new Date(m_SprintInfo.get(position).getSprintEndDate());
            Format format = new SimpleDateFormat("dd/MM/yyyy");
            holder.txtEndDate.setText(format.format(date));
        } else {
            holder.txtEndDate.setText(null);
        }
        holder.imgSendMail.setTag(m_SprintInfo.get(position));
        holder.imgToDo.setTag(m_SprintInfo.get(position));
        holder.imgEscalate.setTag(m_SprintInfo.get(position));
        holder.cvTodayLayout.setTag(m_SprintInfo.get(position));

        //holder.cvTodayLayout.setCardBackgroundColor(m_SprintInfo.get(position).getCardBackgroundColor());
    }

    @Override
    public int getItemCount() {
        return m_SprintInfo.size();
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public class TodayListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtProjectName, txtSprintName, txtStoriesCount, txtStoriesOpenCount, txtBugCount, txtBugOpenCount, txtCurrentDay, txtStartDate, txtEndDate;
        TextView txtStoriesInProgressCount, txtStoriesDone;
        TextView txtBugsInTesting, txtBugsDone, txtTotalDaysOfSprint;
        LinearLayout layoutStoryOpenGraph, layoutStoryInProgressGraph, layoutStoryDoneGraph, layoutSprintData;
        LinearLayout layoutBugsOpenGraph, layoutBugsInTestingGraph, layoutBugsDoneGraph;
        LinearLayout layoutOverallOpenGraph, layoutOverallInTestingGraph, layoutOverallDoneGraph;
        CardView cvTodayLayout;
        ImageView imgSendMail, imgToDo, imgEscalate;

        public TodayListViewHolder(View itemView) {
            super(itemView);
            txtProjectName = (TextView) itemView.findViewById(R.id.txtProjectName);
            txtSprintName = (TextView) itemView.findViewById(R.id.txtSprintName);
            txtStoriesCount = (TextView) itemView.findViewById(R.id.txtStoriesCount);
            txtStoriesOpenCount = (TextView) itemView.findViewById(R.id.txtStoriesOpenCount);
            txtStoriesInProgressCount = (TextView) itemView.findViewById(R.id.txtStoriesInProgressCount);
            txtStoriesDone = (TextView) itemView.findViewById(R.id.txtStoriesDone);
            txtBugCount = (TextView) itemView.findViewById(R.id.txtBugsCount);
            txtBugOpenCount = (TextView) itemView.findViewById(R.id.txtBugsOpenCount);
            txtBugsInTesting = (TextView) itemView.findViewById(R.id.txtBugsInTesting);
            txtBugsDone = (TextView) itemView.findViewById(R.id.txtBugsDone);
            txtCurrentDay = (TextView) itemView.findViewById(R.id.txtCurrentDay);
            txtTotalDaysOfSprint = (TextView) itemView.findViewById(R.id.txtTotalDaysOfSprint);
            txtStartDate = (TextView) itemView.findViewById(R.id.txtStartDate);
            txtEndDate = (TextView) itemView.findViewById(R.id.txtEndDate);
            cvTodayLayout = (CardView) itemView.findViewById(R.id.cvTodayLayout);
            layoutStoryOpenGraph = (LinearLayout) itemView.findViewById(R.id.layoutStoryOpenGraph);
            layoutStoryInProgressGraph = (LinearLayout) itemView.findViewById(R.id.layoutStoryInProgressGraph);
            layoutStoryDoneGraph = (LinearLayout) itemView.findViewById(R.id.layoutStoryDoneGraph);

            layoutBugsOpenGraph = (LinearLayout) itemView.findViewById(R.id.layoutBugsOpenGraph);
            layoutBugsInTestingGraph = (LinearLayout) itemView.findViewById(R.id.layoutBugsInTestingGraph);
            layoutBugsDoneGraph = (LinearLayout) itemView.findViewById(R.id.layoutBugsDoneGraph);

            layoutOverallOpenGraph = (LinearLayout) itemView.findViewById(R.id.layoutOverallOpenGraph);
            layoutOverallInTestingGraph = (LinearLayout) itemView.findViewById(R.id.layoutOverallInTestingGraph);
            layoutOverallDoneGraph = (LinearLayout) itemView.findViewById(R.id.layoutOverallDoneGraph);

            //layoutSprintData = (LinearLayout) itemView.findViewById(R.id.layoutSprintData);
            imgSendMail = (ImageView) itemView.findViewById(R.id.imgSendMail);
            imgToDo = (ImageView) itemView.findViewById(R.id.imgToDo);
            imgEscalate = (ImageView) itemView.findViewById(R.id.imgEscalate);
            imgSendMail.setOnClickListener(this);
            imgToDo.setOnClickListener(this);
            imgEscalate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
