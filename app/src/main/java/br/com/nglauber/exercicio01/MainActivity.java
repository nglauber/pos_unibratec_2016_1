package br.com.nglauber.exercicio01;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import br.com.nglauber.exercicio01.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_NENHUM = 0;
    public static final int RESULT_ALCOOL = 1;
    public static final int RESULT_GASOLINA = 2;
    private static final String EXTRA_RESULTADO = "result";

    ActivityMainBinding mBinding;

    int mResultado = RESULT_NENHUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setController(this);

        if (savedInstanceState != null){
            mResultado = savedInstanceState.getInt(EXTRA_RESULTADO);
            atualizarTela();
        }
    }

    private void atualizarTela() {
        if (mResultado == RESULT_GASOLINA){
            mBinding.textResultado.setText(R.string.texto_resultado_gasolina);
        } else if (mResultado == RESULT_ALCOOL){
            mBinding.textResultado.setText(R.string.texto_resultado_alcool);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_RESULTADO, mResultado);
    }

    public void calcularClick(View view) {
        String textoGas = mBinding.editGasolina.getText().toString();
        String textoAlcool = mBinding.editAlcool.getText().toString();

        try {
            double gas = Double.parseDouble(textoGas);
            double alcool = Double.parseDouble(textoAlcool);

            if (gas * 0.7 > alcool){
                mResultado = RESULT_ALCOOL;
            } else {
                mResultado = RESULT_GASOLINA;
            }
            atualizarTela();

        } catch (Exception e){
            Toast.makeText(MainActivity.this, R.string.error_valores_invalidos, Toast.LENGTH_SHORT).show();
        }
    }
}
