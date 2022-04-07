package com.example.royalapp.remote;



///13  requisicao , como vai ser feita , onde os dados vao estar

import com.example.royalapp.model.Cadastro;
import com.example.royalapp.model.Login;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;



public interface RouterInterface {

    //14
    @POST("/royal/cadastro") Call<Cadastro> addCadastro(@Body Cadastro cadastro);
    @POST("/royal/login") Call<Login> addLogin(@Body Login login);



}
