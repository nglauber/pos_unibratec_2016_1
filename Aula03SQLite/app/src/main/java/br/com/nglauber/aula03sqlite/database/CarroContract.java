package br.com.nglauber.aula03sqlite.database;

import android.provider.BaseColumns;

public interface CarroContract extends BaseColumns {

    String TABELA = "carro";

    String MODELO = "modelo";
    String ANO = "ano";
    String FABRICANTE = "fabricante";
    String FOTO = "foto";
}
