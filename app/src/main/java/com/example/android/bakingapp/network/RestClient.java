package com.example.android.bakingapp.network;

import com.example.android.bakingapp.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private Api_Service api_service;

    public RestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.RECIPES_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api_service = retrofit.create(Api_Service.class);
    }

    public Api_Service getApi_service() {

        return api_service;

    }

}
