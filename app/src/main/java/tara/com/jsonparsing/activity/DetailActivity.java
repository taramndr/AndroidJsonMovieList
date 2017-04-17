package tara.com.jsonparsing.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tara.com.jsonparsing.BuildConfig;
import tara.com.jsonparsing.R;
import tara.com.jsonparsing.Response.Result;
import tara.com.jsonparsing.Response.YoutubeListing;
import tara.com.jsonparsing.rest.RetrofitManager;

public class DetailActivity extends AppCompatActivity {
    private static String TAG = DetailActivity.class.getSimpleName();
    private TextView tvMovieTitle, tvMovieOverview, tvReleaseDate, tvMoviePopularity, tvMovieVoteCount, tvMovieVoteAverage;
    private ImageView ivMovieImage, ivBackdropImage;
    Toolbar toolbar;
    private TextView txtViewTool;
    private ImageView imgViewTool;

    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //obtain Toolbar Contents
        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        txtViewTool = (TextView) toolbar.findViewById(R.id.txt_tool_title);
        imgViewTool = (ImageView) toolbar.findViewById(R.id.img_tool);

        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //getSupportActionBar().setLogo(R.mipmap.ic_launcher); //logo work as back button
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        Result movieDetail = (Result) intent.getExtras().get("Movie_Detail");
        Log.i(TAG, "onCreate: "+ movieDetail.getTitle());
        getSupportActionBar().setTitle(movieDetail.getTitle());

        initializeViews();

        movieID = movieDetail.getId().toString();
        getMovieYtVideoLink(movieID);

        Log.i(TAG, "onCreate: movieid = "+movieID);
        //  Log.i(TAG, "onCreate: " + movieDetail.getOriginalTitle());
        //  Log.i(TAG, "onCreate: " + movieDetail.getReleaseDate());

        //set values on View
        tvMovieTitle.setText(movieDetail.getOriginalTitle());
        tvReleaseDate.setText(movieDetail.getReleaseDate());
        //Log.i(TAG, "image url: "+ "http://image.tmdb.org/t/p/w185//"+ movieDetail.getPosterPath() + movieDetail.getOriginalTitle() );
        Glide.with(DetailActivity.this)
                .load("http://image.tmdb.org/t/p/w185//"+ movieDetail.getPosterPath())
                .into(ivMovieImage);
//        Glide.with(DetailActivity.this)
//                .load("http://image.tmdb.org/t/p/w185//"+ movieDetail.getBackdropPath())
//                .into(ivBackdropImage);

       //Making second image Round
        final Context context = getApplicationContext();
        Glide.with(context)
                .load("http://image.tmdb.org/t/p/w185//"+ movieDetail.getBackdropPath())
                .asBitmap().centerCrop().into(new BitmapImageViewTarget(ivBackdropImage){
            @Override
            protected void setResource(Bitmap resource) {
                //super.setResource(resource);
                RoundedBitmapDrawable circleBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circleBitmapDrawable.setCircular(true);
                ivBackdropImage.setImageDrawable(circleBitmapDrawable);
            }
        });

        tvMovieOverview.setText(movieDetail.getOverview());
        tvMoviePopularity.setText(movieDetail.getPopularity().toString());
        tvMovieVoteCount.setText(movieDetail.getVoteCount().toString());
        tvMovieVoteAverage.setText(String.valueOf(movieDetail.getVoteAverage()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void getMovieYtVideoLink(String movieID){
        RetrofitManager.getInstance().getMovieYtLink( movieID, BuildConfig.TMDBMOVIEAPIKEY, new Callback<YoutubeListing>(){

            @Override
            public void onResponse(Call<YoutubeListing> call, Response<YoutubeListing> response) {
                if (response.code() == 200){
                    Log.i(TAG, "onResponse: Youtube link"+ response.body().getVlinkresults());
                } else{
                    Log.i(TAG, "onResponse: " + response.message());
                    Toast.makeText(DetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<YoutubeListing> call, Throwable t) {
                Log.i(TAG, "onFailure: "+ t.getLocalizedMessage());
            }
        });
    }


    private void initializeViews() {
        tvMovieTitle = (TextView) findViewById(R.id.tv_detail_title);
        ivMovieImage = (ImageView) findViewById(R.id.iv_detail_image);
        ivBackdropImage = (ImageView) findViewById(R.id.iv_detail_backdrop_image);

        tvReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        tvMovieOverview = (TextView) findViewById(R.id.tv_detail_overview);
        tvMoviePopularity = (TextView) findViewById(R.id.tv_detail_popularity);
        tvMovieVoteAverage = (TextView) findViewById(R.id.tv_detail_vote_average);
        tvMovieVoteCount = (TextView) findViewById(R.id.tv_detail_vote_count);
    }

    public static Intent getLaunchIntent(Context context, Result result) {
        Intent movieDetailActivityIntent = new Intent(context, DetailActivity.class);
        movieDetailActivityIntent.putExtra("Movie_Detail",result);
        return movieDetailActivityIntent;
    }
}
