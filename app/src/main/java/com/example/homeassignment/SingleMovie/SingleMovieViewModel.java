package com.example.homeassignment.SingleMovie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.homeassignment.data.repository.NetworkState;
import com.example.homeassignment.data.vo.MovieDetails;

import io.reactivex.disposables.CompositeDisposable;

public class SingleMovieViewModel extends ViewModel {


    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private int mId;
    MovieDetailsRepository mMovieDetailsRepository;
    LiveData<MovieDetails> mMovieDetails;
    LiveData<NetworkState> mNetworkState;
    public SingleMovieViewModel(int id, MovieDetailsRepository movieDetailRepository){
        mId = id;
        mMovieDetailsRepository = movieDetailRepository;
        mMovieDetails = mMovieDetailsRepository.fetchSingleMovieDetails(mId,compositeDisposable);
        mNetworkState = movieDetailRepository.getMovieDetailsNetworkState();

    }

    @Override
    public void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

}
