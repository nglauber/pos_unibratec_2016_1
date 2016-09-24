package br.com.nglauber.aula04_filmes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.nglauber.aula04_filmes.model.Movie;

public class MainActivity extends AppCompatActivity implements OnMovieClickListener {

    FloatingActionButton fab;
    LocalBroadcastManager mLocalBroadcastManager;
    MovieReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicializando o PagerAdapter, ViewPager e TabLayout para exibir as abas
        MoviesPagerAdapter pagerAdapter = new MoviesPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Definimos alguns comportamentos especiais para tablets...
        if (getResources().getBoolean(R.bool.tablet)){
            // As abas ficam alinhadas a esquerda
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            // Inicializamos esse receiver para saber quando o filme no fragment de detalhe
            // foi carregado (ver método notifyUpdate da DetailMovieFragment)
            mReceiver = new MovieReceiver();
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
            mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(MovieEvent.MOVIE_LOADED));
            mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(MovieEvent.MOVIE_FAVORITE_UPDATED));

            // O FAB envia a mensagem para o DetailFragment inserir/excluir filme no banco
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(MovieEvent.UPDATE_FAVORITE);
                    mLocalBroadcastManager.sendBroadcast(it);
                }
            });
        }
    }

    @Override
    public void onMovieClick(View view, Movie movie, int position) {
        // Esse método é chamado pelas telas de listagem quando o usuário
        // clica em um item da lista (ver MovieListFragment e FavoriteMoviesFragment)
        if (getResources().getBoolean(R.bool.phone)) {
            ActivityOptionsCompat optionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                            Pair.create(view.findViewById(R.id.movie_item_image_poster), "capa"),
                            Pair.create(view.findViewById(R.id.movie_item_text_title), "titulo"),
                            Pair.create(view.findViewById(R.id.movie_item_text_year), "ano"));

            // Se for smartphone, abra uma nova activity
            Intent it = new Intent(MainActivity.this, DetailActivity.class);
            it.putExtra(DetailActivity.EXTRA_MOVIE, movie);
            ActivityCompat.startActivity(this, it, optionsCompat.toBundle());

        } else {
            // Se for tablet, exiba um fragment a direita
            DetailMovieFragment detailMovieFragment = DetailMovieFragment.newInstance(movie);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.placeholderDetail, detailMovieFragment)
                    .commit();
        }
    }

    // Esse receiver será chamado quando o fragment de detalhe carrega os dados do filme
    // (ver método notifyUpdate de DetailMovieFragment)
    class MovieReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Movie movie = (Movie)intent.getSerializableExtra(MovieEvent.EXTRA_MOVIE);
            fab.setVisibility(View.VISIBLE);
            MovieDetailUtils.toggleFavorite(context, fab, movie.getImdbId());
        }
    }

    // O PagerAdapter é o que determina o que será exibido em cada aba
    class MoviesPagerAdapter extends FragmentPagerAdapter {
        public MoviesPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            if (position == 1){
                MovieListFragment movieListFragment = new MovieListFragment();
                return movieListFragment;
            } else {
                FavoriteMoviesFragment favoriteMoviesFragment = new FavoriteMoviesFragment();
                return favoriteMoviesFragment;
            }
        }
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 1) return getString(R.string.tab_search);
            else return getString(R.string.tab_favorites);
        }
        @Override
        public int getCount() {
            return 2;
        }
    }
}
