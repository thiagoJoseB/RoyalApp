package com.example.royalapp.remote;



///13  requisicao , como vai ser feita , onde os dados vao estar

import com.example.royalapp.model.Extrato;
import com.example.royalapp.remote.request.Cadastro;
import com.example.royalapp.remote.request.Codigo;
import com.example.royalapp.remote.request.InserirEmail;
import com.example.royalapp.remote.request.Login;
import com.example.royalapp.remote.response.DashboardData;
import com.example.royalapp.remote.response.Resultado;
import com.example.royalapp.remote.request.SenhaNova;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RouterInterface {

    //14
    @POST("/royal/cadastro")
    Call<Resultado> addCadastro(@Body Cadastro cadastro);

    @POST("/royal/contas")
    Call<String> login(@Body Login login);

    @POST("/royal/resetar")
    Call<Resultado> addInserirEmail(@Body InserirEmail inserirEmail);

    @POST("/royal/resetar")
    Call<Resultado> addCodigo(@Body Codigo codigo);

    @POST("/royal/resetar")
    Call<Resultado> addSenhaNova(@Body SenhaNova senhaNova);

    @GET("/royal/data/{token}")
    Call<DashboardData> getDashboardInfo(@Path("token") String token);

    @GET("/royal/data/{token}/extrato-mes")
    Call<List<Extrato>> getExtratos(@Path("token") String token, @Query("ano") int ano, @Query("mes") int mes);


}