package com.alpharec.data;

import com.alpharec.JavaConfig;
import com.alpharec.util.MybatisUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.mariadb.jdbc.internal.util.scheduler.FixedSizedSchedulerImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 所有外部资源都通过spring获得这个类的各个接口来进行访问
 * @author pillvic
* */
@Component
public class Resource {
    public static final int THREAD_POOL_SIZE = 5;
    public DbReader dbReader;
    public DbWriter dbWriter;
    public ThreadPoolExecutor threadPoolExecutor;

    public Resource() {
        this.dbWriter = MybatisUtils.getSqlSession(true).getMapper(DbWriter.class);
        this.dbReader = MybatisUtils.getSqlSession(false).getMapper(DbReader.class);
        this.threadPoolExecutor = new FixedSizedSchedulerImpl(THREAD_POOL_SIZE,"resource thread pool");
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

    public static Resource getResource(){
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        return context.getBean("resource", Resource.class);
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
