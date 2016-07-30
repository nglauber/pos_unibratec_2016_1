package br.com.nglauber.aula02lista;

public class Carro {
    public String modelo;
    public int ano;

    public Carro(String modelo, int ano) {
        this.modelo = modelo;
        this.ano = ano;
    }

    @Override
    public String toString() {
        return modelo +" / "+ ano;
    }
}
