package br.com.nglauber.aula07;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_VAI_FILHAO = "VAI_FILHAO";

    private MyReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(ACTION_VAI_FILHAO);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public void enviarBroadcast(View view) {
        sendBroadcast(new Intent(ACTION_VAI_FILHAO));
    }
}
