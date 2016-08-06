package br.com.nglauber.exercicio02;

import android.os.Parcel;
import android.os.Parcelable;

public class Carro implements Parcelable {
    public static final int FAB_FIAT = 0;
    public static final int FAB_FORD = 1;
    public static final int FAB_GM = 2;
    public static final int FAB_VW = 3;

    String modelo;
    int fabricante;
    int ano;

    public Carro() {
    }

    protected Carro(Parcel in) {
        modelo = in.readString();
        fabricante = in.readInt();
        ano = in.readInt();
    }

    public static final Creator<Carro> CREATOR = new Creator<Carro>() {
        @Override
        public Carro createFromParcel(Parcel in) {
            return new Carro(in);
        }

        @Override
        public Carro[] newArray(int size) {
            return new Carro[size];
        }
    };

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getFabricante() {
        return fabricante;
    }

    public void setFabricante(int fabricante) {
        this.fabricante = fabricante;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(modelo);
        dest.writeInt(fabricante);
        dest.writeInt(ano);
    }
}
