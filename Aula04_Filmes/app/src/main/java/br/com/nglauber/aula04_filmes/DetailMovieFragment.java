package br.com.nglauber.aula04_filmes;


import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Arrays;

import br.com.nglauber.aula04_filmes.database.MovieContract;
import br.com.nglauber.aula04_filmes.database.MoviesProvider;
import br.com.nglauber.aula04_filmes.http.MovieByIdTask;
import br.com.nglauber.aula04_filmes.model.Movie;


public class DetailMovieFragment extends Fragment {

    private static final String EXTRA_MOVIE = "movie";
    private static final int LOADER_DB = 0;
    private static final int LOADER_WEB = 1;

    ImageView imgPoster;
    TextView  txtTitle;
    TextView  txtYear;
    TextView  txtGenre;
    TextView  txtDirector;
    TextView  txtPlot;
    TextView  txtRuntime;
    TextView  txtActors;
    RatingBar rating;

    Movie mMovie;
    LocalBroadcastManager mLocalBroadcastManager;
    MovieEventReceiver mReceiver;
    ShareActionProvider mShareActionProvider;
    Intent mShareIntent;

    // Para criarmos um DetailMovieFragment precisamos passar um objeto Movie
    public static DetailMovieFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MOVIE, movie);

        DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
        detailMovieFragment.setArguments(args);
        return detailMovieFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inicializando o layout do fragment...
        View view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        imgPoster   = (ImageView)view.findViewById(R.id.detail_image_poster);
        txtTitle    = (TextView)view.findViewById(R.id.detail_text_title);
        txtYear     = (TextView)view.findViewById(R.id.detail_text_year);
        txtGenre    = (TextView)view.findViewById(R.id.detail_text_genre);
        txtDirector = (TextView)view.findViewById(R.id.detail_text_director);
        txtPlot     = (TextView)view.findViewById(R.id.detail_text_plot);
        txtRuntime  = (TextView)view.findViewById(R.id.detail_text_runtime);
        txtActors   = (TextView)view.findViewById(R.id.detail_text_actors);
        rating      = (RatingBar)view.findViewById(R.id.detail_rating);

        // Animação de transição de tela
        ViewCompat.setTransitionName(imgPoster, "capa");
        ViewCompat.setTransitionName(txtTitle, "titulo");
        ViewCompat.setTransitionName(txtYear, "ano");

        // Inicializamos mMovie (ver onSaveInsatnceState)
        if (savedInstanceState == null){
            // Se não tem um estado anterior, use o que foi passado no método newInstance.
            mMovie = (Movie)getArguments().getSerializable(EXTRA_MOVIE);
        } else {
            // Se há um estado anterior, use-o
            mMovie = (Movie)savedInstanceState.getSerializable(EXTRA_MOVIE);
        }

        // Se o objeto mMovie possui um ID (no banco local), carregue do banco local,
        // senão carregue do servidor.
        boolean isFavorite = MovieDetailUtils.isFavorite(getActivity(), mMovie.getImdbId());
        if (isFavorite){
            // Faz a requisição em background ao banco de dados (ver mCursorCallback)
            getLoaderManager().initLoader(LOADER_DB, null, mCursorCallback);
        } else {
            // Faz a requisição em background ao servidor (ver mMovieCallback)
            getLoaderManager().initLoader(LOADER_WEB, null, mMovieCallback);
        }

        // Registramos o receiver para tratar sabermos quando o botão de favoritos da
        // activity de detalhes foi chamado.
        mReceiver = new MovieEventReceiver();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        mLocalBroadcastManager.registerReceiver(mReceiver, new IntentFilter(MovieEvent.UPDATE_FAVORITE));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Precisamos manter o objeto mMovie atualizado pois ele pode ter sido
        // incluído e excluído dos favoritos.
        outState.putSerializable(EXTRA_MOVIE, mMovie);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Desregistramos o receiver ao destruir a View do fragment
        mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (mShareIntent != null){
            mShareActionProvider.setShareIntent(mShareIntent);
        }
    }

    // --------------- LoaderManager.LoaderCallbacks<Movie>
    // Esse callback trata o retorno da requisição feita ao servidor
    LoaderManager.LoaderCallbacks mMovieCallback = new LoaderManager.LoaderCallbacks<Movie>() {
        @Override
        public Loader<Movie> onCreateLoader(int id, Bundle args) {
            // inicializa a requisição em background para o servidor usando AsyncTaskLoader
            // (veja a classe MovieByIdTask)
            return new MovieByIdTask(getActivity(), mMovie.getImdbId());
        }

        @Override
        public void onLoadFinished(Loader<Movie> loader, Movie movie) {
            updateUI(movie);
        }

        @Override
        public void onLoaderReset(Loader<Movie> loader) {
        }
    };

    // --------------- LoaderManager.LoaderCallbacks<Cursor>
    // Esse callback trata o retorno da requisição feita ao servidor
    LoaderManager.LoaderCallbacks<Cursor> mCursorCallback = new LoaderManager.LoaderCallbacks<Cursor>(){

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // inicializa a requisição em background para o ContentProvider usando CursorLoader
            // perceba que estamos utilizando a Uri específica
            // (veja o método query do MovieProvider)
            return new CursorLoader(getActivity(),
                    MoviesProvider.MOVIES_URI,
                    null,
                    MovieContract.COL_IMDB_ID +" = ?",
                    new String[]{ mMovie.getImdbId() }, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            // Ao receber o retorno do cursor, criamos um objeto Movie e preenchemos a tela
            // (ver updateUI)
            if (cursor != null && cursor.moveToFirst()) {
                Movie movie = new Movie();
                movie.setId(cursor.getLong(cursor.getColumnIndex(MovieContract._ID)));
                movie.setImdbId(cursor.getString(cursor.getColumnIndex(MovieContract.COL_IMDB_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.COL_TITLE)));
                movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieContract.COL_POSTER)));
                movie.setYear(cursor.getString(cursor.getColumnIndex(MovieContract.COL_YEAR)));
                movie.setGenre(cursor.getString(cursor.getColumnIndex(MovieContract.COL_GENRE)));
                movie.setDirector(cursor.getString(cursor.getColumnIndex(MovieContract.COL_DIRECTOR)));
                movie.setPlot(cursor.getString(cursor.getColumnIndex(MovieContract.COL_PLOT)));
                movie.setActors(cursor.getString(cursor.getColumnIndex(MovieContract.COL_PLOT)).split(","));
                movie.setRuntime(cursor.getString(cursor.getColumnIndex(MovieContract.COL_RUNTIME)));
                movie.setRating(cursor.getFloat(cursor.getColumnIndex(MovieContract.COL_RATING)));
                updateUI(movie);
            }


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };

    private void createShareIntent(Movie movie) {
        mShareIntent = new Intent(Intent.ACTION_SEND);
        mShareIntent.setType("text/plain");
        mShareIntent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_text, movie.getTitle(), movie.getPlot()));
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(mShareIntent);
        }
    }

    // --------------- INNER
    // Esse receiver é chamado pelo FAB da DetailActivity para iniciar o processo
    // de inserir/excluir o movie nos favoritos
    class MovieEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(MovieEvent.UPDATE_FAVORITE)) {
                toggleFavorite();
            }
        }
    }

    // --------------- PRIVATE

    private void updateUI(Movie movie){
        // Atualiza o objeto mMovie com os dados vindos dos callbacks
        // (ver mCursorCallback e mMovieCallback)
        mMovie = movie;
        txtTitle.setText(movie.getTitle());
        txtYear.setText(movie.getYear());
        txtGenre.setText(movie.getGenre());
        txtDirector.setText(movie.getDirector());
        txtPlot.setText(movie.getPlot());
        txtRuntime.setText(movie.getRuntime());
        rating.setRating(movie.getRating() / 2);

        // Tanto no JSON quanto no Banco, estamos salvando a lista
        // de atores separado por vírgula
        //TODO Criar uma nova tabela e fazer chave estrangeira
        StringBuffer sb = new StringBuffer();
        for (String actor :
                movie.getActors()) {
            sb.append(actor).append('\n');
        }
        txtActors.setText(sb.toString());

        // Enviando mensagem para todos que querem saber que o filme carregou
        // (ver DetailActivity.MovieReceiver)
        notifyUpdate(MovieEvent.MOVIE_LOADED);

        // Quando estiver em tablet, exiba o poster no próprio fragment
        if (getResources().getBoolean(R.bool.tablet)){
            imgPoster.setVisibility(View.VISIBLE);
            Glide.with(imgPoster.getContext()).load(movie.getPoster()).into(imgPoster);
        }
        createShareIntent(movie);
    }

    // Método auxiliar que insere/remove o movie no banco de dados
    private void toggleFavorite() {
        if (mMovie == null) return; // isso não deve acontecer...

        // Primeiro verificamos se o livro está no banco de dados
        boolean isFavorite = MovieDetailUtils.isFavorite(getActivity(), mMovie.getImdbId());

        boolean success = false;
        if (isFavorite) {
            // Se já é favorito, exclua
            if (deleteFavorite(mMovie.getId())){
                success = true;
                mMovie.setId(0);
                getLoaderManager().destroyLoader(LOADER_DB);
            }
            //TODO Mensagem de erro ao excluir

        } else {
            // Se não é favorito, inclua...
            long id = insertFavorite(mMovie);
            success = id > 0;
            mMovie.setId(id);
        }

        // Se deu tudo certo...
        if (success) {
            // Envia a mensagem para as activities (para atualizar o FAB)
            notifyUpdate(MovieEvent.MOVIE_FAVORITE_UPDATED);

            // Exibe o snackbar que permite o "desfazer"
            //TODO Internailizar a aplicação
            Snackbar.make(getView(),
                    isFavorite ? R.string.msg_removed_favorites : R.string.msg_added_favorites,
                    Snackbar.LENGTH_LONG)
                    .setAction(R.string.text_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toggleFavorite();
                        }
                    }).show();
        }
    }

    private void notifyUpdate(String action){
        // Cria a intent e dispara o broadcast
        Intent it = new Intent(action);
        it.putExtra(MovieEvent.EXTRA_MOVIE, mMovie);
        mLocalBroadcastManager.sendBroadcast(it);
    }

    // Método auxiliar para excluir nos favoritos
    //TODO fazer delete em background
    private boolean deleteFavorite(long movieId){
        return getActivity().getContentResolver().delete(
                ContentUris.withAppendedId(MoviesProvider.MOVIES_URI, movieId),
                null, null) > 0;
    }

    // Método auxiliar para inserir nos favoritos
    //TODO fazer insert em background
    private long insertFavorite(Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.COL_IMDB_ID , movie.getImdbId());
        contentValues.put(MovieContract.COL_TITLE   , movie.getTitle());
        contentValues.put(MovieContract.COL_YEAR    , movie.getYear());
        contentValues.put(MovieContract.COL_POSTER  , movie.getPoster());
        contentValues.put(MovieContract.COL_GENRE   , movie.getGenre());
        contentValues.put(MovieContract.COL_DIRECTOR, movie.getDirector());
        contentValues.put(MovieContract.COL_PLOT    , movie.getPlot());
        contentValues.put(MovieContract.COL_ACTORS  , Arrays.toString(movie.getActors()));
        contentValues.put(MovieContract.COL_RUNTIME , movie.getRuntime());
        contentValues.put(MovieContract.COL_RATING  , movie.getRating());

        Uri uri = getActivity().getContentResolver().insert(MoviesProvider.MOVIES_URI, contentValues);
        //TODO mensagem de erro ao falhar
        return ContentUris.parseId(uri);
    }
}
