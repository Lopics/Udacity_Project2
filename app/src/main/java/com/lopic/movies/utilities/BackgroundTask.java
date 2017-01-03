package com.lopic.movies.utilities;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lopic.movies.MainActivity;

import org.json.JSONObject;

import static com.lopic.movies.utilities.NetworkUtils.buildUrl;

public class BackgroundTask {
    private int mPref;
    private Context mContext;
    private int id;

    public BackgroundTask(Context c, int p) {
        mContext = c;
        mPref = p;
    }

    public BackgroundTask(int i, Context c) {
        mContext = c;
        id = i+100;
    }

    public void getProduct(Response.Listener<JSONObject> listener,
                           Response.ErrorListener errlsn) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(buildUrl(mPref), null, listener, errlsn);
        MySingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    public void getVideo(Response.Listener<JSONObject> listener,
                         Response.ErrorListener errlsn) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(buildUrl(id), null, listener, errlsn);
        MySingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }
}
