package com.lopic.movies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String RATED_BASE_URL =
            "https://api.themoviedb.org/3/movie/top_rated";
    private static final String POPULAR_BASE_URL =
            "https://api.themoviedb.org/3/movie/popular";
    private static String BASE_URL;
    private static final String API_KEY = "e5eb3c60e02416db4ede9ebb0e9b2906";
    private static final String API_PARAM = "api_key";

    public static String buildUrl(int URL) {
        if (URL == 0) {
            BASE_URL = RATED_BASE_URL;
        }else if (URL == 1) {
            BASE_URL = POPULAR_BASE_URL;
        }else {
            BASE_URL = "https://api.themoviedb.org/3/movie/"+ (URL-100) +"/videos";
        }
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url.toString();
    }
}