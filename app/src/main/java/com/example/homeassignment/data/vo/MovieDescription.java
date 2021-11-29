package com.example.homeassignment.data.vo;

import com.google.gson.annotations.SerializedName;

public class MovieDescription {
    private int id;
    @SerializedName("poster_path")
    private String poster_path;

    public int getId() {
        return id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    @SerializedName("release_date")
    private String release_date;
    private String title;
}
