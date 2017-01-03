package com.lopic.movies;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lopic.movies.utilities.BackgroundTask;
import com.lopic.movies.utilities.OpenJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle;
    TextView mOverview;
    TextView mRelease_Date;
    TextView mVote_AVG;
    ImageView mImageView;
    Button mTrailer;
    Button mFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = (TextView) findViewById(R.id.original_title);
        mImageView = (ImageView) findViewById(R.id.poster);
        mOverview = (TextView) findViewById(R.id.overview);
        mRelease_Date = (TextView) findViewById(R.id.release_date);
        mVote_AVG = (TextView) findViewById(R.id.vote_avg);
        mTrailer = (Button) findViewById(R.id.trailer_button);
        mFav = (Button) findViewById(R.id.fav_button);
        Intent intentThatStartedThisActivity = getIntent();
        String id = "";

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                String[] movie = intentThatStartedThisActivity.getStringArrayExtra("movie");
                mTitle.setText(movie[0]);
                Picasso.with(getApplicationContext()).load(movie[1]).into(mImageView);
                mOverview.setText(movie[2]);
                mVote_AVG.setText(movie[3] + " / 10");
                mRelease_Date.setText(movie[4]);
                id = movie[5];
            }

        }
        /*Give Functionality to Trailer Button*/
        BackgroundTask bgTask = new BackgroundTask(this, 'v' ,Integer.parseInt(id));
        bgTask.getProduct(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    final String url = "https://www.youtube.com/watch?v=" + OpenJsonUtils.getVideoFromJson(response.toString());
                    mTrailer.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        }
                    });

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        });
    }
}