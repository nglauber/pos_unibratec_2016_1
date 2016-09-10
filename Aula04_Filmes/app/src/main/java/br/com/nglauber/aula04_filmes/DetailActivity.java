package br.com.nglauber.aula04_filmes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import br.com.nglauber.aula04_filmes.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "imdb_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String id = getIntent().getStringExtra(EXTRA_ID);
        DetailMovieFragment detailMovieFragment =
                DetailMovieFragment.newInstance(id);
        detailMovieFragment.setMovieLoadedListener(new DetailMovieFragment.OnMovieLoadedListener() {
            @Override
            public void onMovieLoaded(Movie movie) {
                ImageView imgPoster = (ImageView)findViewById(R.id.detail_image_poster);
                Glide.with(imgPoster.getContext()).load(movie.getPoster()).into(imgPoster);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.placeholderDetail, detailMovieFragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
