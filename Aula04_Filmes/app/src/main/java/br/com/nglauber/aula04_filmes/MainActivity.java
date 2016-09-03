package br.com.nglauber.aula04_filmes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.nglauber.aula04_filmes.http.MovieHttp;
import br.com.nglauber.aula04_filmes.model.Movie;

public class MainActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    RecyclerView mRecyclerView;
    MovieAdapter mAdapter;
    List<Movie> mMoviesList;

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
//                Toast.makeText(MainActivity.this, movie.getId() +" - "+ movie.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.main_recycler_movies);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        new MoviesTask().execute(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    class MoviesTask extends AsyncTask<String, Void, List<Movie>>{

        @Override
        protected List<Movie> doInBackground(String... strings) {
            return MovieHttp.searchMovies(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            super.onPostExecute(movies);
            if (movies != null && movies.size() > 0){
                mMoviesList.addAll(movies);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
