package br.com.nglauber.aula07.service;

import android.os.Binder;

public class ContadorBinder extends Binder {

    Contador contador;

    public ContadorBinder(Contador contador) {
        this.contador = contador;
    }

    public Contador getContador() {
        return contador;
    }
}
