package com.alpharec.item;

import com.alpharec.pojo.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieItem {
    private final Movie movie;
    private final List<String> genreList;
    private List<String> tags;
    private int sawCount = 0;
    private double totalRate = 0;

    public MovieItem(Movie movie) {
        this.movie = movie;
        this.genreList = Arrays.stream(movie.getGenres().split(",")).toList();
    }

    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
    }

    public void addRate(double rate) {
        this.sawCount += 1;
        this.totalRate += rate;
    }

    public List<String> getTags() {
        return tags;
    }

    public int getSawCount() {
        return this.sawCount;
    }

    public double getTotalRate() {
        return this.totalRate;
    }

    public double getAverage() {
        if (this.sawCount == 0) return 0;
        return this.totalRate / this.sawCount;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public List<String> getGenreList() {
        return genreList;
    }
}
