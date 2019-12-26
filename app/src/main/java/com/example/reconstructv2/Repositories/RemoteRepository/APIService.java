package com.example.reconstructv2.Repositories.RemoteRepository;


import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.DesiredItemsAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ImageIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.Models.User;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface APIService {

    @GET("/")
    Call<BaseAPIResponse> testConnection();

    @GET("/auth")
    Call<BaseAPIResponse> testConnectionAuthenticated(@Header("authorisation") String AuthHeaderToken);

    /////////////////////////////////////////////////////////

    @GET("/checkUniqueUsername")
    Call<CheckAvailableAPIResponse> checkUsernameUnique(@Query("username") String username);

    @GET("/checkUniqueEmail")
    Call<CheckAvailableAPIResponse> checkEmailUnique(@Query("email") String email);

    /////////////////////////////////////////////////////////

    @POST("/auth/createListing")
    Call<ListingIDAPIResponse> createListing(@Header("authorisation") String AuthHeaderToken,
                                             @Query("title") String title,
                                             @Query("body") String body,
                                             @Query("end_date") String end_date,
                                             @Query("location") String location,
                                             @Body List<Integer> itemList,
                                             @Query("main_photo") String main_photoID);

    @POST("/auth/addListingtoWatchList")
    Call<BaseAPIResponse> addListingtoWatchList(@Header("authorisation") String AuthHeaderToken,
                                                @Query("listingID") String listingID);

    @POST("/auth/removeListingfromWatchList")
    Call<BaseAPIResponse> removeListingfromWatchList(@Header("authorisation") String AuthHeaderToken,
                                                     @Query("listingID") String listingID);

    @GET("/auth/getWatchlist")
    Call<ListingListAPIResponse> getWatchlist(@Header("authorisation") String AuthHeaderToken);

    @GET("/auth/getRecentListings")
    Call<ListingListAPIResponse> getRecentListings(@Header("authorisation") String AuthHeaderToken,
                                                   @Query("pageNum") Integer pageNum);

    @GET("/auth/getListing")
    Call<SingleListingAPIResponse> getListingAuthenticated(@Header("authorisation") String AuthHeaderToken,
                                                           @Query("listingID") String listingID);

    @POST("/auth/reserveItems")
    Call<BaseAPIResponse> reserveItemsRequest(@Header("authorisation") String AuthHeaderToken,
                                              @Query("listingItemIDList") List<JsonObject> listingItemIDs);

    @GET("/getListingNoAuth")
    Call<SingleListingAPIResponse> getListingNoAuth(@Query("listingID") String listingID);

    @GET("/getFrontPageListings")
    Call<ListingListAPIResponse> getFrontPageListings(@Query("pageNum") Integer pageNum);

    @GET("/getFilteredListings")
    Call<ListingListAPIResponse> getFilteredListings(@Query("searchString") String searchString,
                                                     @Query("pageNum") Integer pageNum);

    @GET("/getDesiredItems")
    Call<DesiredItemsAPIResponse> getDesiredItems(@Query("pageNum") Integer pageNum);

    /////////////////////////////////////////////////////////

    @GET("/auth/login")
    Call<UserTokenAPIResponse> login(@Header("authorisation") String AuthHeaderUP);

    @GET("/getUserProfile")
    Call<UserAPIResponse> getUserProfile(@Query("userID") String userID);

    @GET("/auth/changeWantedTags")
    Call<BaseAPIResponse> changeWantedTags(@Header("authorisation") String AuthHeaderToken,
                                           @Query("new_tags") List<String> new_tags);

    @POST("/createAccount")
    Call<UserTokenAPIResponse> createAccount(@Query("username") String username,
                                             @Query("password") String password,
                                             @Query("first_name") String first_name,
                                             @Query("last_name") String last_name,
                                             @Query("email") String email,
                                             @Query("phone") Integer phone);

    @POST("/auth/update_user_data")
    Call<BaseAPIResponse> saveUser(@Header("authorisation") String AuthHeaderToken,
                                   @Body User user);

    @POST("/auth/saveImage")
    Call<ImageIDAPIResponse> saveImageOnServer(@Part MultipartBody.Part file);

    @DELETE("/auth/deleteListing")
    Call<BaseAPIResponse> deleteListing(@Header("authorisation") String AuthHeaderToken,
                                        @Query("listingID") String listingID);
}