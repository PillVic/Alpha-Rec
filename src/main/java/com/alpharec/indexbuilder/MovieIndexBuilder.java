package com.alpharec.indexbuilder;

import com.alpharec.JavaConfig;
import com.alpharec.data.Resource;
import com.alpharec.item.MovieItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;

public class MovieIndexBuilder implements Runnable{
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
        index(3);
    }

    private void indexRange(int minMovieId, int maxMovieId){
    }

    private void index(int movieId){
        MovieItem movieItem = new MovieItem(r.dbReader.getMovieById(movieId));
        for(var tag: r.dbReader.getTagsByMovieId(movieId)){
            movieItem.addTag(tag.getTag());
        }
        for(var rating:r.dbReader.getMovieRatingsByMovieId(movieId)){
            movieItem.addRate(rating.getRating());
        }
    }

    public static void main(String[] args) {
        MovieIndexBuilder movieIndexBuilder = new MovieIndexBuilder();
        movieIndexBuilder.run();
    }
}
