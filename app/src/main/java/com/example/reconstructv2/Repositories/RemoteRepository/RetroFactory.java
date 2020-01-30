package com.example.reconstructv2.Repositories.RemoteRepository;

import android.content.Context;
import android.content.res.Resources;

import com.example.reconstructv2.R;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFactory {
    public static APIService getRetrofitInstance(Context mContext) {
        return new Retrofit.Builder()
                .baseUrl(getRooturl(mContext))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(APIService.class);


    }

    private static String getRooturl(Context contex){
        return contex.getResources().getString(R.string.ROOTURL);
    };

}
