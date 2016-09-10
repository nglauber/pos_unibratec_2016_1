package br.com.nglauber.aula04_filmes.http;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import br.com.nglauber.aula04_filmes.model.Movie;

/**
 * Created by nglauber on 9/3/16.
 */
public class MovieByIdTask extends AsyncTaskLoader<Movie> {

    private Movie mMovie;
    private String mId;

    public MovieByIdTask(Context context, String id) {
        super(context);
        mId = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (mMovie == null) {
            forceLoad();
        } else {
            deliverResult(mMovie);
        }
    }

    @Override
    public Movie loadInBackground() {
        mMovie = MovieHttp.loadMovieById(mId);
        return mMovie;
    }
}
