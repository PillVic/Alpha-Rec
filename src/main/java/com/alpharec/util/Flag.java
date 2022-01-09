package com.alpharec.util;

public class Flag {
    public static final String EMPTY_GENRES = "(no genres listed)";

    /**
     * 记录Movie的所有倒排相关字段
     *
    * */
    public enum MovieField {
        MOVIE_TAG, MOVIE_GENRES, MOVIE_ID, MOVIE_YEAR,
        MOVIE_TOTAL_SCORE, MOVIE_SEEN, MOVIE_AVERAGE
    }

    /**
     * 记录User倒排相关字段
    * */
    public enum UserField {
        USER_ID, PREFER_TAGS, PREFER_GENRES
    }
}
