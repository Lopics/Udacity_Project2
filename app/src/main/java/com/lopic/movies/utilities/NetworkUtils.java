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


    private static final String API_KEY = "API_KEY";
    private static final String API_PARAM = "api_key";

    public static String buildUrl(String BASE_URL) {

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