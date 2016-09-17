package br.com.nglauber.aula04_filmes;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import br.com.nglauber.aula04_filmes.database.MovieContract;
import br.com.nglauber.aula04_filmes.database.MoviesProvider;
import br.com.nglauber.aula04_filmes.model.Movie;


public class FavoriteMoviesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    OnMovieClickListener mMovieClickListener;
    MovieCursorAdapter mAdapter;

    public FavoriteMoviesFragment() {
    }

    public void setMovieClickListener(OnMovieClickListener mMovieClickListener) {
        this.mMovieClickListener = mMovieClickListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Essa abordagem é mais usada, e mais rápida
        // entretanto requer um atributo adicional
        if (context instanceof OnMovieClickListener) {
            mMovieClickListener = (OnMovieClickListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_movies, container, false);

        ListView listView = (ListView) view.findViewById(R.id.favorites_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mMovieClickListener != null) {
                    Cursor cursor = mAdapter.getCursor();
                    if (cursor.moveToPosition(position)) {
                        Movie movie = new Movie();
                        movie.setId(cursor.getLong(cursor.getColumnIndex(MovieContract._ID)));
                        movie.setImdbId(cursor.getString(cursor.getColumnIndex(MovieContract.COL_IMDB_ID)));
                        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.COL_TITLE)));
                        movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieContract.COL_POSTER)));
                        movie.setYear(cursor.getString(cursor.getColumnIndex(MovieContract.COL_YEAR)));
                        mMovieClickListener.onMovieClick(movie, position);
                    }
                }
            }
        });

        mAdapter = new MovieCursorAdapter(getActivity(), null);

        listView.setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().initLoader(2, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MoviesProvider.MOVIES_URI,
                MovieContract.LIST_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
