package com.example.homeassignment.SingleMovie;

import androidx.lifecycle.LiveData;

import com.example.homeassignment.data.api.MovieDbInterface;
import com.example.homeassignment.data.repository.MovieNetworkDetailsDataSource;
import com.example.homeassignment.data.repository.NetworkState;
import com.example.homeassignment.data.vo.MovieDetails;

import io.reactivex.disposables.CompositeDisposable;

public class MovieDetailsRepository {
    private MovieDbInterface apiService;
    private MovieNetworkDetailsDataSource mMovieNetworkDetailsDataSource;

    public MovieDetailsRepository(MovieDbInterface movieDbInterface){
        apiService = movieDbInterface;
    }

    LiveData<MovieDetails> fetchSingleMovieDetails(int id, CompositeDisposable compositeDisposable){
        mMovieNetworkDetailsDataSource = new MovieNetworkDetailsDataSource(apiService,compositeDisposable);
        mMovieNetworkDetailsDataSource.fetchMovieDetails(id);

        return mMovieNetworkDetailsDataSource.getDownloadedMovieDetails();
    }

    LiveData<NetworkState> getMovieDetailsNetworkState(){
        return mMovieNetworkDetailsDataSource.getNetworkState();
    }



}
