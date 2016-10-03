package com.example.kiwi.tpprogresstracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kiwi on 9/20/2016.
 */
public class authenticationResponse {
    @SerializedName("Token")
    @Expose
    private String Token;

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
