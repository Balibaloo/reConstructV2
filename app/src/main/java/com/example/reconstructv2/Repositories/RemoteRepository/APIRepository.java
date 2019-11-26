package com.example.reconstructv2.Repositories.RemoteRepository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;


import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;

import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIRepository{
    private APIService apiService;
    private MutableLiveData<ListingListAPIResponse> listingListAPIResponseMutableLiveData;

    public APIRepository(Application application) {
        this.apiService = RetroFactory.getRetrofitInstance();

        this.listingListAPIResponseMutableLiveData = new MutableLiveData<>();
    }


    public APIService getApiService() {
        return apiService;
    }


    public MutableLiveData<ListingListAPIResponse> getListingListAPIResponseMutableLiveData(){
        return listingListAPIResponseMutableLiveData;
    }


    public void getFrontPageListings () {
        apiService.getFrontPageListings(0).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()) {
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    System.out.println("LISTING REQUEST FAILED ?????????????");
                }
            }

            @Override
            public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {
                System.out.println("LISTING REQUEST FAILED 2 ?????????????");
                System.out.println(t.getMessage());
            }


        });
    }
}