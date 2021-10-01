package com.alpharec.data;

import com.alpharec.pojo.Link;
import com.alpharec.pojo.Movie;
import com.alpharec.pojo.Rating;
import com.alpharec.pojo.Tag;

import java.util.List;

public interface DbReader {
    int getMaxMovieId();
    int getMinMovieId();

    //basic single select
    Movie getMovieById(int movieId);
    Link getLinkById(int movieId);
    List<Rating> getMovieRatingsByMovieId(int movieId);
    List<Rating> getMovieRatingsByUserId(int movieId);
    List<Tag> getTagsByMovieId(int movieId);
    List<Tag> getTagsByUserId(int userId);
/*
    List<Movie> getMovies(List<Integer> movieIds);
*/
/*
    List<Rating> getRatings(int movieId);
    List<Tag> getTags(int movieId);
*/
}
