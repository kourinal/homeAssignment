package com.example.homeassignment;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.example.homeassignment.data.api.MovieDbInterface;
import com.example.homeassignment.data.repository.MovieDataSourceFactory;
import com.example.homeassignment.data.vo.Movie;
import androidx.paging.LivePagedListBuilder;
import io.reactivex.disposables.CompositeDisposable;

public class MoviePagedListRepository {
    LiveData<PagedList<Movie>> moviePagedList;
    MovieDataSourceFactory movieDataSourceFactory;
    MovieDbInterface apiService;
    final int PAGE_SIZE = 8;
    public MoviePagedListRepository(MovieDbInterface apiService){
        this.apiService = apiService;
    }

    public LiveData<PagedList<Movie>> fetchLiveMoviePagedList (CompositeDisposable compositeDisposable){
        MovieDataSourceFactory moviesDataSourceFactory = new MovieDataSourceFactory(apiService, compositeDisposable);

        PagedList.Config config = new PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(PAGE_SIZE).build();

        moviePagedList =new LivePagedListBuilder(moviesDataSourceFactory, config).build();

        return moviePagedList;
    }

}
