package com.example.kiwi.tpprogresstracker.model.m_project;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kiwi on 9/23/2016.
 */

public class Items {
    private String PlannedStartDate;

    private String Effort;

    private String CreateDate;

    private String IsPrivate;

    private String ModifyDate;

    private String EffortToDo;

    private String ResourceType;

    private String StartDate;

    private boolean IsActive;

    private Process Process;

    private Project Project;

    private String Company;

    private Program Program;

    private String[] CustomFields;

    private Owner Owner;

    private String Description;

    private String MailReplyAddress;

    private EntityState EntityState;

    private String LastCommentDate;

    private String IsProduct;

    private String LastCommentedUser;

    private String EffortCompleted;

    private String PlannedEndDate;

    private String NumericPriority;

    private String Progress;

    private String Color;

    private String Name;

    private String Tags;

    private String LinkedTestPlan;

    private String EndDate;

    private EntityType EntityType;

    private boolean IsCurrent;

    private Release Release;

    private String Id;

    private String Abbreviation;

    @SerializedName("UserStories-Count")
    private String UserStoriesCount;

    @SerializedName("Bugs-Count")
    private String BugsCount;

    @SerializedName("UserStories-TimeSpent-Sum")
    private String UserStoriesSpentTime;

    @SerializedName("UserStories-Effort-Sum")
    private String UserStoriesTotalEffort;

    @SerializedName("Bugs-TimeSpent-Sum")
    private String BugsSpentTime;

    @SerializedName("Bugs-Effort-Sum")
    private String BugsTotalEffort;

    public String getPlannedStartDate() {
        return PlannedStartDate;
    }

    public void setPlannedStartDate(String PlannedStartDate) {
        this.PlannedStartDate = PlannedStartDate;
    }

    public String getEffort() {
        return Effort;
    }

    public void setEffort(String Effort) {
        this.Effort = Effort;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public String getIsPrivate() {
        return IsPrivate;
    }

    public void setIsPrivate(String IsPrivate) {
        this.IsPrivate = IsPrivate;
    }

    public String getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(String ModifyDate) {
        this.ModifyDate = ModifyDate;
    }

    public String getEffortToDo() {
        return EffortToDo;
    }

    public void setEffortToDo(String EffortToDo) {
        this.EffortToDo = EffortToDo;
    }

    public String getResourceType() {
        return ResourceType;
    }

    public void setResourceType(String ResourceType) {
        this.ResourceType = ResourceType;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(boolean IsActive) {
        this.IsActive = IsActive;
    }

    public Process getProcess() {
        return Process;
    }

    public void setProcess(Process Process) {
        this.Process = Process;
    }

    public Project getProject() {
        return Project;
    }

    public void setProject(Project Project) {
        this.Project = Project;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public Program getProgram() {
        return Program;
    }

    public void setProgram(Program Program) {
        this.Program = Program;
    }

    public String[] getCustomFields() {
        return CustomFields;
    }

    public void setCustomFields(String[] CustomFields) {
        this.CustomFields = CustomFields;
    }

    public Owner getOwner() {
        return Owner;
    }

    public void setOwner(Owner Owner) {
        this.Owner = Owner;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getMailReplyAddress() {
        return MailReplyAddress;
    }

    public void setMailReplyAddress(String MailReplyAddress) {
        this.MailReplyAddress = MailReplyAddress;
    }

    public EntityState getEntityState() {
        return EntityState;
    }

    public void setEntityState(EntityState EntityState) {
        this.EntityState = EntityState;
    }

    public String getLastCommentDate() {
        return LastCommentDate;
    }

    public void setLastCommentDate(String LastCommentDate) {
        this.LastCommentDate = LastCommentDate;
    }

    public String getIsProduct() {
        return IsProduct;
    }

    public void setIsProduct(String IsProduct) {
        this.IsProduct = IsProduct;
    }

    public String getLastCommentedUser() {
        return LastCommentedUser;
    }

    public void setLastCommentedUser(String LastCommentedUser) {
        this.LastCommentedUser = LastCommentedUser;
    }

    public String getEffortCompleted() {
        return EffortCompleted;
    }

    public void setEffortCompleted(String EffortCompleted) {
        this.EffortCompleted = EffortCompleted;
    }

    public String getPlannedEndDate() {
        return PlannedEndDate;
    }

    public void setPlannedEndDate(String PlannedEndDate) {
        this.PlannedEndDate = PlannedEndDate;
    }

    public String getNumericPriority() {
        return NumericPriority;
    }

    public void setNumericPriority(String NumericPriority) {
        this.NumericPriority = NumericPriority;
    }

    public String getProgress() {
        return Progress;
    }

    public void setProgress(String Progress) {
        this.Progress = Progress;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String Color) {
        this.Color = Color;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String Tags) {
        this.Tags = Tags;
    }

    public String getLinkedTestPlan() {
        return LinkedTestPlan;
    }

    public void setLinkedTestPlan(String LinkedTestPlan) {
        this.LinkedTestPlan = LinkedTestPlan;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public EntityType getEntityType() {
        return EntityType;
    }

    public void setEntityType(EntityType EntityType) {
        this.EntityType = EntityType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getAbbreviation() {
        return Abbreviation;
    }

    public void setAbbreviation(String Abbreviation) {
        this.Abbreviation = Abbreviation;
    }

    public com.example.kiwi.tpprogresstracker.model.m_project.Release getRelease() {
        return Release;
    }

    public void setRelease(com.example.kiwi.tpprogresstracker.model.m_project.Release release) {
        Release = release;
    }

    public boolean isCurrent() {
        return IsCurrent;
    }

    public void setCurrent(boolean current) {
        IsCurrent = current;
    }

    public String getUserStoriesCount() {
        return UserStoriesCount;
    }

    public void setUserStoriesCount(String userStoriesCount) {
        UserStoriesCount = userStoriesCount;
    }

    public String getBugsCount() {
        return BugsCount;
    }

    public void setBugsCount(String bugsCount) {
        BugsCount = bugsCount;
    }

    public String getUserStoriesSpentTime() {
        return UserStoriesSpentTime;
    }

    public void setUserStoriesSpentTime(String userStoriesSpentTime) {
        UserStoriesSpentTime = userStoriesSpentTime;
    }

    public String getUserStoriesTotalEffort() {
        return UserStoriesTotalEffort;
    }

    public void setUserStoriesTotalEffort(String userStoriesTotalEffort) {
        UserStoriesTotalEffort = userStoriesTotalEffort;
    }

    public String getBugsSpentTime() {
        return BugsSpentTime;
    }

    public void setBugsSpentTime(String bugsSpentTime) {
        BugsSpentTime = bugsSpentTime;
    }

    public String getBugsTotalEffort() {
        return BugsTotalEffort;
    }

    public void setBugsTotalEffort(String bugsTotalEffort) {
        BugsTotalEffort = bugsTotalEffort;
    }
}