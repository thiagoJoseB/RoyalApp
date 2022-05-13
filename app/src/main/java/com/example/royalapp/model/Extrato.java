package com.example.royalapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class Extrato {

    @SerializedName("categoria")
    @Expose
    private int categoria ;

    @SerializedName("valor")
    @Expose
    private BigDecimal valor ;

    @SerializedName("descricao")
    @Expose
    private String descricao;


    @SerializedName("data")
    @Expose
    private String data;




    public Extrato(int i, Extrato extrato){

    }


    public Extrato(int categoria , BigDecimal valor, String descricao,  String data ) {
        this.categoria = categoria;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;



    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
