package br.com.nglauber.exercicio01;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.nglauber.exercicio01.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_COMBUSTIVEL = "comb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CombustivelModel combustivelModel = (CombustivelModel)
                getIntent().getSerializableExtra(EXTRA_COMBUSTIVEL);

        ActivityResultBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_result);
        binding.setComb(combustivelModel);
    }
}
