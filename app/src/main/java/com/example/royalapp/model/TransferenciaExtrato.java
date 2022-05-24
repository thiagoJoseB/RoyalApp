package com.example.royalapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class TransferenciaExtrato {

    @SerializedName("categoria")
    public int categoria ;

    @SerializedName("parcelas")
    public Integer parcelas ;

    @SerializedName("indice")
    public Integer indice ;

    @SerializedName("valor")
    public BigDecimal valor ;

    @SerializedName("descricao")
    public String descricao;


    @SerializedName("data")
    public String data;






    public TransferenciaExtrato(){
    }


    public TransferenciaExtrato(int categoria , BigDecimal valor, String descricao, String data ) {
        this.categoria = categoria;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }
}
