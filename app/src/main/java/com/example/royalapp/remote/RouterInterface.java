package com.example.royalapp.remote;



///13  requisicao , como vai ser feita , onde os dados vao estar

import com.example.royalapp.model.Cadastro;
import com.example.royalapp.model.Codigo;
import com.example.royalapp.model.InserirEmail;
import com.example.royalapp.model.Login;
import com.example.royalapp.model.Resultado;
import com.example.royalapp.model.SenhaNova;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;


public interface RouterInterface {

    //14
    @POST("/royal/cadastro") Call<Resultado> addCadastro(@Body Cadastro cadastro);
    @POST("/royal/contas") Call<ResponseBody> login(@Body Login login);
    @POST("/royal/resetar") Call<Resultado> addInserirEmail(@Body InserirEmail inserirEmail);
    @POST("/royal/resetar") Call<Resultado> addCodigo(@Body Codigo codigo);
    @POST("/royal/resetar") Call<Resultado> addSenhaNova(@Body SenhaNova senhaNova);
    @GET("/royal/dashboard?k={token}") Call<RequestBody> getDashboardInfo(@Path("token") String token);





}
