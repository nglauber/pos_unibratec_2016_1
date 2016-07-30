package br.com.nglauber.aula02result;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import br.com.nglauber.aula02result.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_REDE_SOCIAL = 1;
    private static final String EXTRA_USUARIO = "usuario";

    ActivityMainBinding mBinding;
    Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (savedInstanceState == null){
            mUsuario = new Usuario();
        } else {
            mUsuario = (Usuario)savedInstanceState.getSerializable(EXTRA_USUARIO);
        }
        mBinding.setUsuario(mUsuario);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_USUARIO, mUsuario);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REDE_SOCIAL && resultCode == RESULT_OK){
            mUsuario.setRedeSocial(
                    data.getIntExtra(SocialActivity.EXTRA_REDE_SOCIAL, Usuario.REDE_FACEBOOK));
        }
    }

    public void redeSocialClick(View view) {
        Intent it = new Intent(this, SocialActivity.class);
        it.putExtra(SocialActivity.EXTRA_REDE_SOCIAL, mUsuario.getRedeSocial());
        startActivityForResult(it, REQUEST_REDE_SOCIAL);
    }

    public void resultadoClick(View view) {
        String[] redes = getResources().getStringArray(R.array.redes_sociais);
        String rede = redes[mUsuario.getRedeSocial()];
        String s = getString(R.string.mensagem_usuario, mUsuario.getNome(), rede);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
