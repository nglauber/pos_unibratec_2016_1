package br.com.nglauber.exercicio01;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import br.com.nglauber.exercicio01.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_RESULTADO = "result";

    ActivityMainBinding mBinding;
    CombustivelModel mCombustivelModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState != null){
            mCombustivelModel = (CombustivelModel) savedInstanceState.getSerializable(EXTRA_RESULTADO);
        } else {
            mCombustivelModel = new CombustivelModel();
        }

        mBinding.setController(this);
        mBinding.setCombustivelModel(mCombustivelModel);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_RESULTADO, mCombustivelModel);
    }

    public void calcularClick(View view) {
        try {
            mCombustivelModel.calcular();
        } catch (Exception e){
            Toast.makeText(this, R.string.error_valores_invalidos, Toast.LENGTH_SHORT).show();
        }
    }

    public void verResultadoClick(View view){
        calcularClick(view);
        if (mCombustivelModel.getResultado() != CombustivelModel.RESULT_NENHUM) {
            Intent it = new Intent(this, ResultActivity.class);
            it.putExtra(ResultActivity.EXTRA_COMBUSTIVEL, mCombustivelModel);
            startActivity(it);
        }
    }

}
