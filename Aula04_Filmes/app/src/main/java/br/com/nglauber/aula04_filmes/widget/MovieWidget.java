package br.com.nglauber.aula04_filmes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Random;

import br.com.nglauber.aula04_filmes.R;

public class MovieWidget extends AppWidgetProvider {
    public static final String EXTRA_ACTION = "acao";
    public static final String ACTION_PREVIOUS = "anterior";
    public static final String ACTION_NEXT = "proximo";
    public static final String ACTION_DELETED = "deleted";
    public static final String ACTION_UPDATED = "updated";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        for (int i = 0; i < appWidgetIds.length; i++) {
            setButtonClicks(context, appWidgetIds[i], views);
        }
        appWidgetManager.updateAppWidget(appWidgetIds, views);

        // iniciando serviço para carregar a imagem assim que adicionar o widget na tela
        Intent it = new Intent(context, MovieWidgetService.class);
        it.putExtra(EXTRA_ACTION, ACTION_UPDATED);
        it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(it);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // Esse método é chamado quando um widget é removido da tela
        // Devemos chamar o service apenas para remover esses IDs da lista que temos na memória
        Intent it = new Intent(context, MovieWidgetService.class);
        it.putExtra(EXTRA_ACTION, ACTION_DELETED);
        it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(it);
    }

    private void setButtonClicks(Context context, int appWidgetId, RemoteViews views) {
        views.setOnClickPendingIntent(R.id.btnProximo,
                servicePendingIntent(context, ACTION_NEXT, appWidgetId));

        views.setOnClickPendingIntent(R.id.btnAnterior,
                servicePendingIntent(context, ACTION_PREVIOUS, appWidgetId));
    }

    private PendingIntent servicePendingIntent(Context ctx, String acao, int appWidgetId) {

        Intent serviceIntent = new Intent(ctx, MovieWidgetService.class);

        serviceIntent.putExtra(EXTRA_ACTION, acao);

        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        int requestId = new Random().nextInt();

        PendingIntent pit = PendingIntent.getService(ctx, requestId, serviceIntent, 0);
        return pit;
    }
}