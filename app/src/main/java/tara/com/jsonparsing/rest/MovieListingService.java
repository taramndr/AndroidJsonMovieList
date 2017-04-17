package tara.com.jsonparsing.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tara.com.jsonparsing.Response.MovieListing;
import tara.com.jsonparsing.Response.YoutubeListing;

/**
 * Created by Tara on 05-Apr-17.
 */

public interface MovieListingService {

    @GET("upcoming")
    Call<MovieListing> getUpcomingMovies(@Query("api_key") String apiKey);  //Query is url manipulators , Path, body, full, part

   // @GET("movie/{id}")
   // Call<MovieListing> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);  //Query is url manipulators , Path, body, full, part

    @GET("movie/{id}/videos")
    Call<YoutubeListing> getYoutubeLinks(@Path("id") String id, @Query("api_key") String apiKey);
}
