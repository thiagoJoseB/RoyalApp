package com.example.royalapp.remote;



///13  requisicao , como vai ser feita , onde os dados vao estar

import com.example.royalapp.model.ItemFavorito;
import com.example.royalapp.model.TransferenciaExtrato;
import com.example.royalapp.remote.request.Cadastro;
import com.example.royalapp.remote.request.Codigo;
import com.example.royalapp.remote.request.InserirEmail;
import com.example.royalapp.remote.request.Login;
import com.example.royalapp.remote.request.PerfilDoUsuario;
import com.example.royalapp.remote.response.Resultado;
import com.example.royalapp.remote.request.SenhaNova;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API {
    String API_URL = "http://6.6.6.104:8080/royal/";
    String WS_API_URL = "ws" + API_URL.substring(4);
    Retrofit INTERNAL_RETROFIT = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()))
            .build();

    static API get(){
        return INTERNAL_RETROFIT.create(API.class);
    }

    //14
    @POST("cadastro") Call<Resultado> addCadastro(@Body Cadastro cadastro);
    @POST("contas") Call<String> login(@Body Login login);
    @POST("resetar") Call<Resultado> addInserirEmail(@Body InserirEmail inserirEmail);
    @POST("resetar") Call<Resultado> addCodigo(@Body Codigo codigo);
    @POST("resetar") Call<Resultado> addSenhaNova(@Body SenhaNova senhaNova);
    @GET("data/saldo/categorias") Call<String> getDashboardInfo(@Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("data/saldo") Call<String> getSaldo(@Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("data/extrato-mes") Call<List<TransferenciaExtrato>> getExtratos(@Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("grafico/{tipo}") Call<String> graficoMensal(@Path("tipo") String tipo, @Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("data/favorito") Call<List<ItemFavorito>> getFavorito(@Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);

    @GET("data/perfil") Call<String> getPerfil(@Query("k") String token);
}
