package com.example.kiwi.tpprogresstracker.adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.model.SprintInfo;
import com.example.kiwi.tpprogresstracker.receiver.NotificationPublisher;
import com.example.kiwi.tpprogresstracker.ui.ToDOList;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kiwi on 9/28/2016.
 */
public class TodayListAdapter extends RecyclerView.Adapter<TodayListAdapter.TodayListViewHolder> {

    private Context m_context;
    private ArrayList<SprintInfo> m_SprintInfo;
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
    public void onBindViewHolder(TodayListViewHolder holder, final int position) {
        SprintInfo sprintInfo = m_SprintInfo.get(position);
        holder.txtProjectName.setText(sprintInfo.getProjectName());
        holder.txtSprintName.setText(sprintInfo.getSprintName());
        holder.txtStoriesCount.setText(sprintInfo.getStoriesCount());
        holder.txtStoriesOpenCount.setText(String.valueOf(sprintInfo.getStoriesOpen() + sprintInfo.getStoriesInDevelopment() + sprintInfo.getStoriesInDesign()));
        holder.txtStoriesInProgressCount.setText(String.valueOf(sprintInfo.getStoriesInTesting()));
        holder.txtStoriesDone.setText(String.valueOf(sprintInfo.getStoriesDone()));
        holder.txtBugCount.setText(sprintInfo.getBugsCount());
        holder.txtBugOpenCount.setText(String.valueOf(sprintInfo.getBugsOpen() + sprintInfo.getBugsInDevelopment()));
        holder.txtBugsInTesting.setText(String.valueOf(sprintInfo.getBugsInTesting()));
        holder.txtBugsDone.setText(String.valueOf(sprintInfo.getBugsDone()));
        holder.txtCurrentDay.setText(sprintInfo.getCurrentDay());
        holder.txtTotalDaysOfSprint.setText(sprintInfo.getTotalDaysOfSprint());
        double storiesProgress = sprintInfo.getStoriesSpentTime() / sprintInfo.getStoriesTotalEffort();
        if (sprintInfo.getStoriesSpentTime() == 0.0 && sprintInfo.getStoriesTotalEffort() == 0.0) {
            storiesProgress = 0;
        } else {
            if (Double.isInfinite(storiesProgress)) {
                storiesProgress = 0;
            }
            storiesProgress = storiesProgress * 100;
            storiesProgress = Double.parseDouble(new DecimalFormat("##.##").format(storiesProgress));
        }


        holder.txtStoriesProgress.setText(String.valueOf(storiesProgress) + "%");
        double bugsProgress = sprintInfo.getBugsSpentTime() / sprintInfo.getBugsTotalEffort();
        if (sprintInfo.getBugsSpentTime() == 0.0 && sprintInfo.getBugsTotalEffort() == 0.0) {
            bugsProgress = 0;
        } else {
            if (Double.isInfinite(bugsProgress)) {
                bugsProgress = 0;
            }
            bugsProgress = bugsProgress * 100;
            bugsProgress = Double.parseDouble(new DecimalFormat("##.##").format(bugsProgress));
        }
        holder.txtBugsProgress.setText(String.valueOf(bugsProgress) + "%");
        if (sprintInfo.getStoriesCount() == null) {
            sprintInfo.setStoriesCount("0");
        }
        if (sprintInfo.getCurrentDay() != null && sprintInfo.getTotalDaysOfSprint() != null) {
            if (sprintInfo.getCurrentDay().equals(sprintInfo.getTotalDaysOfSprint().replace("/", ""))) {
                holder.imgEscalate.setVisibility(View.VISIBLE);
                holder.checkBoxSentToClient.setVisibility(View.VISIBLE);
            } else {
                holder.imgEscalate.setVisibility(View.GONE);
                holder.checkBoxSentToClient.setVisibility(View.GONE);
            }
        }
        float fltOpenGraph = (sprintInfo.getStoriesOpen() + sprintInfo.getStoriesInDevelopment() + sprintInfo.getStoriesInDesign()) / Float.parseFloat(sprintInfo.getStoriesCount());
        if (Double.isNaN(fltOpenGraph)) {
            fltOpenGraph = 0;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100);
        lp.weight = fltOpenGraph;
        holder.layoutStoryOpenGraph.setLayoutParams(lp);
        float fltInTestingGraph = (sprintInfo.getStoriesInTesting()) / Float.parseFloat(sprintInfo.getStoriesCount());
        if (Double.isNaN(fltInTestingGraph)) {
            fltInTestingGraph = 0;
        }
        LinearLayout.LayoutParams lpInTestingGraph = new LinearLayout.LayoutParams(0, 100);
        lpInTestingGraph.weight = fltInTestingGraph;
        holder.layoutStoryInProgressGraph.setLayoutParams(lpInTestingGraph);
        float fltStoryDoneGraph = (float) sprintInfo.getStoriesDone() / Float.parseFloat(sprintInfo.getStoriesCount());
        if (Double.isNaN(fltStoryDoneGraph)) {
            fltStoryDoneGraph = 0;
        }
        LinearLayout.LayoutParams lpDoneGraph = new LinearLayout.LayoutParams(0, 100);
        lpDoneGraph.weight = fltStoryDoneGraph;
        holder.layoutStoryDoneGraph.setLayoutParams(lpDoneGraph);

        if (sprintInfo.getBugsCount() == null) {
            sprintInfo.setBugsCount("0");
        }
        float fBugsOpenGraph = (sprintInfo.getBugsOpen() + sprintInfo.getBugsInDevelopment()) / Float.parseFloat(sprintInfo.getBugsCount());
        if (Double.isNaN(fBugsOpenGraph)) {
            fBugsOpenGraph = 0;
        }
        LinearLayout.LayoutParams lpOpenBugs = new LinearLayout.LayoutParams(0, 100);
        lpOpenBugs.weight = fBugsOpenGraph;
        holder.layoutBugsOpenGraph.setLayoutParams(lpOpenBugs);
        float fBugsInTestingGraph = (sprintInfo.getBugsInTesting()) / Float.parseFloat(sprintInfo.getBugsCount());
        if (Double.isNaN(fBugsInTestingGraph)) {
            fBugsInTestingGraph = 0;
        }
        if (sprintInfo.getBugsInTesting() == 0 && Float.parseFloat(sprintInfo.getBugsCount()) == 0) {
            fBugsInTestingGraph = 0;
        }
        LinearLayout.LayoutParams lpBugsInTestingGraph = new LinearLayout.LayoutParams(0, 100);
        lpBugsInTestingGraph.weight = fBugsInTestingGraph;
        holder.layoutBugsInTestingGraph.setLayoutParams(lpBugsInTestingGraph);

        float fBugsDoneGraph = (float) sprintInfo.getBugsDone() / Float.parseFloat(sprintInfo.getBugsCount());
        if (Double.isNaN(fBugsDoneGraph)) {
            fBugsDoneGraph = 0;
        }
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


        if (sprintInfo.getSprintStartDate() != null && sprintInfo.getSprintStartDate() > 0) {
            Date date = new Date(sprintInfo.getSprintStartDate());
            Format format = new SimpleDateFormat("dd/MM/yyyy");
            holder.txtStartDate.setText(format.format(date));
        } else {
            holder.txtStartDate.setText(null);
        }
        if (sprintInfo.getSprintEndDate() != null && sprintInfo.getSprintEndDate() > 0) {
            Date date = new Date(sprintInfo.getSprintEndDate());
            Format format = new SimpleDateFormat("dd/MM/yyyy");
            holder.txtEndDate.setText(format.format(date));
        } else {
            holder.txtEndDate.setText(null);
        }
        holder.imgSendMail.setTag(sprintInfo);
        holder.imgToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SprintInfo info = m_SprintInfo.get(position);
                Intent intent = new Intent(m_context, ToDOList.class);
                intent.putExtra("day", info.getCurrentDay());
                intent.putExtra("project_id", info.getProjectId());
                intent.putExtra("project_name", info.getProjectName());
                m_context.startActivity(intent);
            }
        });
        holder.checkBoxSentToClient.setChecked(sprintInfo.isBuildSentToClient());
        holder.checkBoxSentToClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SprintInfo info = m_SprintInfo.get(position);
                if (((CheckBox) view).isChecked()) {
                    info.setBuildSentToClient(true);
                } else {
                    info.setBuildSentToClient(false);
                }
            }
        });
        holder.imgEscalate.setTag(sprintInfo);
        holder.cvTodayLayout.setTag(sprintInfo);
        if (sprintInfo.getTotalDaysOfSprint() == null || sprintInfo.getTotalDaysOfSprint() == "") {
            sprintInfo.setTotalDaysOfSprint("/0");
        }
        if (sprintInfo.getCurrentDay() == null || sprintInfo.getCurrentDay() == "") {
            sprintInfo.setCurrentDay("0");
        }
