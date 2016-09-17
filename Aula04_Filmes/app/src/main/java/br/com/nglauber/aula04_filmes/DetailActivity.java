package br.com.nglauber.aula04_filmes;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

    public static final String EXTRA_ID = "movie_id"; // vindo dos favoritos
    public static final String EXTRA_IMDB_ID = "imdb_id"; // vindo da web
    private Movie mMovie;
    private long mMovieId;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String imdbId = getIntent().getStringExtra(EXTRA_IMDB_ID);
        long id = getIntent().getLongExtra(EXTRA_ID, 0);

        DetailMovieFragment detailMovieFragment;
        if (id != 0){
            // Favoritos
            detailMovieFragment = DetailMovieFragment.newInstance(id);
        } else {
            // Busca da internet
            detailMovieFragment = DetailMovieFragment.newInstance(imdbId);
        }

        detailMovieFragment.setMovieLoadedListener(new DetailMovieFragment.OnMovieLoadedListener() {
            @Override
            public void onMovieLoaded(Movie movie) {
                mMovie = movie;
                ImageView imgPoster = (ImageView)findViewById(R.id.detail_image_poster);
                Glide.with(imgPoster.getContext()).load(mMovie.getPoster()).into(imgPoster);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        toggleFavorite(imdbId);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = isFavorite(mMovie.getImdbId());

                if (mMovie != null){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.COL_IMDB_ID , mMovie.getImdbId());
                    contentValues.put(MovieContract.COL_TITLE   , mMovie.getTitle());
                    contentValues.put(MovieContract.COL_YEAR    , mMovie.getYear());
                    contentValues.put(MovieContract.COL_POSTER  , mMovie.getPoster());
                    contentValues.put(MovieContract.COL_GENRE   , mMovie.getGenre());
                    contentValues.put(MovieContract.COL_DIRECTOR, mMovie.getDirector());
                    contentValues.put(MovieContract.COL_PLOT    , mMovie.getPlot());
                    contentValues.put(MovieContract.COL_ACTORS  , Arrays.toString(mMovie.getActors()));
                    contentValues.put(MovieContract.COL_RUNTIME , mMovie.getRuntime());
                    contentValues.put(MovieContract.COL_RATING  , mMovie.getRating());

                    if (isFavorite) {
                        getContentResolver().delete(
                                ContentUris.withAppendedId(MoviesProvider.MOVIES_URI, mMovieId),
                                null, null);
                    } else {
                        Uri uri = getContentResolver().insert(MoviesProvider.MOVIES_URI, contentValues);
                        mMovieId = ContentUris.parseId(uri);
                    }
                }
                toggleFavorite(mMovie.getImdbId());

                Snackbar.make(view,
                        isFavorite ? R.string.msg_removed_favorites : R.string.msg_added_favorites,
                        Snackbar.LENGTH_LONG)
                        .setAction(R.string.text_undo, this).show();
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.placeholderDetail, detailMovieFragment)
                .commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null){
            mMovieId = savedInstanceState.getLong(EXTRA_ID);
        } else {
            mMovieId = id;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(EXTRA_ID, mMovieId);
    }

    private boolean isFavorite(String imdbId){
        Cursor cursor = getContentResolver().query(
                MoviesProvider.MOVIES_URI,
                new String[]{ MovieContract._ID },
                MovieContract.COL_IMDB_ID +" = ?",
                new String[]{ imdbId },
                null
        );
        boolean isFavorite = false;
        if (cursor != null) {
            isFavorite = cursor.getCount() > 0;
            cursor.close();
        }
        return isFavorite;
    }

    private void toggleFavorite(String imdbId){
        if (isFavorite(imdbId)){
            fab.setImageResource(R.drawable.ic_favorite);
        } else {
            fab.setImageResource(R.drawable.ic_unfavorite);
        }
    }
}
