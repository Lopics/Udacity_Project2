package com.lopic.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavouritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouritelist";
    private static final int DATABASE_VERSION = 4;


    public FavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITELIST_TABLE = "CREATE TABLE " + Favourites.FavouritesList.TABLE_NAME +
                " (" + Favourites.FavouritesList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Favourites.FavouritesList.Movie_ID + " INTEGER UNIQUE NOT NULL );";
        db.execSQL(SQL_CREATE_FAVOURITELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIsTS "+ Favourites.FavouritesList.TABLE_NAME);
        onCreate(db);
    }
}
