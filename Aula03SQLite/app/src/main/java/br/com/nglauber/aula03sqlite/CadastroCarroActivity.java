package br.com.nglauber.aula03sqlite;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Date;

import br.com.nglauber.aula03sqlite.database.CarroRepositorio;
import br.com.nglauber.aula03sqlite.model.Carro;

public class CadastroCarroActivity extends AppCompatActivity {

    public static final String EXTRA_CARRO = "carro";
    private static final int REQUEST_CAMERA = 1;
    EditText mEditModelo;
    EditText mEditAno;
    Spinner mSpnFabricante;
    ImageView mImageFoto;
    Carro mCarro;
    File caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_carro);

        mCarro = getIntent().getParcelableExtra(EXTRA_CARRO);

        mEditModelo = (EditText)findViewById(R.id.edit_modelo);
        mEditAno = (EditText)findViewById(R.id.edit_ano);
        mSpnFabricante = (Spinner)findViewById(R.id.spinner_fabricante);
        mImageFoto = (ImageView) findViewById(R.id.image_foto);
        if (mCarro != null){
            mEditModelo.setText(mCarro.getModelo());
            mEditAno.setText(String.valueOf(mCarro.getAno()));
            mSpnFabricante.setSelection(mCarro.getFabricante());
            Glide.with(this).load("file://"+ mCarro.getFoto()).into(mImageFoto);

        } else {
            mCarro = new Carro();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
            Glide.with(this).load("file://"+ caminhoFoto.getAbsolutePath()).into(mImageFoto);
            mCarro.setFoto(caminhoFoto.getAbsolutePath());
        }
    }

    public void salvarClick(View view) {
        try {
            mCarro.setModelo(mEditModelo.getText().toString());
            mCarro.setAno(Integer.parseInt(mEditAno.getText().toString()));
            mCarro.setFabricante(mSpnFabricante.getSelectedItemPosition());

            CarroRepositorio db = new CarroRepositorio(this);
            db.salvar(mCarro);

            setResult(RESULT_OK);
            finish();

        } catch (Exception e){
            Toast.makeText(this, R.string.erro_cadastro, Toast.LENGTH_SHORT).show();
        }
    }

    public void tirarFotoClick(View v) {
        String nomeFoto = DateFormat.format(
                "yyyy-MM-dd_hhmmss", new Date()).toString();

        caminhoFoto = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES),
                nomeFoto);

        Intent it = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(caminhoFoto));
        startActivityForResult(it, REQUEST_CAMERA);
    }
}
