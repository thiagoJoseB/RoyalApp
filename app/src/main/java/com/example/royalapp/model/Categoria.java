package com.example.royalapp.model;

import androidx.annotation.NonNull;

public final class Categoria {
    public Integer idCategoria;
    public String nome;
    public String cor;
    public String icone;

    public Categoria(Integer idCategoria, String nome, String cor, String icone) {
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


}