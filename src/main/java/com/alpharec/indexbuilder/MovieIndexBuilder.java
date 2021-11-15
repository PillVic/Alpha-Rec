package com.alpharec.indexbuilder;

import com.alpharec.JavaConfig;
import com.alpharec.data.Resource;
import com.alpharec.item.MovieItem;
import com.alpharec.pojo.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class MovieIndexBuilder implements Runnable {
    private static Logger log = LoggerFactory.getLogger(MovieIndexBuilder.class);
    public static int threads = 20;
    private ThreadPoolExecutor work;
    private Resource r;

    @Override
    public void run() {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);

        this.r = context.getBean("resource", Resource.class);
        int minMovieId = r.dbReader.getMinMovieId();
        int maxMovieId = r.dbReader.getMaxMovieId();
        log.info("[INFO]: minMovieId:{}, maxMovieId:{}", minMovieId, maxMovieId);
        List<MovieItem> movieItems = indexRange(minMovieId, maxMovieId);
        for (var movieItem : movieItems) {
            Movie movie = movieItem.getMovie();
            log.info("id:{}, name:{}", movie.getMovieId(), movie.getTitle());
        }
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
