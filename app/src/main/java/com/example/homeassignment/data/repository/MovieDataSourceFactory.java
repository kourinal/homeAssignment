package com.example.homeassignment.data.repository;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.homeassignment.data.api.MovieDbInterface;
import com.example.homeassignment.data.vo.Movie;

import io.reactivex.disposables.CompositeDisposable;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie>{
    public MutableLiveData<MovieDataSource> mMovieListDataSource = new MutableLiveData<>();
    MovieDbInterface apiService;
    CompositeDisposable mCompositeDisposable;

    public MovieDataSourceFactory(MovieDbInterface apiService, CompositeDisposable compositeDisposable){
        this.apiService = apiService;
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public DataSource<Integer, Movie> create(){
        MovieDataSource movieDataSource = new MovieDataSource(apiService,mCompositeDisposable);
        mMovieListDataSource.postValue(movieDataSource);
        return (DataSource)movieDataSource;
    }
}
