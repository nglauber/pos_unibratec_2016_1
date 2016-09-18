package br.com.nglauber.aula04_filmes.http;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import br.com.nglauber.aula04_filmes.model.Movie;

// Classe que customiza o GSON para fazer a leitura do JSON vindo do servidor.
// Utilizada na classe MovieHttp
public class MovieDeserializer implements JsonDeserializer<Movie> {

    @Override
    public Movie deserialize(JsonElement json,
                             Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonObject = (JsonObject) json;
            Movie movie = new Movie();
            movie.setImdbId(jsonObject.get("imdbID").getAsString());
            movie.setTitle(jsonObject.get("Title").getAsString());
            movie.setYear(jsonObject.get("Year").getAsString());
            movie.setPoster(jsonObject.get("Poster").getAsString());
            movie.setGenre(jsonObject.get("Genre").getAsString());
            movie.setDirector(jsonObject.get("Director").getAsString());
            movie.setPlot(jsonObject.get("Plot").getAsString());
            movie.setActors(jsonObject.get("Actors").getAsString().split(","));
            movie.setRuntime(jsonObject.get("Runtime").getAsString());
            movie.setRating(asFloat(jsonObject.get("imdbRating").getAsString()));

            return movie;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    float asFloat(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
