package com.alpharec.data;

import com.alpharec.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component
public class Resource {
    public DbReader dbReader;
    public DbWriter dbWriter;

    public Resource() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        this.dbWriter = sqlSession.getMapper(DbWriter.class);
        this.dbReader = sqlSession.getMapper(DbReader.class);


        Runtime.getRuntime().addShutdownHook( new Thread(()->{
            sqlSession.commit();
            sqlSession.close();
        }));
    }
}
