package com.example.royalapp.remote;



//// 18 criar uma class dentro da remote

//////APIUTIL

///19
public class APIUtil {


    public APIUtil(){

    }

    public  static final String API_URL = "http://10.107.144.11:8080/royal/";

    public static RouterInterface getApiInterface(){
                              //// onde esta API
        return RetrofitRoyal.getApi(API_URL)
                //// AS  ROTAS
                .create(RouterInterface.class);
    }






}


