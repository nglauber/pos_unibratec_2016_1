package br.com.nglauber.aula02lista;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import br.com.nglauber.aula02lista.databinding.ItemCarroBinding;

public class CarroAdapter extends ArrayAdapter<Carro> {

    public CarroAdapter(Context context, List<Carro> carros) {
        super(context, 0, carros);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_carro, parent, false);
        }
        ItemCarroBinding binding =
                DataBindingUtil.bind(convertView);
        binding.setCarro(getItem(position));

        return convertView;
    }
}
