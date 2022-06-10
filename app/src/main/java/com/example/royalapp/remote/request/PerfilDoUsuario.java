package com.example.royalapp.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PerfilDoUsuario {




    @SerializedName("nome")
    @Expose
    private String nome;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("foto")
    @Expose
    private String foto;


    @SerializedName("duasestapas")
    @Expose
    private boolean duasestapas;




    public PerfilDoUsuario(){

    }


    public PerfilDoUsuario (String nome, String email, String foto){
        this.nome = nome;
        this.email = email;
        this.foto = foto;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isDuasestapas() {
        return duasestapas;
    }

    public void setDuasestapas(boolean duasestapas) {
        this.duasestapas = duasestapas;
    }
}
