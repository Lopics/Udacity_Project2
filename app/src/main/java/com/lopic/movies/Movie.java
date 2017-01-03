package com.lopic.movies;

public class Movie {


    private String original_title;
    private String poster;
    private String overview;
    private String vote_average;
    private String release_date;
    private String id;
    private String video;

    public Movie(String or, String p, String ov, String v, String r,String i) {
        original_title = or;
        poster = p;
        overview = ov;
        vote_average = v;
        release_date = r;
        id = i;
    }

    public void videoURL(){

    }
    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster() {
        return poster;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String[] getArray() {
        String[] a = {original_title, poster, overview, vote_average, release_date, id};
        return a;
    }

    public String getId() {
        return id;
    }
}
