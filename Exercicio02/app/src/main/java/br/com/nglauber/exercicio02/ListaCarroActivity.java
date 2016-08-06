package br.com.nglauber.exercicio02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaCarroActivity extends AppCompatActivity {

    private static final int REQUEST_CADASTRO = 1;
    private static final String EXTRA_LISTA = "lista";
    ArrayList<Carro> mCarros;
    ListView mListView;
    ArrayAdapter<Carro> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_carro);
        mListView = (ListView)findViewById(R.id.lista_carros);

        if (savedInstanceState == null) {
            mCarros = new ArrayList<>();
        } else {
            mCarros = savedInstanceState.getParcelableArrayList(EXTRA_LISTA);
        }
//        mAdapter = new ArrayAdapter<>(
//                this, android.R.layout.simple_list_item_1, mCarros);
        mAdapter = new CarroAdapter(this, mCarros);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_LISTA, mCarros);
    }

    public void novoCarroClick(View view) {
        startActivityForResult(
                new Intent(this, CadastroCarroActivity.class),
                REQUEST_CADASTRO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CADASTRO && resultCode == RESULT_OK){
            Carro carro = data.getParcelableExtra(CadastroCarroActivity.EXTRA_CARRO);
            mCarros.add(carro);
            mAdapter.notifyDataSetChanged();
        }
    }
}
