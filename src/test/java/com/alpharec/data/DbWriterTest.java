package com.alpharec.data;

import com.alpharec.pojo.Link;
import com.alpharec.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class DbWriterTest {
    @Test
    public void writeLinks(){
        String file = "/home/neovic/Work/RecommendSystem/DataSet/MovieLens/ml-latest-small/links.csv";
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
