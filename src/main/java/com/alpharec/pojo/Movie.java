package com.alpharec.pojo;

public class Movie {
    private int movieId;
    private int title;
    private String genres;

    public Movie() {
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Movie(int movieId, int title, String genres) {
        this.movieId = movieId;
        this.title = title;
        this.genres = genres;
    }
}
