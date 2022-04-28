package com.example.royalapp.remote;



//// 18 criar uma class dentro da remote

//////APIUTIL

///19
public class APIUtil {


    public APIUtil(){

    }

    public  static final String API_URL = "http://6.6.6.103:8080/";

    public static RouterInterface getApiInterface(){
                              //// onde esta API
        return RetrofitRoyal.getApi(API_URL)
                //// AS  ROTAS
                .create(RouterInterface.class);
    }






}


