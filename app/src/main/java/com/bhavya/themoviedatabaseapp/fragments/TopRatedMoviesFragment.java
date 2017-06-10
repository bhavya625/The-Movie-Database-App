package com.bhavya.themoviedatabaseapp.fragments;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bhavya.themoviedatabaseapp.AppUtils;
import com.bhavya.themoviedatabaseapp.Movie;
import com.bhavya.themoviedatabaseapp.R;
import com.bhavya.themoviedatabaseapp.adapter.MoviesAdapter;
import com.bhavya.themoviedatabaseapp.api.MoviesApiService;
import com.bhavya.themoviedatabaseapp.database.DataRetrievalHelper;
import com.bhavya.themoviedatabaseapp.database.DatabaseHelper;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by bhavya on 09/06/2017.
 */

public class TopRatedMoviesFragment extends Fragment {

    RecyclerView mRecyclerView;
    public MoviesAdapter mAdapter;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public TopRatedMoviesFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.content_top_rated_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.popular_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.grid_col)));
        mAdapter = new MoviesAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);

        /*Checking API Connection. If the connection is successful movieResult is set to the movies adapter.
            The top two movie results are stored locally using SQLite.
         */
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", getResources().getString(R.string.api_key));
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        MoviesApiService service = restAdapter.create(MoviesApiService.class);
        service.getTopRatedMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                List<Movie> movieList = movieResult.getResults();
                Movie movie;
                if(AppUtils.isNetworkAvailable(getContext())) {
                    mAdapter.setMovieList(movieList);
                    mAdapter.notifyDataSetChanged();
                    for (int i = 0; i < 2; i++) {
                        movie = movieList.get(i);
                        sqLiteDatabase = databaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.COLUMN_TITLE, movie.getTitle());
                        values.put(DatabaseHelper.COLUMN_POSTER, movie.getPoster());
                        values.put(DatabaseHelper.COLUMN_OVERVIEW, movie.getOverview());
                        values.put(DatabaseHelper.COLUMN_RELEASE_DATE, movie.getReleaseDate());
                        sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, values);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                //If network connection is not available the top two results from the local storage will be displayed
                if(!AppUtils.isNetworkAvailable(getContext())) {
                    try {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                        dialogBuilder.setTitle(getResources().getString(R.string.dailog_title));
                        dialogBuilder.setMessage(getResources().getString(R.string.dailog_message));
                        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showMovieFromLocalDB();
                            }
                        });
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();
                    }
                    catch(Exception e)
                    {
                        Log.d("Show Dialog: ", e.getMessage());
                    }
                }
                error.printStackTrace();
            }
        });
    }

    private void showMovieFromLocalDB() {
        DataRetrievalHelper dataRetrievalHelper = new DataRetrievalHelper();
        List<Movie> movies = dataRetrievalHelper.getData(getContext());
        mAdapter.setMovieList(movies);
        mAdapter.notifyDataSetChanged();
    }
}
