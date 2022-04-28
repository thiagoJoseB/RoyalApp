package com.example.royalapp.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SenhaNova {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("senha")
    @Expose
    private String senha;


    @SerializedName("tipo")
    @Expose
    private String tipo = "MUDAR";




    public SenhaNova() {

    }




    public SenhaNova(String email, String senha,String tipo){

        this.email = email;
        this.senha = senha;
        this.tipo = tipo;

    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
