package com.alpharec.pojo;

import com.alpharec.JavaConfig;
import com.alpharec.data.DbWriter;
import com.alpharec.data.Handler;
import com.alpharec.data.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.alpharec.util.ObjectAnalyzer.toJsonString;

/**
 * @author pillvic
* */
public class Link {
    private static final int VALID_COL_NUM = 3;
    private static final String LINK_FILE = "Data/MovieLens/ml-latest-small/links.csv";
    private int movieId;
    private int imdbId;
    private int tmdbId;

    public Link(int movieId, int imdbId, int tmdbId) {
        this.movieId = movieId;
        this.imdbId = imdbId;
        this.tmdbId = tmdbId;
    }

    public Link() {
    }

    public Link(String line) {
        String[] cols = line.split(",");
        this.movieId = Integer.parseInt(cols[0]);
        this.imdbId = Integer.parseInt(cols[1]);
        if (cols.length == VALID_COL_NUM) {
            this.tmdbId = Integer.parseInt(cols[2]);
        }
        System.out.println(this);
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getImdbId() {
        return imdbId;
    }

    public void setImdbId(int imdbId) {
        this.imdbId = imdbId;
    }

    public int getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(int tmdbId) {
        this.tmdbId = tmdbId;
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(JavaConfig.class);
        Resource resource = context.getBean("resource", Resource.class);

        DbWriter dbWriter = resource.dbWriter;

        Handler h = new Handler(LINK_FILE, (line)->{
            Link link = new Link(line);
            dbWriter.insertLink(link);
        });
        Thread r = new Thread(h, "write link");
        r.start();
        try {
            r.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
