package br.com.nglauber.aula04_filmes.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.nglauber.aula04_filmes.model.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nglauber on 9/3/16.
 */
public class MovieHttp {

    public static final String API_URL = "http://www.omdbapi.com/?s=%s&r=json";
    public static final String API_URL_ID = "http://www.omdbapi.com/?i=%s&plot=full&r=json";

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
            // from here!
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("Search");
            String jsonList = jsonArray.toString();

            Gson gson = new Gson();
            Movie[] moviesArray = gson.fromJson(jsonList, Movie[].class);
            movies.addAll(Arrays.asList(moviesArray));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public static Movie loadMovieById(String imdbId){
        OkHttpClient client = new OkHttpClient();
        String url = String.format(API_URL_ID, imdbId);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String json = response.body().string();

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Movie.class, new MovieDeserializer());
            Gson gson = gsonBuilder.create();
            Movie movie = gson.fromJson(json, Movie.class);
            return movie;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
