package com.alpharec.pojo;

import com.alpharec.data.Handler;
import com.alpharec.util.Flag;

public class Movie {
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
        String[] v = line.split(",");
        this.movieId = Integer.parseInt(v[0]);
        if (v[2].equals(Flag.EMPTY_GENRES)) {
            System.out.println(v[2]);
        } else {
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
        return "Movie{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", genres='" + genres + '\'' +
                ", year=" + year +
                '}';
    }

    public static void main(String[] args) {
        String file = "DataSet/MovieLens/ml-latest-small/movies.csv";
        Handler handler = new Handler(file, (line) -> {
            Movie movie = new Movie(line);
            System.out.println(movie);
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
