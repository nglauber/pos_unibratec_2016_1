package br.com.nglauber.aula04_filmes.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MoviesProvider extends ContentProvider {

    private static final String PATH = "movies";
    private static final int TYPE_MOVIES_LIST = 0;
    private static final int TYPE_MOVIE_BY_ID = 1;
    private static final String AUTHORITY = "br.com.nglauber.movies";

    public static Uri BASE_URI = Uri.parse("content://"+ AUTHORITY);
    public static Uri MOVIES_URI = BASE_URI.withAppendedPath(BASE_URI, PATH);

    private UriMatcher mMatcher;
    private MovieDBHelper mHelper;

    public MoviesProvider() {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTHORITY, PATH, TYPE_MOVIES_LIST);
        mMatcher.addURI(AUTHORITY, PATH +"/#", TYPE_MOVIE_BY_ID);
    }

    @Override
    public boolean onCreate() {
        mHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int uriType = mMatcher.match(uri);
        switch (uriType){
            case TYPE_MOVIES_LIST:
                return ContentResolver.CURSOR_DIR_BASE_TYPE +"/"+ AUTHORITY;
            case TYPE_MOVIE_BY_ID:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE +"/"+ AUTHORITY;
            default:
                throw new IllegalArgumentException("Invalid Uri");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = mMatcher.match(uri);
        if (uriType == TYPE_MOVIES_LIST){
            SQLiteDatabase db = mHelper.getWritableDatabase();
            long id = db.insert(MovieContract.TABLE_NAME, null, values);
            db.close();
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(MOVIES_URI, id);

        } else {
            throw new IllegalArgumentException("Invalid Uri");
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = mMatcher.match(uri);
        if (uriType == TYPE_MOVIE_BY_ID){
            SQLiteDatabase db = mHelper.getWritableDatabase();
            long id = ContentUris.parseId(uri);
            int rowsAffected = db.delete(
                    MovieContract.TABLE_NAME,
                    MovieContract._ID +" = ?",
                    new String[] { String.valueOf(id) } );
            db.close();
            getContext().getContentResolver().notifyChange(uri, null);
            return rowsAffected;

        } else {
            throw new IllegalArgumentException("Invalid Uri");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new IllegalArgumentException("Invalid Uri");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int uriType = mMatcher.match(uri);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor;
        switch (uriType){
            case TYPE_MOVIES_LIST:
                cursor = db.query(MovieContract.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case TYPE_MOVIE_BY_ID:
                long id = ContentUris.parseId(uri);
                cursor = db.query(MovieContract.TABLE_NAME,
                        projection, MovieContract._ID +" = ?",
                        new String[] { String.valueOf(id) }, null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Invalid Uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
}
