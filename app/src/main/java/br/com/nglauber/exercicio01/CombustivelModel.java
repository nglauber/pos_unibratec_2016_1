package br.com.nglauber.exercicio01;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

public class CombustivelModel extends BaseObservable implements Serializable {
    public static final int RESULT_NENHUM = 0;
    public static final int RESULT_ALCOOL = 1;
    public static final int RESULT_GASOLINA = 2;

    private String gasolina;
    private String alcool;
    private int resultado;
    private int textoResultado;

    public CombustivelModel() {
        textoResultado = R.string.texto_resultado_nenhum;
    }

    @Bindable
    public int getTextoResultado() {
        return textoResultado;
    }

    public void setTextoResultado(int textoResultado) {
        this.textoResultado = textoResultado;
        notifyPropertyChanged(br.com.nglauber.exercicio01.BR.textoResultado);
    }

    @Bindable
    public String getGasolina() {
        return gasolina;
    }

    public void setGasolina(String gasolina) {
        this.gasolina = gasolina;
        notifyPropertyChanged(br.com.nglauber.exercicio01.BR.gasolina);
    }

    @Bindable
    public String getAlcool() {
        return alcool;
    }

    public void setAlcool(String alcool) {
        this.alcool = alcool;
        notifyPropertyChanged(br.com.nglauber.exercicio01.BR.alcool);
    }

    @Bindable
    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
        notifyPropertyChanged(br.com.nglauber.exercicio01.BR.resultado);
    }

    public void calcular(){
        try {
            double gasValor = Double.parseDouble(gasolina);
            double alcoolValor = Double.parseDouble(alcool);

            if (gasValor * 0.7 > alcoolValor){
                resultado = RESULT_ALCOOL;
                setTextoResultado(R.string.texto_resultado_alcool);
            } else {
                resultado = RESULT_GASOLINA;
                setTextoResultado(R.string.texto_resultado_gasolina);
            }

        } catch (Exception e){
            resultado = RESULT_NENHUM;
            throw new RuntimeException("Invalid values");
        }
    }
}
