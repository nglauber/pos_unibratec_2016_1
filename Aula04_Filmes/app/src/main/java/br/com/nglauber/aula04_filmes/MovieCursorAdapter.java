package br.com.nglauber.aula04_filmes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import br.com.nglauber.aula04_filmes.database.MovieContract;

public class MovieCursorAdapter extends SimpleCursorAdapter {

    private static final int LAYOUT = R.layout.item_movie;

    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, LAYOUT, c, MovieContract.ALL_COLUMNS, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(LAYOUT, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageViewPoster = (ImageView) view.findViewById(R.id.movie_item_image_poster);
        TextView textViewTitle = (TextView) view.findViewById(R.id.movie_item_text_title);
        TextView textViewYear = (TextView) view.findViewById(R.id.movie_item_text_year);

        String poster = cursor.getString(cursor.getColumnIndex(MovieContract.COL_POSTER));
        String title = cursor.getString(cursor.getColumnIndex(MovieContract.COL_TITLE));
        String year = cursor.getString(cursor.getColumnIndex(MovieContract.COL_YEAR));

        Glide.with(context).load(poster).into(imageViewPoster);
        textViewTitle.setText(title);
        textViewYear.setText(year);
    }
}
