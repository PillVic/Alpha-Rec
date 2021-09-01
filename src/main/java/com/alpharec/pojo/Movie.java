package com.alpharec.pojo;

import com.alpharec.data.DbWriter;
import com.alpharec.data.Handler;
import com.alpharec.util.Flag;
import com.alpharec.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Movie {
    private int movieId;
    private String title;
    private String genres;
    private int year;

    public Movie(int movieId, String title, String genres, int year) {
        this.movieId = movieId;
        this.title = title;
        this.genres = genres;
        this.year = year;
    }

    public Movie() {
    }

    public Movie(String line) {
        String cleanLine = line.replaceAll("\"", "");
        String[] v = cleanLine.split(",");
        this.movieId = Integer.parseInt(v[0]);

        v[1] = v[1].trim();
        if (v[1].matches(".*\\([0-9]{4}\\)")) {
            int begin = v[1].length();
            Pattern yearPattern = Pattern.compile("[0-9]{4}");
            Matcher matcher = yearPattern.matcher(v[1]);
            while (matcher.find()) {
                this.year = Integer.parseInt(matcher.group());
                begin = matcher.start();
            }
            this.title = v[1].substring(0, begin - 1).trim();
        } else {
            this.title = v[1];
        }

        if (!v[2].equals(Flag.EMPTY_GENRES)) {
            this.genres = v[2];
        }
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", genres='" + genres + '\'' +
                ", year=" + year +
                '}';
    }

    public static void main(String[] args) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        DbWriter dbWriter = sqlSession.getMapper(DbWriter.class);

        String file = "DataSet/MovieLens/ml-latest-small/movies.csv";
        Handler handler = new Handler(file, (line) -> {
            Movie movie = new Movie(line);
            dbWriter.insertMovie(movie);
        });
        Thread r = new Thread(handler, "write link");
        r.start();
        try {
            r.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sqlSession.commit();
        sqlSession.close();
    }
}
