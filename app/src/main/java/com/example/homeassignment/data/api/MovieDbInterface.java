package com.example.homeassignment.data.api;

import com.example.homeassignment.data.vo.Movie;
import com.example.homeassignment.data.vo.MovieDetails;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDbInterface {

    // https://api.themoviedb.org/3/movie/popular?api_key=6e63c2317fbe963d76c3bdc2b785f6d1&page=1
    // https://api.themoviedb.org/3/movie/299534?api_key=6e63c2317fbe963d76c3bdc2b785f6d1
    // https://api.themoviedb.org/3/



    @GET("movie/popular")
    Single<Movie> getPopular(@Query("page") int page);

    @GET("movie/{movie_id}")
    Single<MovieDetails> getMovieDetails(@Path("movie_id")int id);//returns Observeable Single
}
