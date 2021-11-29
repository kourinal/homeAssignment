package com.example.homeassignment.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.homeassignment.data.api.MovieDbInterface;
import com.example.homeassignment.data.vo.MovieDetails;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieNetworkDetailsDataSource {
    private MovieDbInterface apiService;
    private CompositeDisposable mCompositeDisposable;
    public LiveData<MovieDetails> mDownloadedMovieResponse;
    private MutableLiveData<NetworkState> mNetworkState = new MutableLiveData<>();
    private MutableLiveData<MovieDetails> mDownloadedMovieDetails = new MutableLiveData<>();

    public MovieNetworkDetailsDataSource(MovieDbInterface movieDBInterface, CompositeDisposable compositeDisposable){
        this.apiService = movieDBInterface;
        this.mCompositeDisposable = compositeDisposable;
    }



    public void fetchMovieDetails(int id){
        mNetworkState.postValue(new NetworkState(NetworkState.Status.RUNNING, "Running"));
        try{
            mCompositeDisposable.add(
                    apiService.getMovieDetails(id)
                            .subscribeOn(Schedulers.io())
                            .subscribe(res->{
                                    mDownloadedMovieDetails.postValue(res);
                                    mNetworkState.postValue(new NetworkState(NetworkState.Status.SUCCESS, "Success"));
                                }, err->{
                                    mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, "Failed"));
                                    Log.e("MovieDetailsDataSource",err.getMessage());
                                }
                            ));
        }
        catch (Exception e ){
            Log.e("MovieDetailsDataSource",e.getMessage());
        }
    }

   public  MutableLiveData<NetworkState> getNetworkState(){
        return mNetworkState;
    }



    public MutableLiveData<MovieDetails> getDownloadedMovieDetails(){
        return mDownloadedMovieDetails;
    }

}
