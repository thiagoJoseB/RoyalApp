package com.example.royalapp.remote;



///13  requisicao , como vai ser feita , onde os dados vao estar

import com.example.royalapp.activity.PerfilUsuarioActivity;
import com.example.royalapp.model.Transferencia;
import com.example.royalapp.model.TransferenciaExtrato;
import com.example.royalapp.remote.request.Cadastro;
import com.example.royalapp.remote.request.Codigo;
import com.example.royalapp.remote.request.InserirEmail;
import com.example.royalapp.remote.request.Login;
import com.example.royalapp.remote.request.NovaSenhaPerfil;
import com.example.royalapp.remote.response.Resultado;
import com.example.royalapp.remote.request.SenhaNova;
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
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API {
    public static OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            .readTimeout(10,TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build();
    String API_URL = "https://tomcat.studiotr.io/royalapi/";
    String WS_API_URL = "ws" + API_URL.substring(4);
    Retrofit INTERNAL_RETROFIT = new Retrofit.Builder()
            .client(OK_HTTP_CLIENT)
            .baseUrl(API_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .serializeNulls()
                    .create()))
            .build();

    static API get(){
        return INTERNAL_RETROFIT.create(API.class);
    }



    @GET("data/saldo/categorias/saldo-geral/bruto/fixa") Call<String> getDashboardInfo(@Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("data/saldo/saldo-geral/bruto") Call<String> getSaldo(@Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("data/extrato-mes") Call<List<TransferenciaExtrato>> getExtratos(@Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("grafico/{tipo}") Call<String> graficoMensal(@Path("tipo") String tipo, @Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("data/favorito") Call<List<Transferencia>> getFavorito(@Query("k") String token, @Query("ano") int ano, @Query("mes") int mes);
    @GET("data/fixa") Call<List<Transferencia>> fixa(@Query("k") String token);
    @GET("data/transferencia") Call<List<Transferencia>> getTransferencia(@Query("k") String token, @Query("id") int id);
    @GET("data/perfil") Call<String>  getPerfil(@Query("k") String token);
    @GET("grafico/{tipo}?modo=lista&periodo=dia-mes") Call<List<BigDecimal>> graficoMensal2(@Path("tipo") String tipo, @Query("k") String token, @Query("ano") int ano, @Query("mes") int mes, @Query("cat") int... categorias);
    @GET("grafico/{tipo}?modo=lista&periodo=mes-ano") Call<List<BigDecimal>> graficoAnual2(@Path("tipo") String tipo, @Query("k") String token, @Query("ano") int ano, @Query("cat") int... categorias);

    @POST("cadastro") Call<Resultado> addCadastro(@Body Cadastro cadastro);
    @POST("contas") Call<String> login(@Body Login login);
    @POST("resetar") Call<Resultado> addInserirEmail(@Body InserirEmail inserirEmail);
    @POST("resetar") Call<Resultado> addCodigo(@Body Codigo codigo);
    @POST("resetar") Call<Resultado> addSenhaNova(@Body SenhaNova senhaNova);
    @POST("autologin") Call<String> autoLogin(@Body String body);
    @POST("data/desfixar") Call<String> desfixar(@Body String body, @Query("k") String token);

    @POST("data/perfil") Call<Resultado> setPerfil(@Body PerfilUsuarioActivity.NomeDuasEtapasFoto perfil , @Query("k") String token);
    @POST("data/senha") Call<Resultado> novaSenha(@Body NovaSenhaPerfil novaSenhaPerfil , @Query("k") String token);
    @POST("transferencia") Call<Resultado> transferencia(@Body String transf , @Query("k") String token);
}
