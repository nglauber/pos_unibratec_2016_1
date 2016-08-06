package br.com.nglauber.aula03sqlite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.nglauber.aula03sqlite.model.Carro;

public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarroViewHolder> {
    List<Carro> mCarros;
    AoClicarNoCarroListener mListener;

    public CarroAdapter(List<Carro> carros) {
        mCarros = carros;
    }

    public void setListener(AoClicarNoCarroListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        return mCarros != null ? mCarros.size() : 0;
    }

    @Override
    public CarroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 1 cria o view holder se necessário
        View linha = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_carro, parent, false);
        final CarroViewHolder vh = new CarroViewHolder(linha);

        linha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.carroFoiClicado(
                            mCarros.get(vh.getAdapterPosition()));
                }
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(CarroViewHolder holder, int position) {
        // 2 pega o objeto
        Carro carro = mCarros.get(position);

        // 3 preenche as views com os dados do objeto
        holder.txtModelo.setText(carro.getModelo());
        holder.txtAno.setText(String.valueOf(carro.getAno())); // R.string.texto
        holder.imgLogo.setImageResource(logos[carro.getFabricante()]);
    }

    class CarroViewHolder extends RecyclerView.ViewHolder {
        TextView txtModelo;
        TextView txtAno;
        ImageView imgLogo;

        public CarroViewHolder(View itemView) {
            super(itemView);
            txtModelo = (TextView)itemView.findViewById(R.id.text_modelo);
            txtAno = (TextView)itemView.findViewById(R.id.text_ano);
            imgLogo = (ImageView) itemView.findViewById(R.id.img_logo);
        }
    }

    int[] logos = new int[]{
            R.drawable.fiat,
            R.drawable.ford,
            R.drawable.gm,
            R.drawable.vw
    };

    interface AoClicarNoCarroListener {
        void carroFoiClicado(Carro carro);
    }
}
