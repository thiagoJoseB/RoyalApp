package com.example.royalapp.remote;



//// 18 criar uma class dentro da remote

//////APIUTIL

///19
public class APIUtil {


    public APIUtil(){

    }

    public  static final String API_URL = "http://10.107.144.17:8080/royal/";

    public static RouterInterface getCadastroInterface(){
                              //// onde esta API
        return RetrofitCadastro.getCadastro(API_URL)
                //// AS  ROTAS
                .create(RouterInterface.class);
    }

    public static RouterInterface getLoginInterface(){
        //// onde esta API
        return RetrofitLogin.getLogin(API_URL)
                //// AS  ROTAS
                .create(RouterInterface.class);
    }





}


