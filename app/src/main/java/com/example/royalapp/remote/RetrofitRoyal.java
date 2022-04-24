package com.example.royalapp.remote;



////16
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


///17
public class RetrofitRoyal {

    public static Retrofit retrofit = null;

    public static Retrofit getApi(String url){

        if(retrofit == null){
            /*CRIA E CONFIGURA UM OBJETO GSON**/
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            /*CRIA E CONFIGURA O OBJETO DE RETROFIT**/
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}
