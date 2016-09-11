package br.com.nglauber.aula04_filmes;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Arrays;

import br.com.nglauber.aula04_filmes.database.MovieContract;
import br.com.nglauber.aula04_filmes.database.MoviesProvider;
import br.com.nglauber.aula04_filmes.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "imdb_id";
    private Movie mMovie;

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
                mMovie = movie;
                ImageView imgPoster = (ImageView)findViewById(R.id.detail_image_poster);
                Glide.with(imgPoster.getContext()).load(mMovie.getPoster()).into(imgPoster);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie != null){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.COL_IMDB_ID , mMovie.getId());
                    contentValues.put(MovieContract.COL_TITLE   , mMovie.getTitle());
                    contentValues.put(MovieContract.COL_YEAR    , mMovie.getYear());
                    contentValues.put(MovieContract.COL_POSTER  , mMovie.getPoster());
                    contentValues.put(MovieContract.COL_GENRE   , mMovie.getGenre());
                    contentValues.put(MovieContract.COL_DIRECTOR, mMovie.getDirector());
                    contentValues.put(MovieContract.COL_PLOT    , mMovie.getPlot());
                    contentValues.put(MovieContract.COL_ACTORS  , Arrays.toString(mMovie.getActors()));
                    contentValues.put(MovieContract.COL_RUNTIME , mMovie.getRuntime());
                    contentValues.put(MovieContract.COL_RATING  , mMovie.getRating());

                    getContentResolver().insert(MoviesProvider.MOVIES_URI, contentValues);
                }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
