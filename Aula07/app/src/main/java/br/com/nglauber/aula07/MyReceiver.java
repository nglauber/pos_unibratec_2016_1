package br.com.nglauber.aula07;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(intent.getAction())) {

            boolean isOn = intent.getBooleanExtra("state", false);
            Toast.makeText(context, "Airplane mode! " + (isOn ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();

            Intent it = new Intent(context, MainActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);

        } else {
            Toast.makeText(context, "Vai filhão! ", Toast.LENGTH_SHORT).show();
        }
    }
}
