package com.alpharec.pojo;

import com.alpharec.data.DbWriter;
import com.alpharec.data.Handler;
import com.alpharec.util.Flag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.alpharec.data.Resource.getResource;
import static com.alpharec.util.ObjectAnalyzer.getHashCode;
import static com.alpharec.util.ObjectAnalyzer.toJsonString;

public class Movie {
    private static final String TITLE_CONTAIN_YEAR = ".*\\([0-9]{4}\\)";
    private static final String YEAR_PATTERN = "[0-9]{4}";
    private static final Pattern LEAGAL_MOVIE_PATTERN = Pattern.compile("[0-9]+.*");
    private int movieId;
    private String title;
    private String genres;
    private int year;

    public Movie(int movieId, String title, String genres, int year) {
        this.movieId = movieId;
        this.title = title;
        this.genres = genres;
        this.year = year;
    }

    public Movie() {
    }

    public Movie(String line) {
        String[] v;
        if (line.contains("\"")) {
            v = line.split("(,\")|(\",)");
        } else {
            v = line.split(",");
        }
        this.movieId = Integer.parseInt(v[0]);

        v[1] = v[1].trim();
        if (v[1].matches(TITLE_CONTAIN_YEAR)) {
            int begin = v[1].length();
            Pattern yearPattern = Pattern.compile(YEAR_PATTERN);
            Matcher matcher = yearPattern.matcher(v[1]);
            while (matcher.find()) {
                this.year = Integer.parseInt(matcher.group());
                begin = matcher.start();
            }
            this.title = v[1].substring(0, begin - 1).trim();
        } else {
            this.title = v[1];
        }

        if (!Flag.EMPTY_GENRES.equals(v[2])) {
            this.genres = v[2];
        }
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }

    @Override
    public int hashCode() {
        return getHashCode(this);
    }

    public static void main(String[] args) {
        DbWriter dbWriter = getResource().dbWriter;

        String file = "Data/MovieLens/ml-latest-small/movies.csv";
        Handler handler = new Handler(file, (line) -> {
            System.out.println(line);
            if (line != null && LEAGAL_MOVIE_PATTERN.matcher(line).find()) {
                Movie movie = new Movie(line);
                dbWriter.insertMovie(movie);
                System.out.println(line);
            }
        });
        Thread r = new Thread(handler, "write link");
        r.start();
        try {
            r.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
