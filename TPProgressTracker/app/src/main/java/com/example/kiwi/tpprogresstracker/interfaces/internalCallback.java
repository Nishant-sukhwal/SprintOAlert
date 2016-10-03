package com.example.kiwi.tpprogresstracker.interfaces;

import retrofit2.Response;

/**
 * Created by kiwi on 9/20/2016.
 */
public interface internalCallback {
    public void onSuccess(String url, String id, Response response);

    public void onFaliure(String url, String id, Response response);
}
