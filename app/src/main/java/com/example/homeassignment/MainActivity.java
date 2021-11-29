package com.example.homeassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.homeassignment.data.api.MovieDbClient;
import com.example.homeassignment.data.api.MovieDbInterface;
import com.example.homeassignment.data.repository.NetworkState;
import com.example.homeassignment.data.vo.MovieDescription;

public class MainActivity extends AppCompatActivity {

    MainActivityViewModel viewModel;

    MoviePagedListRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MovieDbInterface apiService = MovieDbClient.getClient();

        movieRepository = new MoviePagedListRepository(apiService);

        viewModel = new MainActivityViewModel(movieRepository);

        PopularMoviePagedListAdapter movieAdapter = new PopularMoviePagedListAdapter(new PopularMoviePagedListAdapter.MovieDiffCallback(), this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = movieAdapter.getItemViewType(position);
                if (viewType == (movieAdapter.MOVIE_VIEW_TYPE))
                    return 1;
                else
                    return 3;
            }

        });

        RecyclerView movieList = findViewById(R.id.rv_movie_list);
        movieList.setLayoutManager(gridLayoutManager);
        movieList.setHasFixedSize(true);
        movieList.setAdapter(movieAdapter);

        viewModel.moviePagedList.observe(this, new Observer<PagedList<MovieDescription>>() {
            @Override
            public void onChanged(PagedList<MovieDescription> movieDetails) {
                movieAdapter.submitList(movieDetails);
            }
        });

        viewModel.networkSrate.observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress_bar_popular);
                TextView txtError = (TextView) findViewById(R.id.txt_error_popular);

                switch(networkState.getStatus()){
                    case FAILED:
                        txtError.setVisibility(View.VISIBLE);
                        break;
                    case RUNNING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressBar.setVisibility(View.GONE);
                        txtError.setVisibility(View.GONE);
                        break;
                }

            }
        });

        /*viewModel.networkSrate.observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_popular);

                if (viewModel.listIsEmpty() && networkState == NetworkState.LOADING)
                    progressBar.setVisibility(View.VISIBLE);
                else
                    progressBar.setVisibility(View.GONE);
                TextView txtError = (TextView) findViewById(R.id.txt_error_popular);

                if (viewModel.listIsEmpty() && networkState == NetworkState.FAILED)
                    txtError.setVisibility(View.VISIBLE);
                else
                    txtError.setVisibility(View.GONE);

                if (!viewModel.listIsEmpty())
                    movieAdapter.setNetworkState(networkState);
            }
        });*/
    }
}