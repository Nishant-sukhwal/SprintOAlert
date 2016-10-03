package com.example.kiwi.tpprogresstracker.model;

/**
 * Created by kiwi on 9/26/2016.
 */
public class SprintInfo {
    private String ProjectId;
    private String ProjectName;
    private String ReleaseId;
    private String ReleaseName;
    private String SprintId;
    private String SprintName;
    private Long SprintStartDate;
    private Long SprintEndDate;
    private boolean SprintIsCurrent;
    private String StoriesCount;
    private int StoriesOpen;
    private int StoriesDone;
    private int StoriesInTesting;
    private int StoriesInDesign;
    private int StoriesInDevelopment;
    private String BugsCount;
    private int BugsOpen;
    private int BugsDone;
    private int BugsInTesting;
    private int BugsInDevelopment;
    private String CurrentDay;
    private String TotalDaysOfSprint;
    private int CardBackgroundColor;

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getSprintId() {
        return SprintId;
    }

    public void setSprintId(String sprintId) {
        SprintId = sprintId;
    }

    public String getSprintName() {
        return SprintName;
    }

    public void setSprintName(String sprintName) {
        SprintName = sprintName;
    }

    public String getStoriesCount() {
        return StoriesCount;
    }

    public void setStoriesCount(String storiesCount) {
        StoriesCount = storiesCount;
    }

    public String getBugsCount() {
        return BugsCount;
    }

    public void setBugsCount(String bugsCount) {
        BugsCount = bugsCount;
    }

    public Long getSprintStartDate() {
        return SprintStartDate;
    }

    public void setSprintStartDate(Long sprintStartDate) {
        SprintStartDate = sprintStartDate;
    }

    public Long getSprintEndDate() {
        return SprintEndDate;
    }

    public void setSprintEndDate(Long sprintEndDate) {
        SprintEndDate = sprintEndDate;
    }

    public boolean isSprintIsCurrent() {
        return SprintIsCurrent;
    }

    public void setSprintIsCurrent(boolean sprintIsCurrent) {
        SprintIsCurrent = sprintIsCurrent;
    }

    public String getReleaseId() {
        return ReleaseId;
    }

    public void setReleaseId(String releaseId) {
        ReleaseId = releaseId;
    }

    public String getReleaseName() {
        return ReleaseName;
    }

    public void setReleaseName(String releaseName) {
        ReleaseName = releaseName;
    }

    public int getStoriesOpen() {
        return StoriesOpen;
    }

    public void setStoriesOpen(int storiesOpen) {
        StoriesOpen = storiesOpen;
    }

    public int getBugsOpen() {
        return BugsOpen;
    }

    public void setBugsOpen(int bugsOpen) {
        BugsOpen = bugsOpen;
    }

    public String getCurrentDay() {
        return CurrentDay;
    }

    public void setCurrentDay(String currentDay) {
        CurrentDay = currentDay;
    }

    public int getCardBackgroundColor() {
        return CardBackgroundColor;
    }

    public void setCardBackgroundColor(int cardBackgroundColor) {
        CardBackgroundColor = cardBackgroundColor;
    }

    public int getStoriesInTesting() {
        return StoriesInTesting;
    }

    public void setStoriesInTesting(int storiesInTesting) {
        StoriesInTesting = storiesInTesting;
    }

    public int getStoriesInDesign() {
        return StoriesInDesign;
    }

    public void setStoriesInDesign(int storiesInDesign) {
        StoriesInDesign = storiesInDesign;
    }

    public int getStoriesDone() {
        return StoriesDone;
    }

    public void setStoriesDone(int storiesDone) {
        StoriesDone = storiesDone;
    }

    public int getStoriesInDevelopment() {
        return StoriesInDevelopment;
    }

    public void setStoriesInDevelopment(int storiesInDevelopment) {
        StoriesInDevelopment = storiesInDevelopment;
    }

    public int getBugsDone() {
        return BugsDone;
    }

    public void setBugsDone(int bugsDone) {
        BugsDone = bugsDone;
    }

    public int getBugsInTesting() {
        return BugsInTesting;
    }

    public void setBugsInTesting(int bugsInTesting) {
        BugsInTesting = bugsInTesting;
    }

    public int getBugsInDevelopment() {
        return BugsInDevelopment;
    }

    public void setBugsInDevelopment(int bugsInDevelopment) {
        BugsInDevelopment = bugsInDevelopment;
    }

    public String getTotalDaysOfSprint() {
        return TotalDaysOfSprint;
    }

    public void setTotalDaysOfSprint(String totalDaysOfSprint) {
        TotalDaysOfSprint = totalDaysOfSprint;
    }
}
