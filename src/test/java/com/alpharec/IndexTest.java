package com.alpharec;

import com.alpharec.data.Resource;
import com.alpharec.item.MovieItem;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

import static com.alpharec.indexbuilder.MovieIndexBuilder.*;
import static com.alpharec.indexbuilder.UserIndexBuilder.USER_INDEX_PATH;
import static com.alpharec.util.Flag.MovieField.MOVIE_ID;
import static com.alpharec.util.Flag.MovieField.MOVIE_TAG;
import static com.alpharec.util.Flag.UserField.*;

public class IndexTest {
    public static final Resource r;

    static {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        r = context.getBean("resource", Resource.class);
    }

    @Test
    public void MovieIndexTest() throws IOException {
        IndexSearcher searcher = r.getIndexSearcher(MOVIE_INDEX_PATH);

        String movieTag = "funny";
        TermQuery query = new TermQuery(new Term(MOVIE_TAG + "", movieTag));
        BooleanQuery bq = new BooleanQuery.Builder().add(query, BooleanClause.Occur.MUST).build();
        TopDocs docs = searcher.search(bq, Integer.MAX_VALUE);
        for (var doc : docs.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            System.out.println(document.get(MOVIE_ID + ""));
        }
    }

    @Test
    public void UserIndexTest() throws IOException {
        IndexSearcher searcher = r.getIndexSearcher(USER_INDEX_PATH);

        String tag = "adventure";
        TermQuery query = new TermQuery(new Term(PREFER_GENRES + "", tag));
        BooleanQuery bq = new BooleanQuery.Builder().add(query, BooleanClause.Occur.MUST).build();
        TopDocs docs = searcher.search(bq, Integer.MAX_VALUE);
        for (var doc : docs.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            System.out.println(document.toString());
        }
    }
}
