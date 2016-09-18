package br.com.nglauber.aula04_filmes.database;

import android.provider.BaseColumns;

public interface MovieContract extends BaseColumns {
    // Nome da tabela no banco de dados
    String TABLE_NAME = "Movies";

    // Colunas do banco de dados
    String COL_IMDB_ID  = "imdbID";
    String COL_TITLE    = "Title";
    String COL_YEAR     = "Year";
    String COL_POSTER   = "Poster";
    String COL_GENRE    = "Genre";
    String COL_DIRECTOR = "Director";
    String COL_PLOT     = "Plot";
    String COL_ACTORS   = "Actors";
    String COL_RUNTIME  = "Runtime";
    String COL_RATING   = "imdbRating";

    // Colunas utilizadas pelo adapter do fragment de favoritos
    String[] LIST_COLUMNS = new String[]{
            MovieContract._ID,
            MovieContract.COL_IMDB_ID,
            MovieContract.COL_TITLE,
            MovieContract.COL_POSTER,
            MovieContract.COL_YEAR
    };
}
