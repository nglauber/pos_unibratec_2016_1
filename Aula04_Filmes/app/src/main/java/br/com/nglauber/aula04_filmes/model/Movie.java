package br.com.nglauber.aula04_filmes.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nglauber on 9/3/16.
 */
public class Movie {
    long id;
    @SerializedName("imdbID")
    private String imdbId;
    @SerializedName("Title")
    private String   title;
    @SerializedName("Year")
    private String   year;
    @SerializedName("Poster")
    private String   poster;
    @SerializedName("Genre")
    private String   genre;
    @SerializedName("Director")
    private String   director;
    @SerializedName("Plot")
    private String   plot;
    @SerializedName("Actors")
    private String[] actors;
    @SerializedName("Runtime")
    private String   runtime;
    @SerializedName("imdbRating")
    private float    rating;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String id) {
        this.imdbId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String[] getActors() {
        return actors;
    }

    public void setActors(String[] actors) {
        this.actors = actors;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
