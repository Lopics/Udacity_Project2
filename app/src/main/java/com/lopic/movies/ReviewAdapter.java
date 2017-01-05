package com.lopic.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ReviewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Review> mDataSource;
    private LayoutInflater mInflater;

    public ReviewAdapter(Context context, ArrayList<Review> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.listview, parent, false);
        TextView titleTextView = (TextView) rowView.findViewById(R.id.author);
        TextView detailTextView = (TextView) rowView.findViewById(R.id.review);
        Review review = (Review) getItem(position);
        titleTextView.setText(review.getAuthor());
        detailTextView.setText(review.getReview());
        return rowView;
    }
}
