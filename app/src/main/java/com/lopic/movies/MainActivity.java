package com.lopic.movies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lopic.movies.data.Favourites;
import com.lopic.movies.data.FavouritesDbHelper;
import com.lopic.movies.utilities.BackgroundTask;
import com.lopic.movies.utilities.OpenJsonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private GridView gridview;
    private TextView mErrorMessageDisplay;
    private TextView mErrorMessageFav;
    private ProgressBar mLoading;
    private SQLiteDatabase mDB;
    private FavouritesDbHelper dbHelper;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDB.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new FavouritesDbHelper(this);
        mDB = dbHelper.getReadableDatabase();
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoading = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageFav = (TextView) findViewById(R.id.tv_error_message_fav);
        gridview = (GridView) findViewById(R.id.gridview);
        loadMovieData();
    }

    private void loadMovieData() {
        showDataView();
        if (getPreference() == 1 || getPreference() == 0) {
            volley();
        } else if (getPreference() == 2) {
            displaySqlList();
        }
    }

    private void displaySqlList() {

        final List<Integer> favMovies = getDataFromSQL();
        if (!favMovies.isEmpty()) {
            final List<Movie> mResults = new ArrayList<Movie>();
            for (int i = 0; i < favMovies.size(); i++) {
                BackgroundTask bgTask = new BackgroundTask(MainActivity.this, getPreference(),favMovies.get(i));
                final int finalI = i;
                bgTask.getProduct(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() > 0) {
                            mResults.add(OpenJsonUtils.getOneMovie(response.toString()));
                            if(finalI +1 == favMovies.size())
                                imageDisplay(mResults);
                        } else {
                            showErrorMessage();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", error.toString());
                    }
                });
            }
            mLoading.setVisibility(View.INVISIBLE);

        }
    }

    private List<Integer> getDataFromSQL() {
        List<Integer> movieID = new ArrayList<Integer>();
        Cursor cursor = mDB.query(Favourites.FavouritesList.TABLE_NAME, null,
                null, null, null, null, Favourites.FavouritesList.Movie_ID);
        if (cursor.getCount() == 0) {
            mLoading.setVisibility(View.INVISIBLE);
            mErrorMessageFav.setVisibility(View.VISIBLE);
            return movieID;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            movieID.add(cursor.getInt(cursor.getColumnIndex(Favourites.FavouritesList.Movie_ID)));
            cursor.moveToNext();
        }
        return movieID;
    }

    private void volley() {
        BackgroundTask bgTask = new BackgroundTask(MainActivity.this, getPreference());
        bgTask.getProduct(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    List<Movie> mResults = OpenJsonUtils
                            .getSimpleStringsFromJson(response.toString());
                    mLoading.setVisibility(View.INVISIBLE);
                    imageDisplay(mResults);
                } else {
                    showErrorMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        });

    }

    private void imageDisplay(final List<Movie> results) {
        if (!results.isEmpty()) {
            gridview.setAdapter(new ImageAdapter(MainActivity.this, results));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intentToStartDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
                    intentToStartDetailActivity.putExtra("movie", results.get(position).getArray());
                    startActivity(intentToStartDetailActivity);
                }
            });
        } else {
            showErrorMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                popup();
                return true;
            case R.id.action_refresh:
                loadMovieData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void popup() {
        CharSequence options[] = new CharSequence[]{"Most Popular", "Top Rated", "Favourate"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setPreference(0);
                } else if (which == 1) {
                    setPreference(1);
                } else if (which == 2) {
                    setPreference(2);
                }
                loadMovieData();
            }
        });
        builder.show();
    }

    private void setPreference(int b) {
        SharedPreferences sharedPref = getSharedPreferences("MoviesApp", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("SortBy", b);
        editor.apply();
    }

    private int getPreference() {
        SharedPreferences sharedPref = getSharedPreferences("MoviesApp", Context.MODE_PRIVATE);
        return sharedPref.getInt("SortBy", 0);
    }

    private void showDataView() {
        gridview.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mErrorMessageFav.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        gridview.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


}

