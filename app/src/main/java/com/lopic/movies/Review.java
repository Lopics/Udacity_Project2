package com.lopic.movies;

public class Review {
    private String mAuthor;
    private String mReview;

    public Review(String a, String r){
        mAuthor = a;
        mReview = r;
    }


    public String getAuthor() {
        return mAuthor;
    }

    public String getReview() {
        return mReview;
    }
}
