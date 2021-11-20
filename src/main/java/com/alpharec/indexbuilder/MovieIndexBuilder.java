package com.alpharec.indexbuilder;

import com.alpharec.JavaConfig;
import com.alpharec.data.Resource;
import com.alpharec.item.MovieItem;
import com.alpharec.pojo.Movie;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.alpharec.util.Flag.MovieFieldValue.MOVIE_GENRES;
import static com.alpharec.util.Flag.MovieFieldValue.MOVIE_TAG;

public class MovieIndexBuilder implements Runnable {

    public static final String MOVIE_ID = "MOVIE_ID";

    private static final Logger log = LoggerFactory.getLogger(MovieIndexBuilder.class);
    public static int threads = 20;
    public static final String MOVIE_INDEX_PATH = "Data/Index/MovieIndex";
    private Resource r;

    @Override
    public void run() {
        try {

            ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);

            this.r = context.getBean("resource", Resource.class);
            int minMovieId = r.dbReader.getMinMovieId();
            int maxMovieId = r.dbReader.getMaxMovieId();
            log.info("[INFO]: minMovieId:{}, maxMovieId:{}", minMovieId, maxMovieId);
            List<MovieItem> movieItems = indexRange(minMovieId, maxMovieId);

            IndexWriter indexWriter = r.getIndexWriter(MOVIE_INDEX_PATH);
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
        if (movieItem == null) return doc;
        doc.add(new TextField(MOVIE_ID, movieItem.getMovie().getMovieId() + "", Field.Store.YES));
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
        return doc;
    }

    private List<MovieItem> indexRange(int minMovieId, int maxMovieId) {
        List<Integer> movieIds = r.dbReader.getMovieIds(minMovieId, maxMovieId);
        List<MovieItem> movieItems = new ArrayList<>();
        for (var movieId : movieIds) {
            MovieItem movieItem = index(movieId);
            movieItems.add(movieItem);
        }
        return movieItems;
    }

    private MovieItem index(int movieId) {
        MovieItem movieItem = new MovieItem(r.dbReader.getMovieById(movieId));
        for (var tag : r.dbReader.getTagsByMovieId(movieId)) {
            movieItem.addTag(tag.getTag());
        }
        for (var rating : r.dbReader.getMovieRatingsByMovieId(movieId)) {
            movieItem.addRate(rating.getRating());
        }
        return movieItem;
    }

    public static void main(String[] args) {
        MovieIndexBuilder movieIndexBuilder = new MovieIndexBuilder();
        movieIndexBuilder.run();
    }
}
