package com.example.kiwi.tpprogresstracker.interfaces;

import com.example.kiwi.tpprogresstracker.model.bugs.StoryBugs;
import com.example.kiwi.tpprogresstracker.model.iteration.Iterations;
import com.example.kiwi.tpprogresstracker.model.m_project.Project;
import com.example.kiwi.tpprogresstracker.model.stories.UserStories;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kiwi on 9/20/2016.
 */
public interface apiManger {

    @GET("Authentication")
    Call<ResponseBody> authentication(@Header("Authorization") String authentication, @Query("format") String format);

    @GET("Projects")
    Call<Project> fetchProjects(@Query("token") String token, @Query("format") String format, @Query("take") String take, @Query("where") String where);

    @GET("Projects/{id}/Releases")
    Call<ResponseBody> fetchProjectRelease(@Path("id") String id, @Query("token") String token, @Query("format") String format);

    @GET("Projects/{id}/Iterations")
    Call<Iterations> fetchProjectIterations(@Path("id") String id, @Query("token") String token, @Query("format") String format, @Query("include") String include, @Query("take") String take, @Query("where") String where, @Query("append") String append);

    @GET("Iterations/{id}/UserStories")
    Call<UserStories> fetchIterationsStories(@Path("id") String id, @Query("token") String token, @Query("format") String format, @Query("include") String include, @Query("take") String take);

    @GET("Iterations/{id}/Bugs")
    Call<StoryBugs> fetchIterationsBugs(@Path("id") String id, @Query("token") String token, @Query("format") String format, @Query("include") String include, @Query("take") String take);
}
