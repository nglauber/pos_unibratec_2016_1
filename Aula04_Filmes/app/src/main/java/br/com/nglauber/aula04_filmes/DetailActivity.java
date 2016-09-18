package br.com.nglauber.aula04_filmes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import br.com.nglauber.aula04_filmes.model.Movie;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie"; // vindo dos favoritos

    Movie mMovie;
    FloatingActionButton fab;

    LocalBroadcastManager mLocalBroadcastManager;
    MovieReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // A MainActivity passará um objeto Movie,
        // então criamos o fragment de detalhe com esse objeto
        mMovie = (Movie)getIntent().getSerializableExtra(EXTRA_MOVIE);
        DetailMovieFragment detailMovieFragment;
        detailMovieFragment = DetailMovieFragment.newInstance(mMovie);

        // Todas as informações do filme estão no DetailMovieFragment,
        // exceto a capa que já carregamos aqui, uma vez que essa informação
        // já existe no objeto Movie.
        ImageView imgPoster = (ImageView)findViewById(R.id.detail_image_poster);
        Glide.with(imgPoster.getContext()).load(mMovie.getPoster()).into(imgPoster);

        // Esse receiver detectará se o Movie foi adicionado ou removido dos favoritos
        // TODO Substituir pelo EventBus?
        mReceiver = new MovieReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(MovieEvent.MOVIE_LOADED));
        mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(MovieEvent.MOVIE_FAVORITE_UPDATED));

        // O FAB faz parte do layout da Activity, mas precisa ser atualizado
        // quando o movie é inserido ou removido dos favoritos. mReceiver fará isso ;)
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Quando clicamos no botão, estamos avisando ao fragment de detalhes para
                // inserir/remover o Movie no banco. Veja DetailMovieFragment.MovieEventReceiver.
                Intent it = new Intent(MovieEvent.UPDATE_FAVORITE);
                mLocalBroadcastManager.sendBroadcast(it);
            }
        });

        if (savedInstanceState == null) {
            // Adicionando o fragment de detalhes na tela
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.placeholderDetail, detailMovieFragment)
                    .commit();
        }

        //TODO barra de status transparente?
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Esse receiver atualizará o status do botão de favoritos.
    class MovieReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            fab.setVisibility(View.VISIBLE);
            Movie movie = (Movie)intent.getSerializableExtra(MovieEvent.EXTRA_MOVIE);
            MovieDetailUtils.toggleFavorite(context, fab, movie.getImdbId());
        }
    }
}
