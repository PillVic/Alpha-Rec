package com.alpharec.data;

import com.alpharec.pojo.Link;
import com.alpharec.pojo.Movie;
import com.alpharec.pojo.Rating;
import com.alpharec.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DbReader {
    int getMaxMovieId();
    int getMinMovieId();

    List<Integer> getMovieIds(@Param("minMovieId") int minMovieId, @Param("maxMovieId") int maxMovieId);

    //basic single selec"t
    Movie getMovieById(int movieId);
    Link getLinkById(int movieId);
    List<Rating> getMovieRatingsByMovieId(int movieId);
    List<Rating> getMovieRatingsByUserId(int movieId);
    List<Tag> getTagsByMovieId(int movieId);
    List<Tag> getTagsByUserId(int userId);
}
