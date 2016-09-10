package br.com.nglauber.aula04_filmes;

import br.com.nglauber.aula04_filmes.model.Movie;

public interface OnMovieClickListener {
    void onMovieClick(Movie movie, int position);
}