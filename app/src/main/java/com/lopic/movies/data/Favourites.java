package com.lopic.movies.data;

import android.provider.BaseColumns;

public class Favourites {

    public static final class FavouritesList implements BaseColumns{
        public static final String TABLE_NAME = "favourites";
        public static final String Movie_ID = "original_title";
    }
}
