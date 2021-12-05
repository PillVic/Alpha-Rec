package com.alpharec.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author pillvic
 * 封装mybatis的sqlsession
* */
public class MybatisUtils {
    private static SqlSessionFactory sqlSessionFactory;
    static{
        String resource = "mybatis-config.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SqlSession getSqlSession(boolean realWrite){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            if(realWrite){
                sqlSession.commit();
            }
            sqlSession.close();
        }));
        return sqlSession;
    }
}
