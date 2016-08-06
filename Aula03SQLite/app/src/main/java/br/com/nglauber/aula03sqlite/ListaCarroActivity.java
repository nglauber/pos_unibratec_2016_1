package br.com.nglauber.aula03sqlite;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

import br.com.nglauber.aula03sqlite.database.CarroRepositorio;
import br.com.nglauber.aula03sqlite.model.Carro;

public class ListaCarroActivity extends AppCompatActivity {

    private static final int REQUEST_CADASTRO = 1;
    private static final String EXTRA_LISTA = "lista";
    List<Carro> mCarros;
    RecyclerView mListView;
    CarroAdapter mAdapter;
    CarroRepositorio mDb;

    CarroAdapter.AoClicarNoCarroListener mItemClick =
            new CarroAdapter.AoClicarNoCarroListener() {
        @Override
        public void carroFoiClicado(Carro carro) {
            Intent it = new Intent(ListaCarroActivity.this, CadastroCarroActivity.class);
            it.putExtra(CadastroCarroActivity.EXTRA_CARRO, carro);
            startActivityForResult(it, REQUEST_CADASTRO);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_carro);
        mListView = (RecyclerView)findViewById(R.id.lista_carros);

        mDb = new CarroRepositorio(this);

        mCarros = mDb.listar();

        mAdapter = new CarroAdapter(mCarros);
        mAdapter.setListener(mItemClick);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mListView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mListView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        mListView.setAdapter(mAdapter);
        configuraSwipe();
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
            mCarros.clear();
            mCarros.addAll(mDb.listar());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void configuraSwipe() {
        ItemTouchHelper.SimpleCallback swipe =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        final int position = viewHolder.getAdapterPosition();
                        Carro carro = mCarros.remove(position);
                        mDb.excluir(carro);
                        mAdapter.notifyItemRemoved(position);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
        itemTouchHelper.attachToRecyclerView(mListView);
    }
}
