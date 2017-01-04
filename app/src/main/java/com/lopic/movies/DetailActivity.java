package com.lopic.movies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lopic.movies.data.Favourites;
import com.lopic.movies.data.FavouritesDbHelper;
import com.lopic.movies.utilities.BackgroundTask;
import com.lopic.movies.utilities.OpenJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView mTitle;
    TextView mOverview;
    TextView mRelease_Date;
    TextView mVote_AVG;
    ImageView mImageView;
    Button mTrailer;
    Menu mMenu;
    int id = 0;
    SQLiteDatabase mDB;
    FavouritesDbHelper dbHelper;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDB.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new FavouritesDbHelper(this);
        mDB = dbHelper.getReadableDatabase();
        mTitle = (TextView) findViewById(R.id.original_title);
        mImageView = (ImageView) findViewById(R.id.poster);
        mOverview = (TextView) findViewById(R.id.overview);
        mRelease_Date = (TextView) findViewById(R.id.release_date);
        mVote_AVG = (TextView) findViewById(R.id.vote_avg);
        mTrailer = (Button) findViewById(R.id.trailer_button);
        Intent intentThatStartedThisActivity = getIntent();


        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                String[] movie = intentThatStartedThisActivity.getStringArrayExtra("movie");
                mTitle.setText(movie[0]);
                Picasso.with(getApplicationContext()).load(movie[1]).into(mImageView);
                mOverview.setText(movie[2]);
                mVote_AVG.setText(movie[3] + " / 10");
                mRelease_Date.setText(movie[4]);
                id = Integer.parseInt(movie[5]);
            }

        }
        /*Give Functionality to Trailer Button*/
        BackgroundTask bgTask = new BackgroundTask(this, 1, id);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu, menu);
        mMenu = menu;
        changeFavImage();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_fav:
                if(!isFav()) {
                    addToFav();
                }else{
                    deleteFav();
                }
                changeFavImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deleteFav() {
        mDB.delete(Favourites.FavouritesList.TABLE_NAME, Favourites.FavouritesList.Movie_ID + "=" + id,
                null);
    }

    private long addToFav() {
        ContentValues cv = new ContentValues();
        cv.put(Favourites.FavouritesList.Movie_ID, id);
        return mDB.insert(Favourites.FavouritesList.TABLE_NAME, null, cv);
    }

    private boolean isFav() {
        Cursor cursor = mDB.query(Favourites.FavouritesList.TABLE_NAME, new String[] {Favourites.FavouritesList.Movie_ID + "=" + id},
                null, null, null, null, Favourites.FavouritesList.Movie_ID);
        if (cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    private void changeFavImage(){
        if(isFav() == true){
            mMenu.findItem(R.id.action_fav).setIcon(R.drawable.ic_favorite_black_24px);
        }else{
            mMenu.findItem(R.id.action_fav).setIcon(R.drawable.ic_favorite_border_white_24px);

        }
    }
}