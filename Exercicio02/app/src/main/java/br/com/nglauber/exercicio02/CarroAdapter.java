package br.com.nglauber.exercicio02;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CarroAdapter extends ArrayAdapter<Carro>{
    int[] logos = new int[]{
            R.drawable.fiat,
            R.drawable.ford,
            R.drawable.gm,
            R.drawable.vw
    };

    public CarroAdapter(Context context, List<Carro> carros) {
        super(context, 0, carros);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1 pegar o objeto
        Carro carro = getItem(position);

        // 2 carregar arquivo de layout
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();

            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_carro, parent, false);

            holder.txtModelo = (TextView)convertView.findViewById(R.id.text_modelo);
            holder.txtAno = (TextView)convertView.findViewById(R.id.text_ano);
            holder.imgLogo = (ImageView) convertView.findViewById(R.id.img_logo);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 3 preencher o layout com as informações do objeto
        holder.txtModelo.setText(carro.getModelo());
        holder.txtAno.setText(String.valueOf(carro.getAno())); // R.string.texto
        holder.imgLogo.setImageResource(logos[carro.getFabricante()]);

        // 4 retornar o layout preenchido
        return convertView;
    }

    static class ViewHolder {
        TextView txtModelo;
        TextView txtAno;
        ImageView imgLogo;
    }
}
