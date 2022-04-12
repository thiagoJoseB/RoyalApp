package com.example.royalapp.remote;



///13  requisicao , como vai ser feita , onde os dados vao estar

import com.example.royalapp.model.Cadastro;
import com.example.royalapp.model.InserirEmail;
import com.example.royalapp.model.Login;
import com.example.royalapp.model.Resultado;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Body;



public interface RouterInterface {

    //14
    @POST("/royal/cadastro") Call<Resultado> addCadastro(@Body Cadastro cadastro);
    @POST("/royal/contas") Call<Resultado> addLogin(@Body Login login);
    @POST("/royal/resetar") Call<Resultado> addInserirEmail(@Body InserirEmail inserirEmail);



}
