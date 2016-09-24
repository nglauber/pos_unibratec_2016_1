package br.com.nglauber.aula04_filmes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewCompat;
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
        super(context, LAYOUT, c, MovieContract.LIST_COLUMNS, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(LAYOUT, parent, false);

        VH vh = new VH();
        vh.imageViewPoster = (ImageView) view.findViewById(R.id.movie_item_image_poster);
        vh.textViewTitle = (TextView) view.findViewById(R.id.movie_item_text_title);
        vh.textViewYear = (TextView) view.findViewById(R.id.movie_item_text_year);
        view.setTag(vh);

        ViewCompat.setTransitionName(vh.imageViewPoster, "capa");
        ViewCompat.setTransitionName(vh.textViewTitle, "titulo");
        ViewCompat.setTransitionName(vh.textViewYear, "ano");

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String poster = cursor.getString(cursor.getColumnIndex(MovieContract.COL_POSTER));
        String title = cursor.getString(cursor.getColumnIndex(MovieContract.COL_TITLE));
        String year = cursor.getString(cursor.getColumnIndex(MovieContract.COL_YEAR));

        VH vh = (VH)view.getTag();
        Glide.with(context)
                .load(poster)
                .placeholder(R.drawable.ic_placeholder)
                .into(vh.imageViewPoster);
        vh.textViewTitle.setText(title);
        vh.textViewYear.setText(year);
    }

    class VH {
        ImageView imageViewPoster;
        TextView textViewTitle;
        TextView textViewYear;
    }
}
