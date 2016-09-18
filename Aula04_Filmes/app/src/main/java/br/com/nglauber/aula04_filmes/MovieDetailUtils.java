package br.com.nglauber.aula04_filmes;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;

import br.com.nglauber.aula04_filmes.database.MovieContract;
import br.com.nglauber.aula04_filmes.database.MoviesProvider;

public class MovieDetailUtils {

    public static boolean isFavorite(Context ctx, String imdbId){
        Cursor cursor = ctx.getContentResolver().query(
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

    public static void toggleFavorite(Context ctx, FloatingActionButton fab, String imdbId){
        if (MovieDetailUtils.isFavorite(ctx, imdbId)){
            fab.setImageResource(R.drawable.ic_favorite);
        } else {
            fab.setImageResource(R.drawable.ic_unfavorite);
        }
    }
}
