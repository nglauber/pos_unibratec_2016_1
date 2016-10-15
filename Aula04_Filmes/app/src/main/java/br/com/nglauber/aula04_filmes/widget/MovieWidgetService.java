package br.com.nglauber.aula04_filmes.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.util.SparseIntArray;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

import br.com.nglauber.aula04_filmes.DetailActivity;
import br.com.nglauber.aula04_filmes.MovieDetailUtils;
import br.com.nglauber.aula04_filmes.R;
import br.com.nglauber.aula04_filmes.database.MoviesProvider;
import br.com.nglauber.aula04_filmes.model.Movie;

import static android.R.attr.id;

public class MovieWidgetService extends Service {

    SparseIntArray mWidgetIds;
    AsyncTask mTask;

    @Override
    public void onCreate() {
        super.onCreate();
        mWidgetIds = new SparseIntArray();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Se o movie widget disparou alguma...
        if (intent != null) {
            String action = intent.getStringExtra(MovieWidget.EXTRA_ACTION);
            if (action != null) {

                // Se for a ação de remover o widget, remove da lista e sai do método...
                if (MovieWidget.ACTION_DELETED.equals(action)) {
                    int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                    for (int id : widgetIds) {
                        mWidgetIds.delete(id);
                    }
                    return super.onStartCommand(intent, flags, startId);
                }

                // A ação de updated é quando qualquer widget é adicionado na tela
                // ou se por qualquer motivo o widget é atualizado
                if (MovieWidget.ACTION_UPDATED.equals(action)) {
                    int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                    for (int id : widgetIds) {
                        updateAppWidget(id, action);
                    }

                } else {
                    // Aqui tratamos as outras ações (next e previous)
                    // ID do widget que deve ser atualizado
                    final int appWidgetId = intent.getIntExtra(
                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                            AppWidgetManager.INVALID_APPWIDGET_ID);

                    updateAppWidget(appWidgetId, action);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateAppWidget(final int appWidgetId, String acao) {
        // Pode existir várias instâncias do mesmo widget, então temos que armazenar a
        // posição de cada instância baseado no seu ID
        int position = 0;
        if (mWidgetIds.indexOfKey(appWidgetId) > -1){
            position = mWidgetIds.get(appWidgetId);
        }

        Cursor cursor = getContentResolver().query(
                MoviesProvider.MOVIES_URI, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0){
            return;
        }

        // Verificando qual posição do cursor que devemos mover
        if (MovieWidget.ACTION_NEXT.equals(acao)) {
            position++;
            if (position >= cursor.getCount()) {
                position = 0;
            }

        } else if (MovieWidget.ACTION_PREVIOUS.equals(acao)) {
            position--;
            if (position < 0) {
                position = cursor.getCount() - 1;
            }
        }

        cursor.moveToPosition(position);

        // Atualizando posição corrente no sparse array.
        mWidgetIds.put(appWidgetId, position);

        // Lendo a URL da capa e o título a partir do cursor
        final Movie movie = MovieDetailUtils.movieItemFromCursor(cursor);

        // Se já está baixando uma imagem, cancele...
        if (mTask != null) mTask.cancel(true);

        // A imagem deve ser carregada em background
        mTask = new AsyncTask<Void, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(Void... params) {
                try {
                    // Baixando o bitmap com o Glide (feio, com o Picasso é mais elegante)
                    Bitmap bitmap = Glide.with(MovieWidgetService.this)
                            .load(movie.getPoster())
                            .asBitmap()
                            .into(-1, -1)
                            .get();
                    return bitmap;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                // Atualiza o widget...
                updateAppWidget(appWidgetId, movie, bitmap);
            }
        }.execute();
    }

    private void updateAppWidget(int widgetId, Movie movie, Bitmap bitmap) {
        // Remote Views é pq o widget roda no processo da Home/Launcher activity
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.widget_layout);

        // Atualiza poster e título
        views.setImageViewBitmap(R.id.image_poster, bitmap);
        views.setTextViewText(R.id.text_title, movie.getTitle());

        if (getResources().getBoolean(R.bool.phone)) {
            Intent answerIntent = new Intent(this, DetailActivity.class);
            answerIntent.putExtra(DetailActivity.EXTRA_MOVIE, movie);

            PendingIntent pit = TaskStackBuilder.create(this)
                    .addParentStack(DetailActivity.class)
                    .addNextIntent(answerIntent)
                    .getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.image_poster, pit);
        }
        // Atualiza o widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // Não usado aqui
        return null;
    }
}