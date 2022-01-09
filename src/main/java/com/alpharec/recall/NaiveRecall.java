package com.alpharec.recall;

import com.alpharec.data.Resource;
import com.alpharec.indexbuilder.MovieIndexBuilder;
import com.alpharec.indexbuilder.UserIndexBuilder;
import com.alpharec.pojo.Rating;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.alpharec.util.Flag.MovieField.*;
import static com.alpharec.util.Flag.UserField.*;
import static com.alpharec.util.ObjectAnalyzer.getTopN;


/**
 * 最基础的推荐算法，召回高分后去掉看过的
 */
public class NaiveRecall {
    private static final Logger logger = LoggerFactory.getLogger(NaiveRecall.class);

    public static List<Integer> recall(int userId, int topNum) {
        try {
            Document userDoc = getUserDoc(userId);
            if (userDoc != null) {
                List<String> preferGenres = Arrays.stream(userDoc.getValues(PREFER_GENRES + "")).toList();
                if (!CollectionUtils.isEmpty(preferGenres)) {
                    List<Integer> rawMovies = getTopMovies(getHotMovieFromGenres(preferGenres), topNum);
                    Set<Integer> seenMovies = Resource.getResource().dbReader.getMovieRatingsByUserId(userId)
                            .stream().map(Rating::getMovieId).collect(Collectors.toSet());
                    return rawMovies.stream().filter(t -> !seenMovies.contains(t)).toList();
                }
            }
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("[ERROR]:NaiveRecall", e);
        }
        return Collections.emptyList();
    }

    private static Document getUserDoc(int userId) throws IOException {
        Resource r = Resource.getResource();
        IndexSearcher searcher = r.getIndexSearcher(UserIndexBuilder.USER_INDEX_PATH);

        TermQuery query = new TermQuery(new Term(USER_ID + "", userId + ""));
        BooleanQuery bq = new BooleanQuery.Builder().add(query, BooleanClause.Occur.MUST).build();
        TopDocs docs = searcher.search(bq, Integer.MAX_VALUE);
        if (docs.scoreDocs.length == 0) {
            return null;
        }
        return searcher.doc(docs.scoreDocs[0].doc);
    }

    private static List<Document> getHotMovieFromGenres(List<String> genres) throws IOException {
        if (!CollectionUtils.isEmpty(genres)) {
            IndexSearcher searcher = Resource.getResource().getIndexSearcher(MovieIndexBuilder.MOVIE_INDEX_PATH);
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            for (var g : genres) {
                builder.add(new TermQuery(new Term(MOVIE_GENRES + "", g)), BooleanClause.Occur.SHOULD);
            }
            TopDocs docs = searcher.search(builder.build(), Integer.MAX_VALUE);
            if (docs.scoreDocs.length != 0) {
                List<Document> movieDocs = new ArrayList<>();
                for (var doc : docs.scoreDocs) {
                    movieDocs.add(searcher.doc(doc.doc));
                }
                return movieDocs;
            }
        }
        return Collections.emptyList();
    }

    private static List<Integer> getTopMovies(List<Document> movieDocs, int topNum) {
        return getTopN(topNum, movieDocs,
                Comparator.comparingDouble(a -> Double.parseDouble(a.get(MOVIE_AVERAGE + ""))))
                .stream().map(t -> Integer.parseInt(t.get(MOVIE_ID + ""))).toList();
    }

    public static void main(String[] args) {
        System.out.println(recall(5, 20));
    }
}
