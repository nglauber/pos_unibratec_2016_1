package br.com.nglauber.aula07.task;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import br.com.nglauber.aula07.MainActivity;
import br.com.nglauber.aula07.R;

public class MyTaskService extends GcmTaskService {

    @Override
    public int onRunTask(TaskParams taskParams) {
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(resultPendingIntent)
                .setContentTitle("GCM Network manager")
                .setContentText(taskParams.getTag())
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        NotificationManagerCompat.from(this).notify(0, notification);
        return GcmNetworkManager.RESULT_SUCCESS;
    }
}