package br.com.nglauber.aula07;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import br.com.nglauber.aula07.service.Contador;
import br.com.nglauber.aula07.service.ContadorBinder;

public class ContadorService extends Service implements Contador {

    int numero;

    public ContadorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ContadorBinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (int i = 0; i < 20; i++){
                    numero = i;
                    Log.d("NGVL", "CONTADOR: "+ i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public int getCount() {
        return numero;
    }
}
