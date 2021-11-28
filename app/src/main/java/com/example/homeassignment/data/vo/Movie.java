package com.example.homeassignment.data.vo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movie {
    private float page;
    @SerializedName("results")
    ArrayList<MovieDetails> results = new ArrayList <MovieDetails>();
    @SerializedName("total_pages")
    private float total_pages;
    @SerializedName("total_results")
    private float total_results;


    // Getter Methods

    public ArrayList<MovieDetails> getMovieList(){
        return results;
    }

    public float getPage() {
        return page;
    }

    public float getTotal_pages() {
        return total_pages;
    }

    public float getTotal_results() {
        return total_results;
    }

    // Setter Methods

    public void setPage(float page) {
        this.page = page;
    }

    public void setTotal_pages(float total_pages) {
        this.total_pages = total_pages;
    }

    public void setTotal_results(float total_results) {
        this.total_results = total_results;
    }
}
