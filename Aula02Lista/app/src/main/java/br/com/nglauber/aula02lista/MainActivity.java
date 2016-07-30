package br.com.nglauber.aula02lista;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.nglauber.aula02lista.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    List<Carro> carros;
    CarroAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_main);

        carros = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            carros.add(new Carro("Fusca"+ i, 1977));
            carros.add(new Carro("Celta"+ i, 2000));
            carros.add(new Carro("KA"+ i, 2001));
            carros.add(new Carro("Fusion"+ i, 2015));
        }
        //new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, carros)
        adapter = new CarroAdapter(this, carros);
        binding.listaCarro.setAdapter(adapter);
        binding.setController(this);

//        binding.listaCarro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, carros.get(position).toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void onCarroClick(int position){
        Toast.makeText(MainActivity.this, carros.get(position).toString(), Toast.LENGTH_SHORT).show();
    }

    public void vaiFilhaoClick(View view) {
        carros.add(0, new Carro("Random ", (int)(Math.random() * 2015)));
        adapter.notifyDataSetChanged();
    }
}
