package br.com.nglauber.aula04_filmes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "dbMovies";
    private static final int DB_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+ MovieContract.TABLE_NAME +" (" +
                        MovieContract._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieContract.COL_IMDB_ID  +" TEXT UNIQUE NOT NULL, "+
                        MovieContract.COL_TITLE    +" TEXT NOT NULL, "+
                        MovieContract.COL_YEAR     +" TEXT NOT NULL, "+
                        MovieContract.COL_POSTER   +" TEXT NOT NULL, "+
                        MovieContract.COL_GENRE    +" TEXT NOT NULL, "+
                        MovieContract.COL_DIRECTOR +" TEXT NOT NULL, "+
                        MovieContract.COL_PLOT     +" TEXT NOT NULL, "+
                        MovieContract.COL_ACTORS   +" TEXT NOT NULL, "+
                        MovieContract.COL_RUNTIME  +" TEXT NOT NULL, "+
                        MovieContract.COL_RATING   +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
