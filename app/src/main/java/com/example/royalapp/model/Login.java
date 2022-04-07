package com.example.royalapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//06 1
public class Login {

    ///11 Mapear os elementos para quando for gson   /// MODEL
    @SerializedName("email")
    @Expose
    //07 2
    private String email;


    @SerializedName("senha")
    @Expose
    private String senha;



///08 metodo para se nao tiver dados ainda (sem parametros e atributos)
public Login(){

}

// ///09 metodos para receber os dados que ja existem
public Login (String email, String senha){

    this.email = email;
    this.senha = senha;

}


///10 metodos set e gets


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