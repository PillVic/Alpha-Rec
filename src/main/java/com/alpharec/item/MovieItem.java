package com.alpharec.item;

import com.alpharec.pojo.Movie;

import java.util.*;

public class MovieItem {
    private final Movie movie;
    private final List<String> genreList;
    private Set<String> tags;
    private int sawCount = 0;
    private double totalRate = 0;

    public MovieItem(Movie movie) {
        this.movie = movie;
        if (movie.getGenres() != null) {
            this.genreList = Arrays.stream(movie.getGenres().split(",")).toList();
        }else{
            this.genreList = null;
        }
    }

    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        }
        this.tags.add(tag);
    }

    public void addRate(double rate) {
        this.sawCount += 1;
        this.totalRate += rate;
    }

    public Set<String> getTags() {
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

    @Override
    public String toString() {
        return "MovieItem{" +
                "movie=" + movie +
                ", genreList=" + genreList +
                ", tags=" + tags +
                ", sawCount=" + sawCount +
                ", totalRate=" + totalRate +
                '}';
    }
}
