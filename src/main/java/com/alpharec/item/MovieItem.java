package com.alpharec.item;

import com.alpharec.pojo.Movie;

import java.util.*;

import static com.alpharec.util.ObjectAnalyzer.ToString;

public class MovieItem {
    private final Movie movie;
    private final List<String> genreList;
    private Set<String> tags;
    private int seenCount = 0;
    private double totalRate = 0;

    public MovieItem(Movie movie) {
        this.movie = movie;
        if (movie.getGenres() != null) {
            this.genreList = Arrays.stream(movie.getGenres().split("\\|"))
                    .map(String::toLowerCase).toList();
        } else {
            this.genreList = null;
        }
    }

    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        }
        this.tags.add(tag.toLowerCase());
    }

    public void addRate(double rate) {
        this.seenCount += 1;
        this.totalRate += rate;
    }

    public Set<String> getTags() {
        return tags;
    }

    public int getSeenCount() {
        return this.seenCount;
    }

    public double getTotalRate() {
        return this.totalRate;
    }

    public double getAverage() {
        if (this.seenCount == 0) return 0;
        return this.totalRate / this.seenCount;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public List<String> getGenreList() {
        return genreList;
    }

    @Override
    public String toString() {
        return ToString(this);
    }

}
