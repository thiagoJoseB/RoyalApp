package com.example.royalapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class Categoria{
    public int idCategoria;
    public String nome;
    public String cor;
    public String icone;

    public Categoria(int idCategoria, String nome, String cor, String icone) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.cor = cor;
        this.icone = icone;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Categoria{");
        sb.append("idCategoria=").append(idCategoria);
        sb.append(", nome=").append(nome);
        sb.append(", cor=").append(cor);
        sb.append(", icone=").append(icone);
        sb.append('}');
        return sb.toString();
    }

    public static List<Categoria> DESPESAS = new ArrayList<>();
    public static List<Categoria> RECEITAS = new ArrayList<>();

    public static List<Categoria> para(BigDecimal valor){
        return valor.compareTo(BigDecimal.ZERO) > 0 ? RECEITAS : DESPESAS;
    }

}