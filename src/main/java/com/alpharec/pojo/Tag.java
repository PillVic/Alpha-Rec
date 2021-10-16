package com.alpharec.pojo;

import com.alpharec.data.DbWriter;
import com.alpharec.data.Handler;
import com.alpharec.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.sql.Timestamp;
import java.util.Date;

import static com.alpharec.util.ObjectAnalyzer.ToString;

public class Tag {
    private int userId;
    private int movieId;
    private String tag;
    private Timestamp timestamp;

    public Tag() {
    }

    public Tag(int userId, int movieId, String tag, Date timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.tag = tag;
    }

    public Tag(String line) {
        String[] cols = line.split(",");
        this.userId = Integer.parseInt(cols[0]);
        this.movieId = Integer.parseInt(cols[1]);
        this.tag = cols[2];
        this.timestamp = new Timestamp(Long.parseLong(cols[3]) * 1000);
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return ToString(this);
    }

    public static void main(String args[]) {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        DbWriter dbWriter = sqlSession.getMapper(DbWriter.class);

        final String fileName = "Data/MovieLens/ml-latest-small/tags.csv";
        Handler handler = new Handler(fileName, (line) -> {
            Tag t = new Tag(line);
            System.out.println(t);
/*            dbWriter.insertTag(t);*/
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
