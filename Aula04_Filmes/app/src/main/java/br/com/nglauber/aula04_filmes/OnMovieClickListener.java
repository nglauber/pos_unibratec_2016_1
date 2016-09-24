package br.com.nglauber.aula04_filmes;

import android.view.View;

import br.com.nglauber.aula04_filmes.model.Movie;

public interface OnMovieClickListener {
    void onMovieClick(View view, Movie movie, int position);
}