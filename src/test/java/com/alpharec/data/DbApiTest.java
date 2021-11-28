package com.alpharec.data;

import com.alpharec.JavaConfig;
import com.alpharec.pojo.Link;
import com.alpharec.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.alpharec.util.ObjectAnalyzer.ToString;

public class DbApiTest {
    @Test
    public void writeLinks() {
        String file = "/home/neovic/Work/RecommendSystem/DataSet/MovieLens/ml-latest-small/links.csv";
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        DbWriter dbWriter = sqlSession.getMapper(DbWriter.class);

        Handler h = new Handler(file, (line) -> {
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

    @Test
    public void testUser(){
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        Resource r = context.getBean("resource", Resource.class);
        System.out.println(r.dbReader.getMinUserId());
        System.out.println(r.dbReader.getMaxUserId());
        for(var t:r.dbReader.getMovieRatingsByUserId(1)){
            System.out.println(ToString(t));
        }
    }

    @Test
    public void readRecord() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        DbReader dbReader = sqlSession.getMapper(DbReader.class);

        System.out.println(dbReader.getMaxMovieId());
        System.out.println(dbReader.getMinMovieId());
        System.out.println(dbReader.getMovieById(1));
        System.out.println(dbReader.getLinkById(1));
        System.out.println(dbReader.getMovieRatingsByMovieId(1).size());
        System.out.println(dbReader.getMovieRatingsByUserId(1).size());
        System.out.println(dbReader.getTagsByUserId(2).size());
        System.out.println(dbReader.getTagsByMovieId(1).size());
        System.out.println(dbReader.getMovieIds(200, 250).size());
    }
}
