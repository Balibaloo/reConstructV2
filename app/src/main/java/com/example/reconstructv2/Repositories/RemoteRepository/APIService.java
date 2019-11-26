package com.example.reconstructv2.Repositories.RemoteRepository;


import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface APIService {

    @GET("/getFrontPageListings")
    Call<ListingListAPIResponse> getFrontPageListings(@Query("pageNum") Integer pageNum);
}
