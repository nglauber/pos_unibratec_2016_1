package br.com.nglauber.aula04_filmes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.nglauber.aula04_filmes.http.MovieHttp;
import br.com.nglauber.aula04_filmes.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "imdb_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new MovieTask().execute(getIntent().getStringExtra(EXTRA_ID));
    }

    class MovieTask extends AsyncTask<String, Void, Movie> {

        @Override
        protected Movie doInBackground(String... strings) {
            return MovieHttp.loadMovieById(strings[0]);
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            ImageView imgPoster   = (ImageView)findViewById(R.id.detail_image_poster);
            TextView  txtTitle    = (TextView)findViewById(R.id.detail_text_title);
            TextView  txtYear     = (TextView)findViewById(R.id.detail_text_year);
            TextView  txtGenre    = (TextView)findViewById(R.id.detail_text_genre);
            TextView  txtDirector = (TextView)findViewById(R.id.detail_text_director);
            TextView  txtPlot     = (TextView)findViewById(R.id.detail_text_plot);
            TextView  txtRuntime  = (TextView)findViewById(R.id.detail_text_runtime);
            RatingBar rating      = (RatingBar)findViewById(R.id.detail_rating);

            Glide.with(imgPoster.getContext()).load(movie.getPoster()).into(imgPoster);
            txtTitle.setText(movie.getTitle());
            txtYear.setText(movie.getYear());
            txtGenre.setText(movie.getGenre());
            txtDirector.setText(movie.getDirector());
            txtPlot.setText(movie.getPlot());
            txtRuntime.setText(movie.getRuntime());
            rating.setRating(movie.getRating() / 2);
        }
    }
}
