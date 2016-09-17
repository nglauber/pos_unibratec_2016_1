package br.com.nglauber.aula04_filmes;


import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.nglauber.aula04_filmes.database.MovieContract;
import br.com.nglauber.aula04_filmes.database.MoviesProvider;
import br.com.nglauber.aula04_filmes.http.MovieByIdTask;
import br.com.nglauber.aula04_filmes.model.Movie;


public class DetailMovieFragment extends Fragment {

    private static final String EXTRA_MOVIE_IMDB_ID = "imdbId";
    private static final String EXTRA_MOVIE_ID = "movieId";

    ImageView imgPoster;
    TextView  txtTitle;
    TextView  txtYear;
    TextView  txtGenre;
    TextView  txtDirector;
    TextView  txtPlot;
    TextView  txtRuntime;
    TextView  txtActors;
    RatingBar rating;

    long mMovieId;
    String mImdbId;

    OnMovieLoadedListener mMovieLoadedListener;

    // Web
    public static DetailMovieFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MOVIE_IMDB_ID, id);

        DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
        detailMovieFragment.setArguments(args);
        return detailMovieFragment;
    }
    // Local (Favoritos)
    public static DetailMovieFragment newInstance(long id) {
        Bundle args = new Bundle();
        args.putLong(EXTRA_MOVIE_ID, id);

        DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
        detailMovieFragment.setArguments(args);
        return detailMovieFragment;
    }

    public void setMovieLoadedListener(OnMovieLoadedListener l) {
        this.mMovieLoadedListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        imgPoster   = (ImageView)view.findViewById(R.id.detail_image_poster);
        txtTitle    = (TextView)view.findViewById(R.id.detail_text_title);
        txtYear     = (TextView)view.findViewById(R.id.detail_text_year);
        txtGenre    = (TextView)view.findViewById(R.id.detail_text_genre);
        txtDirector = (TextView)view.findViewById(R.id.detail_text_director);
        txtPlot     = (TextView)view.findViewById(R.id.detail_text_plot);
        txtRuntime  = (TextView)view.findViewById(R.id.detail_text_runtime);
        txtActors   = (TextView)view.findViewById(R.id.detail_text_actors);
        rating      = (RatingBar)view.findViewById(R.id.detail_rating);

        mMovieId = getArguments().getLong(EXTRA_MOVIE_ID);
        mImdbId = getArguments().getString(EXTRA_MOVIE_IMDB_ID);
        if (mMovieId > 0){
            getLoaderManager().initLoader(1, null, mCursorCallback);
        } else {
            getLoaderManager().initLoader(2, null, mMovieCallback);
        }

        return view;
    }

    LoaderManager.LoaderCallbacks mMovieCallback = new LoaderManager.LoaderCallbacks<Movie>() {
        @Override
        public Loader<Movie> onCreateLoader(int id, Bundle args) {
            return new MovieByIdTask(getActivity(), mImdbId);
        }

        @Override
        public void onLoadFinished(Loader<Movie> loader, Movie movie) {
            updateUI(movie);
        }

        @Override
        public void onLoaderReset(Loader<Movie> loader) {
        }
    };

    LoaderManager.LoaderCallbacks<Cursor> mCursorCallback = new LoaderManager.LoaderCallbacks<Cursor>(){

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(),
                    ContentUris.withAppendedId(MoviesProvider.MOVIES_URI, mMovieId),
                    null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null && cursor.moveToFirst()) {
                Movie movie = new Movie();
                movie.setImdbId(cursor.getString(cursor.getColumnIndex(MovieContract.COL_IMDB_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.COL_TITLE)));
                movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieContract.COL_POSTER)));
                movie.setYear(cursor.getString(cursor.getColumnIndex(MovieContract.COL_YEAR)));
                movie.setGenre(cursor.getString(cursor.getColumnIndex(MovieContract.COL_GENRE)));
                movie.setDirector(cursor.getString(cursor.getColumnIndex(MovieContract.COL_DIRECTOR)));
                movie.setPlot(cursor.getString(cursor.getColumnIndex(MovieContract.COL_PLOT)));
                movie.setActors(cursor.getString(cursor.getColumnIndex(MovieContract.COL_PLOT)).split(","));
                movie.setRuntime(cursor.getString(cursor.getColumnIndex(MovieContract.COL_RUNTIME)));
                movie.setRating(cursor.getFloat(cursor.getColumnIndex(MovieContract.COL_RATING)));
                updateUI(movie);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void updateUI(Movie movie){
        txtTitle.setText(movie.getTitle());
        txtYear.setText(movie.getYear());
        txtGenre.setText(movie.getGenre());
        txtDirector.setText(movie.getDirector());
        txtPlot.setText(movie.getPlot());
        txtRuntime.setText(movie.getRuntime());
        rating.setRating(movie.getRating() / 2);

        StringBuffer sb = new StringBuffer();
        for (String actor :
                movie.getActors()) {
            sb.append(actor).append('\n');
        }
        txtActors.setText(sb.toString());

        // atualizar imagem da capa
        if (mMovieLoadedListener != null) {
            mMovieLoadedListener.onMovieLoaded(movie);
        }
    }

    interface OnMovieLoadedListener {
        void onMovieLoaded(Movie movie);
    }
}
