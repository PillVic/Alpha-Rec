package com.alpharec.pojo;

import com.alpharec.data.DbWriter;
import com.alpharec.data.Handler;
import com.alpharec.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.sql.Timestamp;

import static com.alpharec.util.ObjectAnalyzer.ToString;

public class Rating {
    private int userId;
    private int movieId;
    private double rating;
    private Timestamp timestamp;

    public Rating(int userId, int movieId, double rating, Timestamp timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public Rating() {
    }

    public Rating(String line) {
        String[] v = line.split(",");
        this.userId = Integer.parseInt(v[0]);
        this.movieId = Integer.parseInt(v[1]);
        this.rating = Double.parseDouble(v[2]);
        this.timestamp = new Timestamp(Long.parseLong(v[3]) * 1000);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return ToString(this);
    }

    public static void main(String[] args) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        DbWriter dbWriter = sqlSession.getMapper(DbWriter.class);

        String file = "Data/MovieLens/ml-latest-small/ratings.csv";
        Handler handler = new Handler(file, (line)->{
            Rating rating = new Rating(line);
            dbWriter.insertRating(rating);
            System.out.println(rating);
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
