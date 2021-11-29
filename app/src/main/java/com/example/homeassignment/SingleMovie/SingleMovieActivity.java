package com.example.homeassignment.SingleMovie;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.homeassignment.data.api.MovieDbClient;
import com.example.homeassignment.data.api.MovieDbInterface;
import com.example.homeassignment.data.repository.NetworkState;
import com.example.homeassignment.data.vo.MovieDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.homeassignment.R;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Observable;
import androidx.lifecycle.Observer;

import org.w3c.dom.Text;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SingleMovieActivity extends AppCompatActivity {

    private SingleMovieViewModel viewModel;
    private MovieDetailsRepository mMovieDetailsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie);

        int movieID = getIntent().getIntExtra("id", 3);
        MovieDbInterface movieDBInterface = MovieDbClient.getClient();


        mMovieDetailsRepository = new MovieDetailsRepository(movieDBInterface);

        viewModel = new SingleMovieViewModel(movieID, mMovieDetailsRepository);

        viewModel.mMovieDetails.observe(this, new Observer<MovieDetails>() {
            @Override
            public void onChanged(MovieDetails movieDetails) {
                bindUI(movieDetails);
            }
        });

        viewModel.mNetworkState.observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress_bar);
                TextView txtError = (TextView) findViewById(R.id.txt_error);

                //TODO This is silly change this
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


    }

    private void bindUI(MovieDetails res){
        TextView textTitle = (TextView) findViewById(R.id.movie_title);
        textTitle.setText(res.getTitle());
        TextView textTag = (TextView) findViewById(R.id.movie_tagline);
        textTag.setText(res.getTagline());
        TextView txtDate = (TextView) findViewById(R.id.movie_release_date);
        txtDate.setText(res.getRelease_date());
        TextView txtRating = (TextView) findViewById(R.id.movie_rating);
        txtRating.setText(Float.toString(res.getVote_average()));
        TextView txtRuntime = (TextView) findViewById(R.id.movie_runtime);
        txtRuntime.setText(Float.toString(res.getRuntime()) +" Minutes");
        TextView txtOverview = (TextView) findViewById(R.id.movie_overview);
        txtOverview.setText(res.getOverview());

        Locale locale = new Locale("en", "US");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        TextView txtBudget = (TextView) findViewById(R.id.movie_budget);
        txtBudget.setText(currencyFormatter.format(res.getBudget()));
        TextView txtRevenue = (TextView) findViewById(R.id.movie_revenue);
        txtRevenue.setText(currencyFormatter.format(res.getRevenue()));
        ImageView posterView = (ImageView) findViewById(R.id.iv_movie_poster);
        String moviePosterURL = MovieDbClient.getPosterBaseUrl() + res.getPoster_path();
        Glide.with(this)
                .load(moviePosterURL)
                .into(posterView);
    }

}