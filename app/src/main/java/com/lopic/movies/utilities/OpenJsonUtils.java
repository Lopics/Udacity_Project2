package com.lopic.movies.utilities;

import android.util.Log;

import com.lopic.movies.Movie;
import com.lopic.movies.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public final class OpenJsonUtils {
    private static final String OWM_LIST = "results";
    private static final String OWM_OT = "original_title";
    private static final String OWM_OV = "overview";
    private static final String OWM_VA = "vote_average";
    private static final String OWM_ID = "id";
    private static final String OWM_RD = "release_date";
    private static final String OWM_POSTER = "poster_path";
    private static final String OWM_KY = "key";
    private static final String OWM_AU = "author";
    private static final String OWM_CO = "content";

    public static List<Movie> getSimpleStringsFromJson(String JsonStr) {

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
                movies.add(new Movie(original_title, poster, overview, vote_average, release_date, id));
            }
            return movies;
        } catch (Exception e) {
            return null;
        }

    }

    public static String getVideoFromJson(String JsonStr) {

        try {
            JSONObject Json = new JSONObject(JsonStr);

            JSONArray Array = Json.getJSONArray(OWM_LIST);
            JSONObject data_movie = Array.getJSONObject(0);
            return data_movie.getString(OWM_KY);
        } catch (Exception e) {
            Log.v("JSON:", "NOT WORKING!");
            return null;
        }

    }

    public static Movie getOneMovie(String JsonStr) {
        try {
            Movie movies;
            JSONObject Json = new JSONObject(JsonStr);

            String original_title;
            String poster;
            String overview;
            String vote_average;
            String release_date;
            String id;

            original_title = Json.getString(OWM_OT);
            poster = "http://image.tmdb.org/t/p/w185" + Json.getString(OWM_POSTER);
            overview = Json.getString(OWM_OV);
            id = Json.getString(OWM_ID);
            vote_average = Json.getString(OWM_VA);
            release_date = Json.getString(OWM_RD);
            movies = new Movie(original_title, poster, overview, vote_average, release_date, id);

            return movies;
        } catch (Exception e) {
            Log.v("JSON:", "NOT WORKING!");
            return null;
        }
    }

    public static ArrayList<Review> getReviews(String JsonStr) {
        try {
            ArrayList<Review> reviews = new ArrayList<Review>();
            JSONObject Json = new JSONObject(JsonStr);
            JSONArray Array = Json.getJSONArray(OWM_LIST);
            for (int i = 0; i < Array.length(); i++) {
                String author;
                String review;
                JSONObject data_movie = Array.getJSONObject(i);
                author = data_movie.getString(OWM_AU);
                review = data_movie.getString(OWM_CO);
                reviews.add(new Review(author, review));
            }
            return reviews;
        } catch (Exception e) {
            Log.v("JSON:", "NOT WORKING!");
            return null;
        }
    }
}