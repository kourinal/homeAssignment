package com.example.homeassignment.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.homeassignment.data.api.MovieDbInterface;
import com.example.homeassignment.data.vo.MovieDescription;
import com.example.homeassignment.data.vo.MovieDetails;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDataSource extends PageKeyedDataSource<Integer, MovieDescription> {

    private CompositeDisposable mMompositeDisposable;
    private MovieDbInterface apiService;
    private MutableLiveData<NetworkState> mNetworkState = new MutableLiveData();
    private int page = 1;

    public MovieDataSource(MovieDbInterface apiService, CompositeDisposable compositeDisposable){
        this.apiService = apiService;
        mMompositeDisposable = compositeDisposable;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MovieDescription> callback) {
        mNetworkState.postValue(NetworkState.LOADING);

        mMompositeDisposable.add(
                apiService.getPopular(page)
                        .subscribeOn(Schedulers.io())
                        .subscribe(res-> {
                                        callback.onResult(res.getMovieList(), null, page+1);
                                        mNetworkState.postValue(NetworkState.LOADED);
                                },
                                err->{
                                        mNetworkState.postValue(NetworkState.FAILED);
                                        Log.e("MovieDataSource", err.getMessage());
                                }
                        )
        );
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieDescription> callback) {
        mNetworkState.postValue(NetworkState.LOADING);

        mMompositeDisposable.add(
                apiService.getPopular(params.key)
                        .subscribeOn(Schedulers.io())
                        .subscribe( res-> {
        if(res.getTotal_pages() >= params.key) {
            callback.onResult(res.getMovieList(), params.key+1);
            mNetworkState.postValue(NetworkState.LOADED);
        }
        else{
            mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, "You have reached the end"));
        }
        },
        err -> {
            mNetworkState.postValue(NetworkState.FAILED);
            Log.e("MovieDataSource", err.getMessage());
        })
        );

    }

    public MutableLiveData<NetworkState> getNetworkState(){
        return mNetworkState;
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieDescription> callback) {

    }
}
