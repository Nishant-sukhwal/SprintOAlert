package com.example.kiwi.tpprogresstracker.httpmanager;

import android.util.Log;

import com.example.kiwi.tpprogresstracker.classes.constants;
import com.example.kiwi.tpprogresstracker.interfaces.internalCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kiwi on 9/20/2016.
 */
public class apiCallback implements Callback {

    public static apiCallback instance;
    private internalCallback mContext;

    public apiCallback() {

    }

    public static apiCallback getInstance() {
        if (instance == null) {
            instance = new apiCallback();
        }
        return instance;
    }

    public void setCallback(internalCallback context) {
        mContext = context;
    }

    @Override
    public void onResponse(Call call, Response response) {
        String customURL = call.request().url().toString().split(constants.SERVER_URL)[1];
        customURL = customURL.split("\\?")[0];
        String id = null;
        if (customURL.contains("Releases")) {
            customURL = "Release";
        } else if (customURL.contains("Iterations") && customURL.contains("Bugs")) {
            id = customURL.split("/")[1];
            customURL = "Bugs";
        } else if (customURL.contains("Iterations") && customURL.contains("UserStories")) {
            id = customURL.split("/")[1];
            customURL = "UserStories";
        } else if (customURL.contains("Iterations")) {
            id = customURL.split("/")[1];
            customURL = "Iterations";
        }
        if (mContext != null) {
            mContext.onSuccess(customURL, id, response);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {
        Log.d("Failure Activity", "onFailure: " + t.getMessage());
    }
}
