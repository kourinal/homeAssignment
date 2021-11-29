package com.example.homeassignment;

import com.example.homeassignment.data.repository.NetworkState;
import com.example.homeassignment.data.vo.MovieDescription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;
import io.reactivex.disposables.CompositeDisposable;

class MainActivityViewModel extends ViewModel {
    LiveData<NetworkState> networkSrate;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    LiveData<PagedList<MovieDescription>> moviePagedList;
    MainActivityViewModel(MoviePagedListRepository movieRepository){
        moviePagedList = movieRepository.fetchLiveMoviePagedList(compositeDisposable);
        networkSrate = movieRepository.getNetworkState();
    }

    public boolean listIsEmpty() {
        if (moviePagedList==null || moviePagedList.getValue()==null) return false;
        return moviePagedList.getValue().isEmpty() ? true : false;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
