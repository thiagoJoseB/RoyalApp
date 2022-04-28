package com.example.royalapp.remote;



////16
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


///17
public class RetrofitRoyal {

    public static Retrofit retrofit = null;

    public static Retrofit getApi(String url){

        if(retrofit == null){
            /*CRIA E CONFIGURA UM OBJETO GSON**/

            /*CRIA E CONFIGURA O OBJETO DE RETROFIT**/
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .setLenient()
                            .create()))
                    .build();
        }

        return retrofit;
    }
}
