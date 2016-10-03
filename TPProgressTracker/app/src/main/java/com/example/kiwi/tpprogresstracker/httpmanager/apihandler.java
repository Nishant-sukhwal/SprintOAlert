package com.example.kiwi.tpprogresstracker.httpmanager;

import com.example.kiwi.tpprogresstracker.classes.constants;
import com.example.kiwi.tpprogresstracker.interfaces.apiManger;
import com.example.kiwi.tpprogresstracker.interfaces.internalCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kiwi on 9/20/2016.
 */
public class apihandler {
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(constants.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    apiManger service = retrofit.create(apiManger.class);

    public static apihandler instance;

    public apihandler() {

    }

    public static apihandler getInstance() {
        if (instance == null) {
            instance = new apihandler();
        }
        return instance;
    }

    public void callAPI(String url, Object jsonObj, String params, HashMap<String, String> queryParams, internalCallback context) {
        apiCallback callback = apiCallback.getInstance();
        callback.setCallback(context);
        Call call = null;
        if (url.equals("Authentication")) {
            call = service.authentication("Basic " + params, "json");
        } else if (url.equals("fetchProjects")) {
            String take = queryParams.get("take");
            String where = queryParams.get("where");
            call = service.fetchProjects(params, "json", take, where);
        } else if (url.equals("Release")) {
            String id = queryParams.get("id");
            call = service.fetchProjectRelease(id, params, "json");
        } else if (url.equals("Iterations")) {
            String id = queryParams.get("id");
            String include = queryParams.get("include");
            String take = queryParams.get("take");
            String where = queryParams.get("where");
            String append = queryParams.get("append");
            call = service.fetchProjectIterations(id, params, "json", include, take, where, append);
        } else if (url.equals("UserStories")) {
            String id = queryParams.get("id");
            String include = queryParams.get("include");
            String take = queryParams.get("take");
            //String where = queryParams.get("where");
            call = service.fetchIterationsStories(id, params, "json", include, take);
        } else if (url.equals("Bugs")) {
            String id = queryParams.get("id");
            String include = queryParams.get("include");
            String take = queryParams.get("take");
            call = service.fetchIterationsBugs(id, params, "json", include, take);
        }
        call.enqueue(callback);
    }
}
