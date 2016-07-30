package br.com.nglauber.posunibratecaula1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Tela2Activity extends AppCompatActivity {
    public static final String EXTRA_NOME = "nome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela2);

        TextView textView = (TextView)findViewById(R.id.txt_conteudo);
        textView.setText(getIntent().getStringExtra(EXTRA_NOME));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
AlertDialog alertDialog = new AlertDialog.Builder(Tela2Activity.this, R.style.AppCompatAlertDialogStyle)
        .setTitle("Solicitar Permissões")
        .setView(R.layout.teste_dialog)
        .setPositiveButton("OK", null)
        .setNegativeButton("Cancel", null)
        .create();
alertDialog.show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("NGVL", "Tela2Activity --- > onCreate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("NGVL", "Tela2Activity --- > onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("NGVL", "Tela2Activity --- > onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NGVL", "Tela2Activity --- > onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("NGVL", "Tela2Activity --- > onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("NGVL", "Tela2Activity --- > onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("NGVL", "Tela2Activity --- > onDestroy");
    }
}
