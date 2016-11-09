package com.example.kiwi.tpprogresstracker;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.example.kiwi.tpprogresstracker.adapter.DashboardPagerAdapter;
import com.example.kiwi.tpprogresstracker.callback.OnRefreshProject;
import com.example.kiwi.tpprogresstracker.classes.ProjectInfo;
import com.example.kiwi.tpprogresstracker.fragment.TodayFragment;
import com.example.kiwi.tpprogresstracker.httpmanager.apihandler;
import com.example.kiwi.tpprogresstracker.interfaces.internalCallback;
import com.example.kiwi.tpprogresstracker.model.SprintInfo;
import com.example.kiwi.tpprogresstracker.model.bugs.StoryBugs;
import com.example.kiwi.tpprogresstracker.model.iteration.Iterations;
import com.example.kiwi.tpprogresstracker.model.m_project.Items;
import com.example.kiwi.tpprogresstracker.model.m_project.Project;
import com.example.kiwi.tpprogresstracker.model.stories.UserStories;
import com.example.kiwi.tpprogresstracker.receiver.NotificationPublisher;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements internalCallback, OnRefreshProject {

    Toolbar mToolbar;
    ViewPager viewPager;
    public static final String MyPreference = "MyPrefs";
    String mToken;
    private DashboardPagerAdapter pagerAdapter;
    FrameLayout flContainer;
    TodayFragment todayFragment;
    private ProgressDialog m_ProgressBar;
    NotificationManager manager;
    Notification myNotication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initViews();
        setSupportActionBar(mToolbar);
        //scheduleNotification(getNotification(), 20000);
//        AlarmManager alarmMgr;
//        alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//
//        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Intent intent = new Intent("com.rj.notitfications.SECACTIVITY");
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.setBigContentTitle("SprintOAlert");
//        //inboxStyle.setSummaryText("(line.length) getBigText()");
//        inboxStyle.addLine("day : 10");
//        inboxStyle.addLine("Bugs not done: 8/15");
//        inboxStyle.addLine("stories not done: 10/20");
//        builder.setAutoCancel(true);
//        builder.setSmallIcon(R.mipmap.kiwi_logo);
//        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder.setStyle(inboxStyle);
//        }
//        builder.setContentIntent(pendingIntent);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder.setPriority(Notification.PRIORITY_HIGH);
//        }
//        builder.setOngoing(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder.build();
//        }
//// Set the alarm to start at 8:30 a.m.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 6);
//        calendar.set(Calendar.MINUTE, 26);
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000 * 20, pendingIntent);
//        myNotication = builder.getNotification();
//        manager.notify(11, myNotication);

        todayFragment = new TodayFragment();
        todayFragment.setSwipeRefreshListener(this);
        getSupportFragmentManager().beginTransaction().add(R.id.flContainer, todayFragment).commit();
        onRefreshList();
    }

    @SuppressLint("ShortAlarm")
    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 10, pendingIntent);
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("SprintOAlert");
        //inboxStyle.setSummaryText("(line.length) getBigText()");
        inboxStyle.addLine("day : 10");
        inboxStyle.addLine("Bugs not done: 8/15");
        inboxStyle.addLine("stories not done: 10/20");
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

    private void fetchProjects() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyPreference, MODE_PRIVATE);
        mToken = sharedPreferences.getString("token", null);
        HashMap<String, String> params = new HashMap<>();
        params.put("take", "1000");
        params.put("where", "(isActive eq 'true')");
        ProjectInfo.listProjectInfo.clear();
        apihandler.getInstance().callAPI("fetchProjects", null, mToken, params, this);
    }


    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //viewPager = (ViewPager) findViewById(R.id.viewPagerImagePicker);
        flContainer = (FrameLayout) findViewById(R.id.flContainer);
    }

    @Override
    public void onSuccess(String url, String requestId, Response response) {
        if (url.equals("Projects")) {
            Project projectResponse = (Project) response.body();
            if (projectResponse.getItems().length == 0) {
                m_ProgressBar.dismiss();
            }
            for (Items item : projectResponse.getItems()) {
                if (item.getIsActive()) {
                    String id = item.getId();
                    String projectName = item.getName();//.split("/")[1];
                    SprintInfo sprintInfo = new SprintInfo();
                    sprintInfo.setProjectId(id);
                    sprintInfo.setProjectName(projectName);
                    ProjectInfo.getInstance().getProjectList().add(sprintInfo);
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("include", "[Id,Name,StartDate,EndDate,IsCurrent,Release,Project]");
                    params.put("take", "1000");
                    params.put("where", "(IsCurrent eq 'true')");
                    params.put("append", "[UserStories-Count,Bugs-Count,UserStories-TimeSpent-Sum,UserStories-Effort-Sum,Bugs-TimeSpent-Sum,Bugs-Effort-Sum]");
                    apihandler.getInstance().callAPI("Iterations", null, mToken, params, this);
                }
            }
        } else if (url.equals("Release")) {
            Log.d("Fetch Projects Activity", "onSuccess: " + response);
            //apihandler.getInstance().callAPI("Release", null, mToken, params, this);
        } else if (url.equals("Iterations")) {
            //RESPONSE TO GET ITERATIONS
            Iterations iterations = (Iterations) response.body();
            if (iterations.getItems().length == 0) {
                m_ProgressBar.dismiss();
                Iterator<SprintInfo> tempList = ProjectInfo.getInstance().getProjectList().iterator();
                while (tempList.hasNext()) {
                    SprintInfo info = tempList.next();
                    if (info.getProjectId().equals(requestId) && info.getSprintName() == null) {
                        tempList.remove();
                    }
                }
            }
            for (Items item : iterations.getItems()) {
                if (item.isCurrent()) {
                    // CHECK WEATHER SPRINT PROJECT ID IS EXIST IN LIST OR NOT
                    for (SprintInfo projItem : ProjectInfo.getInstance().getProjectList()) {
                        if (item.getProject() != null) {
                            if (projItem.getProjectId().equals(item.getProject().getId())) {
                                // SET OTHER PROPERTY VALUES IF EXIST
                                projItem.setSprintId(item.getId());
                                projItem.setSprintName(item.getName());
                                if (item.getRelease() != null) {
                                    projItem.setReleaseId(item.getRelease().getId());
                                    projItem.setReleaseName(item.getRelease().getName());
                                }
                                projItem.setBugsCount(item.getBugsCount());
                                projItem.setStoriesCount(item.getUserStoriesCount());
                                projItem.setStoriesSpentTime(Double.parseDouble(item.getUserStoriesSpentTime()));
                                projItem.setBugsSpentTime(Double.parseDouble(item.getBugsSpentTime()));
                                projItem.setStoriesTotalEffort(Double.parseDouble(item.getUserStoriesTotalEffort()));
                                projItem.setBugsTotalEffort(Double.parseDouble(item.getBugsTotalEffort()));
                                if (item.getStartDate() != null) {
                                    projItem.setSprintStartDate(Long.valueOf(item.getStartDate().split("/Date")[1].split("-")[0].substring(1)));
                                    //int days = getDays(projItem.getSprintStartDate(), System.currentTimeMillis());
                                    Calendar cal1 = toCalendar(projItem.getSprintStartDate());
                                    Calendar cal2 = toCalendar(System.currentTimeMillis());
                                    int numberOfDays = 0;
                                    while (cal1.before(cal2)) {
                                        if ((Calendar.SATURDAY != cal1.get(Calendar.DAY_OF_WEEK))
                                                && (Calendar.SUNDAY != cal1.get(Calendar.DAY_OF_WEEK))) {
                                            numberOfDays++;
                                        }
                                        cal1.add(Calendar.DATE, 1);
                                    }
                                    projItem.setCurrentDay(String.valueOf(numberOfDays));
                                }
                                if (item.getEndDate() != null) {
                                    projItem.setSprintEndDate(Long.valueOf(item.getEndDate().split("/Date")[1].split("-")[0].substring(1)));
                                }

                                Calendar cal1 = toCalendar(projItem.getSprintStartDate());
                                Calendar cal2 = toCalendar(projItem.getSprintEndDate());
                                int numberOfDays = 0;
                                while (cal1.before(cal2)) {
                                    if ((Calendar.SATURDAY != cal1.get(Calendar.DAY_OF_WEEK))
                                            && (Calendar.SUNDAY != cal1.get(Calendar.DAY_OF_WEEK))) {
                                        numberOfDays++;
                                    }
                                    cal1.add(Calendar.DATE, 1);
                                }
                                String totalDaysOfSprint = "/" + String.valueOf(numberOfDays);
                                projItem.setTotalDaysOfSprint(totalDaysOfSprint);

                                HashMap<String, String> storiesParams = new HashMap<>();
                                storiesParams.put("id", item.getId());
                                storiesParams.put("take", "1000");
                                storiesParams.put("include", "[Id,Name,StartDate,EndDate,Release,Project,EntityState]");
                                //storiesParams.put("where", "(EntityState.Name eq 'done')");
                                apihandler.getInstance().callAPI("UserStories", null, mToken, storiesParams, this);

                                HashMap<String, String> bugsParams = new HashMap<>();
                                bugsParams.put("id", item.getId());
                                bugsParams.put("take", "1000");
                                bugsParams.put("include", "[Id,Name,StartDate,EndDate,Release,Project,EntityState]");
                                bugsParams.put("where", "(EntityState.Name eq 'Open')");
                                apihandler.getInstance().callAPI("Bugs", null, mToken, bugsParams, this);

                            }
                        }
                    }
                }
            }

            Log.d("Fetch Projects Activity", "onSuccess Iterations: " + response);
        } else if (url.equals("UserStories")) {
            UserStories result = (UserStories) response.body();
            Log.d("User Story Activity", "onSuccess: " + result);
            if (result.getItems().size() == 0) {
                m_ProgressBar.dismiss();
                for (SprintInfo tempStoryItem : ProjectInfo.getInstance().getProjectList()) {
                    if (tempStoryItem.getSprintId() == null) {
                        tempStoryItem.setSprintId("");
                    }
                    if (tempStoryItem.getSprintId().equals(requestId)) {
                        tempStoryItem.setCardBackgroundColor(Color.parseColor("#E64A19"));
                    }
                    if (tempStoryItem.getStoriesOpen() == 0) {
                        tempStoryItem.setStoriesOpen(0);
                    }
                }
            }
            for (Items item : result.getItems()) {
                int storyDone = 0;
                int storyOpen = 0;
                int storyInTesting = 0;
                int storyInDesign = 0;
                int storyInDevelopment = 0;
                switch (item.getEntityState().getId()) {
                    case "46":
                        storyOpen = 1;
                        break;
                    case "47":
                        storyInDevelopment = 1;
                        break;
                    case "48":
                        storyInTesting = 1;
                        break;
                    case "49":
                        storyDone = 1;
                        break;
                    case "109":
                        storyInDesign = 1;
                        break;
                    default:
                        break;
                }
                for (SprintInfo storyItem : ProjectInfo.getInstance().getProjectList()) {
                    if (item.getProject() != null) {
                        if (storyItem.getProjectId().equals(item.getProject().getId())) {

                            if (storyOpen > 0) {
                                storyItem.setStoriesOpen(storyItem.getStoriesOpen() + 1);
                            } else if (storyInTesting > 0) {
                                storyItem.setStoriesInTesting(storyItem.getStoriesInTesting() + 1);
                            } else if (storyDone > 0) {
                                storyItem.setStoriesDone(storyItem.getStoriesDone() + 1);
                            } else if (storyInDesign > 0) {
                                storyItem.setStoriesInDesign(storyItem.getStoriesInDesign() + 1);
                            } else if (storyInDevelopment > 0) {
                                storyItem.setStoriesInDevelopment(storyItem.getStoriesInDevelopment() + 1);
                            }
                            //Number of days in Sprint
                            int daysInSprint = getDays(storyItem.getSprintStartDate(), storyItem.getSprintEndDate());
                            //Ratio of Stories Done in Days
                            float ratio = Float.parseFloat(storyItem.getStoriesCount()) / daysInSprint;
                            float storiesShouldBeDoneToday = ratio * Float.parseFloat(storyItem.getCurrentDay());
                            if (storyItem.getStoriesOpen() == 0) {
                                storyItem.setStoriesOpen(0);
                            }
                            float totalPercentageOfSprint = (storiesShouldBeDoneToday - (Float.parseFloat(storyItem.getStoriesCount()) - Float.parseFloat(String.valueOf(storyItem.getStoriesOpen())))) * 100 / Float.parseFloat(storyItem.getStoriesCount());
                            Log.d("Sprint Ratio", "onSuccess: " + totalPercentageOfSprint);

                            int workDonePercentage = (int) totalPercentageOfSprint + 100;
                            if (workDonePercentage >= 90) {
                                storyItem.setCardBackgroundColor(Color.parseColor("#388E3C"));
                            } else if (workDonePercentage >= 80 && workDonePercentage < 90) {
                                storyItem.setCardBackgroundColor(Color.parseColor("#FFEB3B"));
                            } else if (workDonePercentage >= 70 && workDonePercentage < 80) {
                                storyItem.setCardBackgroundColor(Color.parseColor("#FF5722"));
                            } else if (workDonePercentage < 70) {
                                storyItem.setCardBackgroundColor(Color.parseColor("#E64A19"));
                            }
                        }
                    }
                }
            }
        } else if (url.equals("Bugs")) {
            StoryBugs result = (StoryBugs) response.body();
            if (result.getItems().size() == 0) {
                m_ProgressBar.dismiss();
                for (SprintInfo tempStoryItem : ProjectInfo.getInstance().getProjectList()) {
                    if (tempStoryItem.getBugsOpen() == 0) {
                        tempStoryItem.setBugsOpen(0);
                    }
                }
            }
            for (Items item : result.getItems()) {
                int bugsDone = 0;
                int bugsOpen = 0;
                int BugsInTesting = 0;
                int BugsInDevelopment = 0;
                switch (item.getEntityState().getId()) {
                    case "53":
                        bugsOpen = 1;
                        break;
                    case "54":
                        BugsInDevelopment = 1;
                        break;
                    case "55":
                        BugsInTesting = 1;
                        break;
                    case "56":
                        bugsDone = 1;
                        break;
                    default:
                        bugsOpen = 1;
                        break;
                }
                for (SprintInfo storyItem : ProjectInfo.getInstance().getProjectList()) {
                    if (item.getProject() != null) {
                        if (storyItem.getProjectId().equals(item.getProject().getId())) {
                            if (bugsOpen > 0) {
                                storyItem.setBugsOpen(storyItem.getBugsOpen() + 1);
                            } else if (BugsInTesting > 0) {
                                storyItem.setBugsInTesting(storyItem.getBugsInTesting() + 1);
                            } else if (bugsDone > 0) {
                                storyItem.setBugsDone(storyItem.getBugsDone() + 1);
                            } else if (BugsInDevelopment > 0) {
                                storyItem.setBugsInDevelopment(storyItem.getBugsInDevelopment() + 1);
                            }

                        }
                    }
                }
                m_ProgressBar.dismiss();
            }
        }
        todayFragment.doWork();
    }

    private int getDays(long startDate, long endDate) {
        Calendar sDate = toCalendar(startDate);
        Calendar eDate = toCalendar(endDate);

        // Get the represented date in milliseconds
        long milis1 = sDate.getTimeInMillis();
        long milis2 = eDate.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = Math.abs(milis2 - milis1);

        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    @Override
    public void onFaliure(String url, String id, Response response) {
        Log.d("Fetch project Activity", "onFaliure: " + response);
    }

    private Calendar toCalendar(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    @Override
    public void onRefreshList() {
        m_ProgressBar = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        m_ProgressBar.setCancelable(false);
        m_ProgressBar.setMessage("Please wait...");
        m_ProgressBar.show();
        fetchProjects();
    }
}
