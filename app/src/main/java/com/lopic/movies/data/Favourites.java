package com.lopic.movies.data;

import android.provider.BaseColumns;

public class Favourites {

    public static final class FavouritesList implements BaseColumns{
        public static final String TABLE_NAME = "favourites";
        public static final String TITLE = "original_title";
        public static final String POSTER = "poster";
        public static final String OVERVIEW = "overview";
        public static final String VOTE_AVG = "vote_average";
        public static final String RELEASED = "release_date";

    }
}
