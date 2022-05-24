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


}
