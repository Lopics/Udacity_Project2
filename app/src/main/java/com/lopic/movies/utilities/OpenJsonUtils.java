package com.lopic.movies.utilities;

import com.lopic.movies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public final class OpenJsonUtils {



    public static List<Movie> getSimpleStringsFromJson(String JsonStr) {
         final String OWM_LIST = "results";
         final String OWM_OT = "original_title";
         final String OWM_OV = "overview";
         final String OWM_VA = "vote_average";
         final String OWM_ID = "id";
         final String OWM_RD = "release_date";
         final String OWM_POSTER = "poster_path";
        try {
            List<Movie> movies = new ArrayList<Movie>();
            JSONObject Json = new JSONObject(JsonStr);
            JSONArray Array = Json.getJSONArray(OWM_LIST);
            for (int i = 0; i < Array.length(); i++) {
                String original_title;
                String poster;
                String overview;
                String vote_average;
                String release_date;
                String id;
                JSONObject data_movie = Array.getJSONObject(i);
                original_title = data_movie.getString(OWM_OT);
                poster = "http://image.tmdb.org/t/p/w185" + data_movie.getString(OWM_POSTER);
                overview = data_movie.getString(OWM_OV);
                id = data_movie.getString(OWM_ID);
                vote_average = data_movie.getString(OWM_VA);
                release_date = data_movie.getString(OWM_RD);
                movies.add(new Movie( original_title, poster, overview, vote_average, release_date, id));
            }
            return movies;
        } catch (Exception e) {
            return null;
        }

    }

    public static String getVideoFromJson(String JsonStr) {
        final String OWM_LIST = "results";
         final String OWM_KY = "key";
        try {
            JSONObject Json = new JSONObject(JsonStr);

            JSONArray Array = Json.getJSONArray(OWM_LIST);
            JSONObject data_movie = Array.getJSONObject(0);
            return data_movie.getString(OWM_KY);
        } catch (Exception e) {
            return null;
        }

    }
}