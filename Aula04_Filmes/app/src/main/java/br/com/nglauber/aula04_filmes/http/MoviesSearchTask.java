package br.com.nglauber.aula04_filmes.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import br.com.nglauber.aula04_filmes.model.Movie;

// Essa tarefa carrega a lista dos filmes baseado nos parâmetros da busca
public class MoviesSearchTask extends AsyncTaskLoader<List<Movie>> {
    List<Movie> movies;
    String query;

    public MoviesSearchTask(Context context, String query, List<Movie> movies) {
        super(context);
        this.query = query;
        this.movies = new ArrayList<>();
        if (movies != null) {
            this.movies.addAll(movies);
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (query != null) {
            forceLoad();
        } else {
            deliverResult(movies);
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        movies.addAll(MovieHttp.searchMovies(query));
        return movies;
    }
}