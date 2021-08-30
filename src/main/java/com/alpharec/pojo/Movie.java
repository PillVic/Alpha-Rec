package com.alpharec.pojo;

public class Movie {
    private int movieId;
    private String title;
    private int year;
    private String genres;

    public Movie() {
    }

    public Movie(String line){
        String[] v = line.split(",");
        this.movieId = Integer.getInteger(v[0]);
        this.genres = v[2];
    }

    public Movie(int movieId, String title, int year, String genres) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.genres = genres;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}
