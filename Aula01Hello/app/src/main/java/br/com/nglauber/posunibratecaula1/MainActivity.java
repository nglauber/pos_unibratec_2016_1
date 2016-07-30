package br.com.nglauber.posunibratecaula1;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import br.com.nglauber.posunibratecaula1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_CONTEUDO = "conteudo";
    ActivityMainBinding mBinding;
    String texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setBonitao(this);

        if (savedInstanceState != null){
            texto = savedInstanceState.getString(EXTRA_CONTEUDO);
            mBinding.textResultado.setText(texto);
        }

        Log.d("NGVL", "MainActivity --- > onCreate");
    }

    public boolean textLongClick(View view){
        Toast.makeText(MainActivity.this, "Clicou de com força!", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void buttonTela2Click(View view){
        Intent it = new Intent(this, Tela2Activity.class);
        it.putExtra(Tela2Activity.EXTRA_NOME, mBinding.editNome.getText().toString());
        startActivity(it);
    }

    public void buttonNomeClick(View view) {
        texto = mBinding.editNome.getText().toString();
        Toast.makeText(MainActivity.this, texto, Toast.LENGTH_SHORT).show();
        mBinding.textResultado.setText(texto);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_CONTEUDO, texto);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("NGVL", "MainActivity --- > onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("NGVL", "MainActivity --- > onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("NGVL", "MainActivity --- > onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("NGVL", "MainActivity --- > onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("NGVL", "MainActivity --- > onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("NGVL", "MainActivity --- > onDestroy");
    }
}
