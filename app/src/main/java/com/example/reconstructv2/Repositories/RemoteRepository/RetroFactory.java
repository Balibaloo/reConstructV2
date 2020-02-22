package com.example.reconstructv2.Repositories.RemoteRepository;

import android.content.Context;
import com.example.reconstructv2.R;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// factory class to create a new retrofit instance
public class RetroFactory {

    // return a new retrofit instance
    public static APIService getRetrofitInstance(Context mContext) {

        // OkHttpClient object used to set connection timeouts on requests
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();


        return new Retrofit.Builder()
                .baseUrl(getRooturl(mContext)) // set the base url for every connection
                .client(okHttpClient) // set timeouts on requests
                .addConverterFactory(GsonConverterFactory.create()) // add a gson converter to parse results from requests
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) 
                .build().create(APIService.class); // create the retrofit instance


    }

    // return the base url for all requests
    private static String getRooturl(Context contex) {
        return contex.getResources().getString(R.string.ROOTURL);
    }


}
