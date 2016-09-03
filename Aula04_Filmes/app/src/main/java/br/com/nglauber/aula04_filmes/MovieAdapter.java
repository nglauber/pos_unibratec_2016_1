package br.com.nglauber.aula04_filmes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.nglauber.aula04_filmes.model.Movie;

/**
 * Created by nglauber on 9/3/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.VH> {

    private List<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(Context ctx, List<Movie> mMovies) {
        this.mContext = ctx;
        this.mMovies = mMovies;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.item_movie, parent, false);
        VH viewHolder = new VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Movie movie = mMovies.get(position);
        Glide.with(mContext).load(movie.getPoster()).into(holder.imageViewPoster);
        holder.textViewTitle.setText(movie.getTitle());
        holder.textViewYear.setText(movie.getYear());
        holder.ratingBar.setRating(movie.getRating());
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView imageViewPoster;
        TextView textViewTitle;
        TextView textViewYear;
        RatingBar ratingBar;

        public VH(View itemView) {
            super(itemView);
            imageViewPoster = (ImageView)itemView.findViewById(R.id.movie_item_image_poster);
            textViewTitle = (TextView) itemView.findViewById(R.id.movie_item_text_title);
            textViewYear = (TextView)itemView.findViewById(R.id.movie_item_text_year);
            ratingBar = (RatingBar)itemView.findViewById(R.id.movie_item_rating);
        }
    }
}
