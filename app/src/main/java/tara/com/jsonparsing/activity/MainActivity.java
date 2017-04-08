package tara.com.jsonparsing.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tara.com.jsonparsing.BuildConfig;
import tara.com.jsonparsing.R;
import tara.com.jsonparsing.Response.MovieListing;
import tara.com.jsonparsing.Response.Result;
import tara.com.jsonparsing.adapter.MovieListingAdapter;
import tara.com.jsonparsing.rest.RetrofitManager;

import static tara.com.jsonparsing.rest.RetrofitManager.getUpcomingMovieList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Result> upcomingMovieList = new ArrayList<>();
    private MovieListingAdapter mListAdapter;
    private RecyclerView rvMovieLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMovieLayout = (RecyclerView) findViewById(R.id.rv_movie_listing);

//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        rvMovieLayout.setLayoutManager(staggeredGridLayoutManager);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this); // it says recycle view to be Linear List
        rvMovieLayout.setLayoutManager(linearLayoutManager);


        getMovieListing();
        mListAdapter = new MovieListingAdapter(MainActivity.this, upcomingMovieList);
        rvMovieLayout.setAdapter(mListAdapter);

    }

    private void getMovieListing(){

        RetrofitManager.getInstance().getUpcomingMovieList("your_api_key", new Callback<MovieListing>(){ //BuildConfig.TMDBMOVIEAPIKEY

            @Override
            public void onResponse(Call<MovieListing> call, Response<MovieListing> response) {

                if (response.code() == 200){
                    updateMovies(response);
                }
                else{
                    Log.i(TAG, "onResponse: " + response);
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MovieListing> call, Throwable t) {
                Log.i(TAG, "onFailure: "+ t.getLocalizedMessage());
            }
        });
    }

    //update movie list when response is success
    private void updateMovies(Response<MovieListing> response){
        upcomingMovieList.addAll(response.body().getResults());
        mListAdapter.notifyDataSetChanged();
        mListAdapter.setClickListener(new MovieListingAdapter.MovieItemClickListener(){
            @Override
            public void onClick(Result result) {
                startActivity(DetailActivity.getLaunchIntent(MainActivity.this, result));
            }
        });

        //  below line defined inside DetailActivity
//        mListAdapter.setClickListener(new MovieListingAdapter.MovieItemClickListener(){
//            @Override
//            public void onClick(Result result) {
//                Intent startDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
//                startDetailActivity.putExtra("Movie_Detail", result);
//                startActivity(startDetailActivity);
//            }
//        });
    }
}
