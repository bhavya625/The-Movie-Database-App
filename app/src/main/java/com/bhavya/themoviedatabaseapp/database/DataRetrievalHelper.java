package com.bhavya.themoviedatabaseapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bhavya.themoviedatabaseapp.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhavya on 09/06/2017.
 */

public class DataRetrievalHelper {

    public List<Movie> getData(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_TITLE,
                DatabaseHelper.COLUMN_POSTER,
                DatabaseHelper.COLUMN_OVERVIEW,
                DatabaseHelper.COLUMN_RELEASE_DATE
        };
        String sortOrder = DatabaseHelper.COLUMN_ID + " DESC";
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME, projection, null,
                null, null, null, sortOrder);
        int iterator = 0;
        List<Movie> movies = new ArrayList<Movie>();
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                Movie movie = new Movie();
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TITLE));
                String poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POSTER));
                String overview = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OVERVIEW));
                String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RELEASE_DATE));
                movie.setTitle(title);
                movie.setPoster(poster);
                movie.setOverview(overview);
                movie.setReleaseDate(releaseDate);
                movies.add(movie);
                iterator++;
            } while (cursor.moveToNext() && iterator < 2);
        }
        return movies;
    }
}
