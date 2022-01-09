package com.alpharec.indexbuilder;

import com.alpharec.JavaConfig;
import com.alpharec.data.Resource;
import com.alpharec.item.MovieItem;
import com.alpharec.pojo.Movie;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.alpharec.util.Flag.MovieField.*;

/**
 * 用于构建movie的倒排索引
 *
 * @author pillvic
 */
public class MovieIndexBuilder implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(MovieIndexBuilder.class);
    public static int threads = 20;
    public static final String MOVIE_INDEX_PATH = "Data/Index/MovieIndex";
    private static final Resource RESOURCE;

    static {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        RESOURCE = context.getBean("resource", Resource.class);
    }

    @Override
    public void run() {
        try {
            int minMovieId = RESOURCE.dbReader.getMinMovieId();
            int maxMovieId = RESOURCE.dbReader.getMaxMovieId();
            log.info("[INFO]: minMovieId:{}, maxMovieId:{}", minMovieId, maxMovieId);
            List<MovieItem> movieItems = indexRange(minMovieId, maxMovieId);

            IndexWriter indexWriter = RESOURCE.getIndexWriter(MOVIE_INDEX_PATH);
            for (var movieItem : movieItems) {
                Movie movie = movieItem.getMovie();
                log.info("id:{}, name:{}", movie.getMovieId(), movie.getTitle());
                indexWriter.addDocument(getMovieDoc(movieItem));
            }
        } catch (Exception e) {
            log.error("[ERROR]", e);
        }
    }

    private Document getMovieDoc(MovieItem movieItem) {
        Document doc = new Document();
        if (movieItem == null) {
            return doc;
        }
        doc.add(new TextField(MOVIE_ID + "", movieItem.getMovie().getMovieId() + "", Field.Store.YES));
        Set<String> tags = movieItem.getTags();
        if (tags != null && !tags.isEmpty()) {
            for (var tag : movieItem.getTags()) {
                doc.add(new TextField(MOVIE_TAG + "", tag, Field.Store.YES));
            }
        }
        List<String> genres = movieItem.getGenreList();
        if (genres != null && !genres.isEmpty()) {
            for (var gen : genres) {
                doc.add(new TextField(MOVIE_GENRES + "", gen, Field.Store.YES));
            }
        }
        doc.add(new DoublePoint(MOVIE_TOTAL_SCORE + "", movieItem.getTotalRate()));
        doc.add(new IntPoint(MOVIE_SEEN + "", movieItem.getSeenCount()));
        doc.add(new DoublePoint(MOVIE_AVERAGE + "", movieItem.getAverage()));

        doc.add(new StoredField(MOVIE_TOTAL_SCORE+"", movieItem.getTotalRate()+""));
        doc.add(new StoredField(MOVIE_SEEN+"", movieItem.getSeenCount()+""));
        doc.add(new StoredField(MOVIE_AVERAGE + "", movieItem.getAverage() + ""));
        doc.add(new StoredField(MOVIE_YEAR + "", movieItem.getMovie().getYear() + ""));
        return doc;
    }

    public static List<MovieItem> indexRange(int minMovieId, int maxMovieId) {
        List<Integer> movieIds = RESOURCE.dbReader.getMovieIds(minMovieId, maxMovieId);
        List<MovieItem> movieItems = new ArrayList<>();
        for (var movieId : movieIds) {
            MovieItem movieItem = buildMovieItem(movieId);
            movieItems.add(movieItem);
        }
        return movieItems;
    }

    public static MovieItem buildMovieItem(int movieId) {
        MovieItem movieItem = new MovieItem(RESOURCE.dbReader.getMovieById(movieId));
        for (var tag : RESOURCE.dbReader.getTagsByMovieId(movieId)) {
            movieItem.addTag(tag.getTag());
        }
        for (var rating : RESOURCE.dbReader.getMovieRatingsByMovieId(movieId)) {
            movieItem.addRate(rating.getRating());
        }
        return movieItem;
    }

    public static void main(String[] args) {
        MovieIndexBuilder movieIndexBuilder = new MovieIndexBuilder();
        movieIndexBuilder.run();
    }
}
