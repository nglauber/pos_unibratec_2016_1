package br.com.nglauber.aula04_filmes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.nglauber.aula04_filmes.http.MoviesSearchTask;
import br.com.nglauber.aula04_filmes.model.Movie;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String QUERY_PARAM = "param";
    public static final int LOADER_ID = 0;

    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;
    List<Movie> mMoviesList;
    LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesList = new ArrayList<>();
        mAdapter = new MovieAdapter(this, mMoviesList);
        mAdapter.setMovieClickListener(new MovieAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie, int position) {
                Intent it = new Intent(MainActivity.this, DetailActivity.class);
                it.putExtra(DetailActivity.EXTRA_ID, movie.getId());
                startActivity(it);
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.main_recycler_movies);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        mRecyclerView.setAdapter(mAdapter);

        mLoaderManager = getSupportLoaderManager();
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    // ---- OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String query) {
        Bundle params = new Bundle();
        params.putString(QUERY_PARAM, query);
        mLoaderManager.restartLoader(LOADER_ID, params, this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    // ---- LoaderManager.LoaderCallbacks
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        String s = args != null ? args.getString(QUERY_PARAM) : null;
        return new MoviesSearchTask(this, s, mMoviesList);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        if (data != null && data.size() > 0){
            mMoviesList.clear();
            mMoviesList.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
    }
}
