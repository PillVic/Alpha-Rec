package com.alpharec;

import com.alpharec.data.Resource;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.alpharec.indexbuilder.MovieIndexBuilder.*;
import static com.alpharec.util.Flag.MovieFieldValue.MOVIE_TAG;

public class IndexTest {
    @Test
    public void MovieIndexTest() throws IOException {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        Resource r = context.getBean("resource", Resource.class);
        IndexSearcher searcher = r.getIndexSearcher(MOVIE_INDEX_PATH);

        String movieTag = "funny";
        List<Integer> movieIds = new ArrayList<>();
        TermQuery query = new TermQuery(new Term(MOVIE_TAG + "", movieTag));
        BooleanQuery bq = new BooleanQuery.Builder().add(query, BooleanClause.Occur.MUST).build();
        TopDocs docs = searcher.search(bq, Integer.MAX_VALUE);
        for (var doc : docs.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            movieIds.add(Integer.valueOf(document.get(MOVIE_ID)));
            System.out.println(document);
        }
        Collections.sort(movieIds);
        System.out.println(movieIds);
    }
}
