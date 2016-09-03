package br.com.nglauber.aula04_filmes.http;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.nglauber.aula04_filmes.model.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nglauber on 9/3/16.
 */
public class MovieHttp {

    public static final String API_URL = "http://www.omdbapi.com/?t=%s&y=&plot=full&r=json";

    public static List<Movie> searchMovies(String query){
        List<Movie> movies = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        String url = String.format(API_URL, query);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String json = response.body().string();
            Gson gson = new Gson();
            Movie movie = gson.fromJson(json, Movie.class);
            movies.add(movie);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }
}
