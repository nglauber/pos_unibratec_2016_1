package br.com.nglauber.aula04_filmes;

public class MovieEvent {
    // Eventos disparados/recebidos pelos Broadcasts da aplicação
    public static final String MOVIE_LOADED = "loaded";
    public static final String UPDATE_FAVORITE = "favorite";
    public static final String MOVIE_FAVORITE_UPDATED = "updated";

    // Chave para obter o Movie a partir das intents de broadcast
    public static final String EXTRA_MOVIE = "movie";
}
