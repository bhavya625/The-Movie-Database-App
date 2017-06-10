package com.bhavya.themoviedatabaseapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bhavya on 08/06/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "moviedatabase";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "moviedata";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "tile";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_RELEASE_DATE = "releaseDate";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME
            + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " string, "
            + COLUMN_POSTER + " string, "
            + COLUMN_OVERVIEW + " string, "
            + COLUMN_RELEASE_DATE + " string"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        ContentValues cv = new ContentValues(3);
        cv.put(COLUMN_TITLE,"");
        cv.put(COLUMN_POSTER, "");
        cv.put(COLUMN_OVERVIEW,"");
        cv.put(COLUMN_RELEASE_DATE,"");
        database.insert(TABLE_NAME,null,cv);

    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(database);

    }
}
