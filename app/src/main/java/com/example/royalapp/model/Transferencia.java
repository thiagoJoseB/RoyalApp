package com.example.royalapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Transferencia {
    public String anexo;
    public String data;
    public String descricao;
    public boolean fixa;
    public Frequencia nomeFrequencia;
    public int categoria;
    public int id;
    public String observacao;
    public boolean parcelada;
    public int parcelas;
    public BigDecimal valor;

    public enum Frequencia {
        DIAS,
        SEMANAS,
        QUINZENAS,
        MESES,
        BIMESTRES,
        TRIMESTRES,
        SEMESTRES,
        ANOS;
    }

    public boolean isDespesa() {
        return valor.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isReceita() {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }
}
