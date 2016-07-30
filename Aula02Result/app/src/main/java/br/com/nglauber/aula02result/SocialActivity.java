package br.com.nglauber.aula02result;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import br.com.nglauber.aula02result.databinding.ActivitySocialBinding;

public class SocialActivity extends AppCompatActivity {

    public static final String EXTRA_REDE_SOCIAL = "rede_social";

    ActivitySocialBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_social);
        int redeSocial = getIntent().getIntExtra(EXTRA_REDE_SOCIAL, Usuario.REDE_FACEBOOK);
        switch (redeSocial){
            case Usuario.REDE_FACEBOOK: mBinding.radioGroupSocial.check(R.id.radioFacebook); break;
            case Usuario.REDE_GPLUS:    mBinding.radioGroupSocial.check(R.id.radioGPlus);    break;
            case Usuario.REDE_TWITTER:  mBinding.radioGroupSocial.check(R.id.radioTwitter);  break;
        }
    }

    public void selecionarClick(View view) {
        int retorno;
        switch (mBinding.radioGroupSocial.getCheckedRadioButtonId()){
            case R.id.radioGPlus   : retorno = Usuario.REDE_GPLUS;    break;
            case R.id.radioTwitter : retorno = Usuario.REDE_TWITTER;  break;
            default                : retorno = Usuario.REDE_FACEBOOK; break;
        }

        Intent it = new Intent();
        it.putExtra(EXTRA_REDE_SOCIAL, retorno);
        setResult(RESULT_OK, it);
        finish();
    }
}
