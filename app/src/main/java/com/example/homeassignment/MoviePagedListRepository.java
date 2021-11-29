package com.example.homeassignment;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;

import com.example.homeassignment.data.api.MovieDbInterface;
import com.example.homeassignment.data.repository.MovieDataSourceFactory;
import com.example.homeassignment.data.repository.NetworkState;
import com.example.homeassignment.data.repository.MovieDataSource;
import com.example.homeassignment.data.vo.MovieDescription;

import androidx.paging.LivePagedListBuilder;

import io.reactivex.disposables.CompositeDisposable;

public class MoviePagedListRepository {
    LiveData<PagedList<MovieDescription>> moviePagedList;
    MovieDataSourceFactory movieDataSourceFactory;
    MovieDbInterface apiService;
    public MoviePagedListRepository(MovieDbInterface apiService){
        this.apiService = apiService;
    }

    public LiveData<PagedList<MovieDescription>> fetchLiveMoviePagedList (CompositeDisposable compositeDisposable){
        movieDataSourceFactory = new MovieDataSourceFactory(apiService, compositeDisposable);

        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(8).build();

        moviePagedList =new LivePagedListBuilder(movieDataSourceFactory, config).build();

        return moviePagedList;
    }

    LiveData<NetworkState> getNetworkState(){
        LiveData<NetworkState> liveData;
        liveData = Transformations.switchMap(movieDataSourceFactory.mMovieListDataSource, MovieDataSource::getNetworkState);
        return liveData;
    }

}
