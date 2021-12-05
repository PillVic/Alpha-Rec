package com.alpharec.data;

import com.alpharec.pojo.Link;
import com.alpharec.pojo.Movie;
import com.alpharec.pojo.Rating;
import com.alpharec.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 利用mybatis 读取数据库中的数据
 *
 * @author pillvic
 */
public interface DbReader {
    int getMaxMovieId();
    int getMinMovieId();

    /**
     * @return ratings 中最大的userId
     */
    int getMaxUserId();

    /**
     * @return ratings 中最小的 userId
     */
    int getMinUserId();

    /**
     * 从movies 批量获取movieId
     *
     * @param minMovieId 指定最小的movieId
     * @param maxMovieId 指定最大的movieId
     * @return 指定范围内的在movies表中存在的所有movieId
     */
    List<Integer> getMovieIds(@Param("minMovieId") int minMovieId, @Param("maxMovieId") int maxMovieId);

    Movie getMovieById(int movieId);

    Link getLinkById(int movieId);

    List<Rating> getMovieRatingsByMovieId(int movieId);

    List<Rating> getMovieRatingsByUserId(int userId);

    List<Tag> getTagsByMovieId(int movieId);

    List<Tag> getTagsByUserId(int userId);
}
