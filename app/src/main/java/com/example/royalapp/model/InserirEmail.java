package com.example.royalapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InserirEmail {

    ///07 iniciar variaveis
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("tipo")
    @Expose
    private String tipo = "Pedir";



    ///08 metodo para se nao tiver dados ainda (sem parametros e atributos)
    public InserirEmail(){

    }

    ///09 metodos para receber os dados que ja existem
    public InserirEmail(String email, String tipo){

        this.email = email;
        this.tipo = tipo;

    }

    ///10 metodos set e gets


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
