package com.alpharec.pojo;

import com.alpharec.data.DbWriter;
import com.alpharec.data.Handler;
import com.alpharec.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

public class Link {
    private int movieId;
    private int imdbId;
    private int tmdbId;

    public Link(int movieId, int imdbId, int tmdbId) {
        this.movieId = movieId;
        this.imdbId = imdbId;
        this.tmdbId = tmdbId;
    }

    public Link() {
    }

    public Link(String line) {
        String[] cols = line.split(",");
        this.movieId = Integer.parseInt(cols[0]);
        this.imdbId = Integer.parseInt(cols[1]);
        if (cols.length == 3) {
            this.tmdbId = Integer.parseInt(cols[2]);
        }
        System.out.println(this);
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getImdbId() {
        return imdbId;
    }

    public void setImdbId(int imdbId) {
        this.imdbId = imdbId;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Override
    public String toString() {
        return "Link{" +
                "movieId=" + movieId +
                ", imdbId=" + imdbId +
                ", tmdbId=" + tmdbId +
                '}';
    }

    public static void main(String[] args) {

        String file = "DataSet/MovieLens/ml-latest-small/links.csv";
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        DbWriter dbWriter = sqlSession.getMapper(DbWriter.class);

        Handler h = new Handler(file, (line)->{
            Link link = new Link(line);
            dbWriter.insertLink(link);
        });
        Thread r = new Thread(h, "write link");
        r.start();
        try {
            r.join();

            sqlSession.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
