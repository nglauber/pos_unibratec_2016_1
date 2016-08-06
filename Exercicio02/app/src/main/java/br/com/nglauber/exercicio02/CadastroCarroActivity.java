package br.com.nglauber.exercicio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CadastroCarroActivity extends AppCompatActivity {

    public static final String EXTRA_CARRO = "carro";
    EditText mEditModelo;
    EditText mEditAno;
    Spinner mSpnFabricante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_carro);

        mEditModelo = (EditText)findViewById(R.id.edit_modelo);
        mEditAno = (EditText)findViewById(R.id.edit_ano);
        mSpnFabricante = (Spinner)findViewById(R.id.spinner_fabricante);
    }

    public void salvarClick(View view) {
        try {
            Carro carro = new Carro();

            carro.setModelo(mEditModelo.getText().toString());
            carro.setAno(Integer.parseInt(mEditAno.getText().toString()));
            carro.setFabricante(mSpnFabricante.getSelectedItemPosition());

            Intent it = new Intent();
            it.putExtra(EXTRA_CARRO, carro);
            setResult(RESULT_OK, it);
            finish();

        } catch (Exception e){
            Toast.makeText(this, R.string.erro_cadastro, Toast.LENGTH_SHORT).show();
        }
    }
}
