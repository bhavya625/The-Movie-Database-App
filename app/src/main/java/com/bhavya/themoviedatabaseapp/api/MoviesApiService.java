package com.bhavya.themoviedatabaseapp.api;

import com.bhavya.themoviedatabaseapp.Movie;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by bhavya on 08/06/2017.
 */

public interface MoviesApiService {
    //Search API Endpoint
    @GET("/search/movie")
    void getSearchMovies(Callback<Movie.MovieResult> cb);

    //Top Rated Movies API Endpoint
    @GET("/movie/top_rated")
    void getTopRatedMovies(Callback<Movie.MovieResult> cb);

    //Now Playing Movies API Endpoint
    @GET("/movie/now_playing")
    void getNowPlayingMovies(Callback<Movie.MovieResult> cb);
}
