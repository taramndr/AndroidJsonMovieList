package tara.com.jsonparsing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import tara.com.jsonparsing.R;
import tara.com.jsonparsing.Response.MovieListingDetail;
import tara.com.jsonparsing.Response.Result;

/**
 * Created by Tara on 06-Apr-17.
 */

public class MovieListingAdapter extends RecyclerView.Adapter<MovieListingAdapter.MovieListViewHolder>{

    private ArrayList<Result> movieListingDetailArrayList;
    private Context context;
    private MovieItemClickListener movieItemClickListener;

    public MovieListingAdapter(Context context, ArrayList<Result> movieListingDetailArrayList){
        this.movieListingDetailArrayList = movieListingDetailArrayList;
        this.context = context;
    }

    public void setClickListener(MovieItemClickListener movieItemClickListener) {
        this.movieItemClickListener = movieItemClickListener;
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {  //return to view holder as view
        View layoutView;
        layoutView = LayoutInflater.from(parent.getContext()). inflate(R.layout.movie_listing, parent, false);
        MovieListViewHolder rvh = new MovieListViewHolder(layoutView);
        return rvh;
        //return null;
    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, final int position) {
        holder.tvMovieName.setText(movieListingDetailArrayList.get(position).getOriginalTitle());
        //holder.ivMoviePoster.setImageResource(movieListingDetailArrayList.get(position).getMoviePosterPath());
        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w185//"+ movieListingDetailArrayList.get(position).getPosterPath())
                .into(holder.ivMoviePoster);
    }


    @Override
    public int getItemCount() {
        return movieListingDetailArrayList.size();
        //return 0;
    }

    public class MovieListViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivMoviePoster;
        public TextView tvMovieName;

        public MovieListViewHolder(View layoutView) {
            super(layoutView);
            ivMoviePoster = (ImageView) layoutView.findViewById(R.id.iv_movie_poster);
            tvMovieName = (TextView) layoutView.findViewById(R.id.tv_movie_name);
        }
    }

    public interface MovieItemClickListener {
        void onClick(Result result);
    }
}
