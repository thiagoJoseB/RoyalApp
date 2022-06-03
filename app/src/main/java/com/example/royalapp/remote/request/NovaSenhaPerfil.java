package com.example.royalapp.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NovaSenhaPerfil {

    @SerializedName("nova")
    @Expose
    private String nova;


    @SerializedName("antiga")
    @Expose
    private String antiga;

    @SerializedName("confirmarSenha")
    @Expose
    private String confirmarSenha;


    public NovaSenhaPerfil(){


    }

    public NovaSenhaPerfil(String nova, String antiga ,String confirmarSenha){

        this.antiga = antiga;
        this.nova = antiga;
        this.confirmarSenha = confirmarSenha;


    }


    public String getNova() {
        return nova;
    }

    public void setNova(String nova) {
        this.nova = nova;
    }

    public String getAntiga() {
        return antiga;
    }

    public void setAntiga(String antiga) {
        this.antiga = antiga;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
}
