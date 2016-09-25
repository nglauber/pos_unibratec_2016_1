package br.com.nglauber.aula07;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import br.com.nglauber.aula07.service.Contador;
import br.com.nglauber.aula07.service.ContadorBinder;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    public static final String ACTION_VAI_FILHAO = "VAI_FILHAO";

    private MyReceiver mReceiver;
    private Contador mContador;

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

    public void iniciarServico(View view) {
        Intent it = new Intent(this, ContadorService.class);
        startService(it);
        bindService(it, this, 0);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mContador = ((ContadorBinder)service).getContador();
        atualizarTexto();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private void atualizarTexto() {
        final TextView text = (TextView)findViewById(R.id.textView);
        text.postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setText("Contador: "+ mContador.getCount());
                if (mContador.getCount() <= 19) {
                    text.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}
