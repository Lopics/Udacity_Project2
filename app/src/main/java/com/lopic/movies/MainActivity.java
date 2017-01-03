package com.lopic.movies;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lopic.movies.utilities.BackgroundTask;
import com.lopic.movies.utilities.MySingleton;
import com.lopic.movies.utilities.OpenJsonUtils;

import org.json.JSONObject;

import java.util.List;

import static com.lopic.movies.utilities.NetworkUtils.buildUrl;

public class MainActivity extends AppCompatActivity {

    private GridView gridview;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoading = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        gridview = (GridView) findViewById(R.id.gridview);
        loadMovieData();
    }

    private void loadMovieData() {
        showDataView();
        if(getPreference() == 1 || getPreference() == 0) {
            volley();
        }else if (getPreference() == 2){
            displaySqlList();
        }
    }

    private void displaySqlList() {

    }


    private void volley() {
        BackgroundTask bgTask = new BackgroundTask(MainActivity.this, getPreference());
        bgTask.getProduct(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() > 0) {
                            try {
                                List<Movie> mResults = OpenJsonUtils
                                        .getSimpleStringsFromJson(response.toString());
                                mLoading.setVisibility(View.INVISIBLE);
                                imageDisplay(mResults);
                            } catch (Exception e) {
                                showErrorMessage();
                            }
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
        if (results != null) {
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
        CharSequence colors[] = new CharSequence[]{"Most Popular", "Top Rated", "Favourate"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
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
    }

    private void showErrorMessage() {
        gridview.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

}

