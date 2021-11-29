package com.example.homeassignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.homeassignment.SingleMovie.SingleMovieActivity;
import com.example.homeassignment.data.api.MovieDbClient;
import com.example.homeassignment.data.repository.NetworkState;
import com.example.homeassignment.data.vo.MovieDescription;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

class PopularMoviePagedListAdapter extends PagedListAdapter<MovieDescription, RecyclerView.ViewHolder> {

    private NetworkState networkState;
    final int MOVIE_VIEW_TYPE = 1;
    final int NETWORK_VIEW_TYPE = 2;
    Context mMcontext;

    protected PopularMoviePagedListAdapter(MovieDiffCallback diffCallback, Context context) {
        super(diffCallback);

        mMcontext = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false);
            return new MovieItemViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false);
            return new NetworkStateItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            ((MovieItemViewHolder) holder).bind(getItem(position), mMcontext);
        } else {
            ((NetworkStateItemViewHolder) holder).bind(networkState);
        }
    }


    public Boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    public int getItemCount() {
        int itemCount = super.getItemCount();

        if (hasExtraRow())
            return itemCount + 1;
        else
            return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return NETWORK_VIEW_TYPE;
        } else {
            return MOVIE_VIEW_TYPE;
        }
    }


    static class MovieDiffCallback extends DiffUtil.ItemCallback<MovieDescription> {

        @Override
        public boolean areItemsTheSame(@NonNull MovieDescription oldItem, @NonNull MovieDescription newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieDescription oldItem, @NonNull MovieDescription newItem) {
            if (oldItem.getId() == newItem.getId())
                return true;
            return false;
        }
    }


    class MovieItemViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        public MovieItemViewHolder(View view) {
            super(view);
            itemView = view;
        }

        public void bind(MovieDescription movie, Context context) {
            TextView txtTitle = (TextView) itemView.findViewById(R.id.cv_movie_title);
            txtTitle.setText(movie.getTitle());
            TextView txtDate = (TextView) itemView.findViewById(R.id.cv_movie_release_date);
            txtDate.setText(movie.getRelease_date());
            ImageView posterView = (ImageView) itemView.findViewById(R.id.cv_iv_movie_poster);
            String moviePosterURL = MovieDbClient.getPosterBaseUrl() + movie.getPoster_path();
            Glide.with(itemView.getContext())
                    .load(moviePosterURL)
                    .into(posterView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SingleMovieActivity.class);
                    intent.putExtra("id", movie.getId());
                    context.startActivity(intent);
                }
            });

        }

    }

    class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        public NetworkStateItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(NetworkState networkState) {
            ProgressBar progressBar = itemView.findViewById(R.id.progress_bar_item);
            if (networkState != null && networkState == NetworkState.LOADING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
            TextView txtError = (TextView) itemView.findViewById(R.id.error_msg_item);
            if (networkState != null && networkState == NetworkState.FAILED) {

                txtError.setVisibility(View.VISIBLE);
                txtError.setText(networkState.getMessage());
            } else {
                txtError.setVisibility(View.GONE);
            }
        }
    }


    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean hadExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean hasExtraRow = hasExtraRow();

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
            } else {
                notifyItemInserted(super.getItemCount());
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }

    }


}