//        int numberOfDays = Integer.valueOf(sprintInfo.getTotalDaysOfSprint().split("/")[1]);
//        int dayPercentage = (numberOfDays * 50) / 100;
//        if (Integer.parseInt(sprintInfo.getCurrentDay()) > dayPercentage) {
//            String projectName = sprintInfo.getProjectName();
//            String currentDay = sprintInfo.getCurrentDay() + sprintInfo.getTotalDaysOfSprint();
//            String bugsNotDone = holder.txtBugOpenCount.getText() + "/" + sprintInfo.getBugsCount();
//            String storiesNotDone = holder.txtStoriesOpenCount.getText() + "/" + sprintInfo.getStoriesCount();
//            scheduleNotification(getNotification(projectName, currentDay, bugsNotDone, storiesNotDone), 20000, sprintInfo.getProjectId());
//            PendingIntent pendingIntent = getPendingIntent(getNotification(projectName, currentDay, bugsNotDone, storiesNotDone), sprintInfo.getProjectId());
//            AlarmManager alarmManager = (AlarmManager) m_context.getSystemService(Context.ALARM_SERVICE);
//            //alarmManager.cancel(pendingIntent);
//        }
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
        TextView txtStoriesInProgressCount, txtStoriesDone, txtStoriesProgress, txtBugsProgress;
        TextView txtBugsInTesting, txtBugsDone, txtTotalDaysOfSprint;
        LinearLayout layoutStoryOpenGraph, layoutStoryInProgressGraph, layoutStoryDoneGraph, layoutSprintData;
        LinearLayout layoutBugsOpenGraph, layoutBugsInTestingGraph, layoutBugsDoneGraph;
        LinearLayout layoutOverallOpenGraph, layoutOverallInTestingGraph, layoutOverallDoneGraph;
        CardView cvTodayLayout;
        ImageView imgSendMail, imgToDo, imgEscalate;
        CheckBox checkBoxSentToClient;

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
            txtStoriesProgress = (TextView) itemView.findViewById(R.id.txtStoriesProgress);
            txtBugsProgress = (TextView) itemView.findViewById(R.id.txtBugsProgress);

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

            checkBoxSentToClient = (CheckBox) itemView.findViewById(R.id.checkBoxSentToClient);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


    @SuppressLint("ShortAlarm")
    private void scheduleNotification(Notification notification, int delay, String notificationID) {

        PendingIntent pendingIntent = getPendingIntent(notification, notificationID);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) m_context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), delay, pendingIntent);
    }

    private PendingIntent getPendingIntent(Notification notification, String notificationID) {
        Intent notificationIntent = new Intent(m_context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        return PendingIntent.getBroadcast(m_context, Integer.valueOf(notificationID), notificationIntent, 0);
    }

    private Notification getNotification(String bigContentTitle, String day, String bugsNotDone, String storiesNotDone) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(m_context);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(bigContentTitle);
        //inboxStyle.setSummaryText("(line.length) getBigText()");
        inboxStyle.addLine("day : " + day);
        inboxStyle.addLine("Bugs not done: " + bugsNotDone);
        inboxStyle.addLine("stories not done: " + storiesNotDone);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.kiwi_logo);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setStyle(inboxStyle);
        }
//        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
//        builder.setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.build();
        }

//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("Scheduled Notification");
//        builder.setContentText(content);
//        builder.setSmallIcon(R.drawable.ic_launcher);
        return builder.build();
    }

}
