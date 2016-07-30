package br.com.nglauber.aula02result;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

public class Usuario extends BaseObservable implements Serializable {
    public static final int REDE_FACEBOOK = 0;
    public static final int REDE_GPLUS = 1;
    public static final int REDE_TWITTER = 2;

    private String nome;
    private int redeSocial;

    public Usuario() {
        this.redeSocial = REDE_FACEBOOK;
    }

    @Bindable
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        notifyPropertyChanged(br.com.nglauber.aula02result.BR.nome);
    }

    @Bindable
    public int getRedeSocial() {
        return redeSocial;
    }

    public void setRedeSocial(int redeSocial) {
        this.redeSocial = redeSocial;
        notifyPropertyChanged(br.com.nglauber.aula02result.BR.redeSocial);
    }
}
