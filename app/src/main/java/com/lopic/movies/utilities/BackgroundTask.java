package com.lopic.movies.utilities;

import android.content.Context;
import android.widget.Switch;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import static com.lopic.movies.utilities.NetworkUtils.buildUrl;

public class BackgroundTask {
    private int mPref;
    private Context mContext;
    private static final String RATED_BASE_URL =
            "https://api.themoviedb.org/3/movie/top_rated";
    private static final String POPULAR_BASE_URL =
            "https://api.themoviedb.org/3/movie/popular";
    private static final String MOVIE_VIDEO_URL = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIE_DETAIL = "https://api.themoviedb.org/3/movie/";
    private static String BASE_URL;

    public BackgroundTask(Context c, int p) {
        mContext = c;
        switch(p) {
            case 0:
                BASE_URL = RATED_BASE_URL;
                break;
            case 1:
                BASE_URL = POPULAR_BASE_URL;
                break;
        }
    }

    public BackgroundTask(Context c, int p, int i) {
        mContext = c;
        switch (p){
            case 1:
                BASE_URL = MOVIE_VIDEO_URL + (i) +"/videos";
                break;
            case 2:
                BASE_URL = MOVIE_DETAIL + i;
        }
    }

    public void getProduct(Response.Listener<JSONObject> listener,
                           Response.ErrorListener errlsn) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(buildUrl(BASE_URL), null, listener, errlsn);
        MySingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

}
