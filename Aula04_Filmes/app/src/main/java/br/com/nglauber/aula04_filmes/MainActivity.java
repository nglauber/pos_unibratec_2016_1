package br.com.nglauber.aula04_filmes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.nglauber.aula04_filmes.model.Movie;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MovieListFragment movieListFragment = (MovieListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentList);
        movieListFragment.setMovieClickListener(new OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie, int position) {
                if (getResources().getBoolean(R.bool.phone)) {
                    // Phone
                    Intent it = new Intent(MainActivity.this, DetailActivity.class);
                    it.putExtra(DetailActivity.EXTRA_ID, movie.getId());
                    startActivity(it);
                } else {
                    // Tablet
                    DetailMovieFragment detailMovieFragment =
                            DetailMovieFragment.newInstance(movie.getId());
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.placeholderDetail, detailMovieFragment)
                            .commit();
                }
            }
        });
    }
}
