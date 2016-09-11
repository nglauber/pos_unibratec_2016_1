package br.com.nglauber.aula04_filmes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.nglauber.aula04_filmes.http.MovieByIdTask;
import br.com.nglauber.aula04_filmes.model.Movie;


public class DetailMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Movie> {

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

    String mMovieId;

    OnMovieLoadedListener mMovieLoadedListener;

    public static DetailMovieFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(EXTRA_MOVIE_ID, id);

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

        mMovieId = getArguments().getString(EXTRA_MOVIE_ID);
        getLoaderManager().initLoader(1, null, this);

        return view;
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new MovieByIdTask(getActivity(), mMovieId);
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie movie) {
        if (mMovieLoadedListener != null) {
            mMovieLoadedListener.onMovieLoaded(movie);
        }

//        Glide.with(imgPoster.getContext()).load(movie.getPoster()).into(imgPoster);
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
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
    }

    interface OnMovieLoadedListener {
        void onMovieLoaded(Movie movie);
    }
}
