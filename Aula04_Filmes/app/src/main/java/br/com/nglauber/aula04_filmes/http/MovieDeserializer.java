package br.com.nglauber.aula04_filmes.http;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.nglauber.aula04_filmes.model.Movie;

/**
 * Created by nglauber on 9/3/16.
 */
public class MovieDeserializer implements JsonDeserializer<Movie> {
    @Override
    public Movie deserialize(JsonElement json,
                             Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonObject = (JsonObject) json;
            Movie movie = new Movie();
            movie.setId(jsonObject.get("imdbID").getAsString());
            movie.setTitle(jsonObject.get("Title").getAsString());
            movie.setYear(jsonObject.get("Year").getAsString());
            movie.setPoster(jsonObject.get("Poster").getAsString());
            movie.setGenre(jsonObject.get("Genre").getAsString());
            movie.setDirector(jsonObject.get("Director").getAsString());
            movie.setPlot(jsonObject.get("Plot").getAsString());
            movie.setActors(jsonObject.get("Actors").getAsString().split(","));
            movie.setRuntime(jsonObject.get("Runtime").getAsString());

            String rating = jsonObject.get("imdbRating").getAsString();
            if (isDouble(rating)) movie.setRating(Float.parseFloat(rating));

            return movie;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
