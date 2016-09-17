package br.com.nglauber.aula04_filmes.database;

import android.provider.BaseColumns;

public interface MovieContract extends BaseColumns {
    String TABLE_NAME = "Movies";

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

    String[] ALL_COLUMNS = new String[] {
            _ID         ,
            COL_IMDB_ID ,
            COL_TITLE   ,
            COL_YEAR    ,
            COL_POSTER  ,
            COL_GENRE   ,
            COL_DIRECTOR,
            COL_PLOT    ,
            COL_ACTORS  ,
            COL_RUNTIME ,
            COL_RATING  ,
    };
    String[] LIST_COLUMNS = new String[]{
            MovieContract._ID,
            MovieContract.COL_IMDB_ID,
            MovieContract.COL_TITLE,
            MovieContract.COL_POSTER,
            MovieContract.COL_YEAR
    };
}
