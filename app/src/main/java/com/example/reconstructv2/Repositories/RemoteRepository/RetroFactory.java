package com.example.reconstructv2.Repositories.RemoteRepository;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFactory {
    private static final String ROOTURL = "http://82.3.163.116:1234";

    public static APIService getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(ROOTURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(APIService.class);
    }


}
