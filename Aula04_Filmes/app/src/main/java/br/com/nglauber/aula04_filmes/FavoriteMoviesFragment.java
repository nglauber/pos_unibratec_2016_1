package br.com.nglauber.aula04_filmes;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.nglauber.aula04_filmes.database.MoviesProvider;


public class FavoriteMoviesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    MovieCursorAdapter mAdapter;

    public FavoriteMoviesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_movies, container, false);

        ListView listView = (ListView) view.findViewById(R.id.favorites_list);


        mAdapter = new MovieCursorAdapter(getActivity(), null);

        listView.setAdapter(mAdapter);

        getActivity().getSupportLoaderManager().initLoader(2, null, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MoviesProvider.MOVIES_URI, null, null, null, null);
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
