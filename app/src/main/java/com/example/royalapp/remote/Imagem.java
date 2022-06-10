package com.example.royalapp.remote;



///13  requisicao , como vai ser feita , onde os dados vao estar

import com.example.royalapp.model.Transferencia;
import com.example.royalapp.model.TransferenciaExtrato;
import com.example.royalapp.remote.request.Cadastro;
import com.example.royalapp.remote.request.Codigo;
import com.example.royalapp.remote.request.InserirEmail;
import com.example.royalapp.remote.request.Login;
import com.example.royalapp.remote.request.NovaSenhaPerfil;
import com.example.royalapp.remote.request.SenhaNova;
import com.example.royalapp.remote.response.Resultado;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Imagem {
    public static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
    Retrofit INTERNAL_RETROFIT = new Retrofit.Builder()
            .client(OK_HTTP_CLIENT)
            .baseUrl(API.API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .serializeNulls()
                    .create()))
            .build();

    static Imagem get(){
        return INTERNAL_RETROFIT.create(Imagem.class);
    }

    @GET("upload/{imagem}") Call<ResponseBody> imagem(@Query("k") String token, @Path("imagem") String imagem);
    @PUT("upload") Call<String> enviaFoto(@Query("k") String token, @Body RequestBody corpoComInputStream );
}
