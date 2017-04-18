package tara.com.jsonparsing.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
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

    private final static String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Result> upcomingMovieList = new ArrayList<>();
    private MovieListingAdapter mListAdapter;
    private RecyclerView rvMovieLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Check for Network Connection
        if ( !isNetworkAvailable() ){
            noInternetAlertMessage();
        }

        if(TextUtils.isEmpty(BuildConfig.TMDBMOVIEAPIKEY)){
            Toast.makeText(MainActivity.this, "API Key is required for further operation.", Toast.LENGTH_SHORT).show();
            return;
        }

        rvMovieLayout = (RecyclerView) findViewById(R.id.rv_movie_listing);

//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        rvMovieLayout.setLayoutManager(staggeredGridLayoutManager);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this); // it says recycle view to be Linear List
        rvMovieLayout.setLayoutManager(linearLayoutManager);

        getMovieListing();
        mListAdapter = new MovieListingAdapter(MainActivity.this, upcomingMovieList);
        rvMovieLayout.setAdapter(mListAdapter);

    }

    private void noInternetAlertMessage() {
       // NoInternetConnectionDialog dialog = new NoInternet
        Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }

        return isAvailable;
    }

    private void getMovieListing(){

        RetrofitManager.getInstance().getUpcomingMovieList(BuildConfig.TMDBMOVIEAPIKEY, new Callback<MovieListing>() {

            @Override
            public void onResponse(Call<MovieListing> call, Response<MovieListing> response) {

                if (response.code() == 200){
                    updateMovies(response);
                }
                else{
                    Log.i(TAG, "onResponse: " + response.message());
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
                Log.i(TAG, "onClickItem: ");
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
