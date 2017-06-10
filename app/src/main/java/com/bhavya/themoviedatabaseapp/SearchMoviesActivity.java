package com.bhavya.themoviedatabaseapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bhavya.themoviedatabaseapp.adapter.MoviesAdapter;
import com.bhavya.themoviedatabaseapp.api.MoviesApiService;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchMoviesActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    public MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,
                getResources().getInteger(R.integer.grid_col)));
        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        //Checking API Connection. If the connection is successful movieResult is set to the movies adapter.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            getSupportActionBar().setTitle("Search Results for "+query);
            //query param with the searched keyword as value is added to the url to fetch the results accordingly
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.themoviedb.org/3")
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addEncodedQueryParam("api_key", getResources().getString(R.string.api_key));
                            request.addEncodedQueryParam("query", query);
                        }
                    })
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            MoviesApiService service = restAdapter.create(MoviesApiService.class);
            service.getSearchMovies(new Callback<Movie.MovieResult>() {
                @Override
                public void success(Movie.MovieResult movieResult, Response response) {
                    mAdapter.setMovieList(movieResult.getResults());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }
    }
}


