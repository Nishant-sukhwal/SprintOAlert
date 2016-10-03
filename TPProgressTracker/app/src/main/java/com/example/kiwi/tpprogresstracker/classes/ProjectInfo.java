package com.example.kiwi.tpprogresstracker.classes;

import com.example.kiwi.tpprogresstracker.model.SprintInfo;

import java.util.ArrayList;

/**
 * Created by kiwi on 9/26/2016.
 */
public class ProjectInfo {
    private static ProjectInfo instance = new ProjectInfo();
    public static ArrayList<SprintInfo> listProjectInfo = new ArrayList<SprintInfo>();

    public static ProjectInfo getInstance() {
        if (instance == null) {
            instance = new ProjectInfo();
        }
        return instance;
    }

    private ProjectInfo() {
    }

    public ArrayList<SprintInfo> getProjectList(){
        return listProjectInfo;
    }


}
