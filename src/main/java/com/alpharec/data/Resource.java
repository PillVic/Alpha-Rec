package com.alpharec.data;

import com.alpharec.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;

@Component
public class Resource {
    public DbReader dbReader;
    public DbWriter dbWriter;

    public Resource() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        this.dbWriter = sqlSession.getMapper(DbWriter.class);
        this.dbReader = sqlSession.getMapper(DbReader.class);


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sqlSession.commit();
            sqlSession.close();
        }));
    }

    public IndexWriter getIndexWriter(String indexPath) throws IOException {
        Directory directory = new NIOFSDirectory(Paths.get(indexPath));

        Analyzer analyzer = new KeywordAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        IndexWriter indexWriter = new IndexWriter(directory, config);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                indexWriter.commit();
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        return indexWriter;
    }

    public IndexSearcher getIndexSearcher(String indexPath) throws IOException {
        Directory directory = new NIOFSDirectory(Paths.get(indexPath));
        IndexReader indexReader = DirectoryReader.open(directory);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                indexReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        return new IndexSearcher(indexReader);
    }
}
