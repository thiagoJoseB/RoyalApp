package com.example.royalapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.royalapp.model.InserirEmail;

public class Codigo {

    ///11 Mapear os elementos para quando for gson   /// MODEL
    @SerializedName("email")
    @Expose
    //07 2
    private String email;

    @SerializedName("codigo")
    @Expose
    private String codigo;

    @SerializedName("tipo")
    @Expose
    private String tipo = "USAR";


    ///08 metodo para se nao tiver dados ainda (sem parametros e atributos)
    public Codigo(){

    }

    // ///09 metodos para receber os dados que ja existem
    public Codigo (String email, String codigo){

        this.email = email;
        this.codigo = codigo;
        this.tipo = tipo;
    }

    ///10 metodos set e gets


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}


