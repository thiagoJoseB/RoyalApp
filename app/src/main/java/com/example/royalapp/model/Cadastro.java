package com.example.royalapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cadastro {



    ///11
    @SerializedName("nome")
    @Expose
    //07
    private String nome;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("senha")
    @Expose
    private String senha;

    @SerializedName("confirmarSenha")
    @Expose
    private String confirmarSenha;



    //08 metodo para se nao tiver dados ainda (sem parametros e atributos)
    public Cadastro(){

    }

    //09 metodos para receber os dados que ja existem

    public Cadastro(String nome, String email, String senha , String confirmarSenha){


        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.confirmarSenha  = confirmarSenha;

    }


    ///10 metodos set e gets


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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
