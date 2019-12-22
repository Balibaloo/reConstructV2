package com.example.reconstructv2.Repositories.RemoteRepository;

import android.app.Application;
import android.util.Base64;

import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.AuthenticationHelper;
import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.DesiredItemsAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ImageIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIRepository {
    private APIService apiService;

    private MutableLiveData<CheckAvailableAPIResponse> CheckAvailableAPIResponseMutableLiveData;
    private MutableLiveData<BaseAPIResponse> baseAPIResponseMutableLiveData;
    private MutableLiveData<ListingIDAPIResponse> listingIDAPIResponseMutableLiveData;
    private MutableLiveData<ListingListAPIResponse> listingListAPIResponseMutableLiveData;
    private MutableLiveData<SingleListingAPIResponse> singleListingAPIResponseMutableLiveData;
    private MutableLiveData<UserAPIResponse> userAPIResponseMutableLiveData;
    private MutableLiveData<UserTokenAPIResponse> userTokenAPIResponseMutableLiveData;
    private MutableLiveData<DesiredItemsAPIResponse> desiredItemsAPIResponseMutableLiveData;
    private MutableLiveData<ImageIDAPIResponse> imageIDAPIResponseMutableLiveData;

    public APIRepository(Application application) {
        this.apiService = RetroFactory.getRetrofitInstance();

        this.CheckAvailableAPIResponseMutableLiveData = new MutableLiveData<>();
        this.baseAPIResponseMutableLiveData = new MutableLiveData<>();
        this.listingIDAPIResponseMutableLiveData = new MutableLiveData<>();
        this.listingListAPIResponseMutableLiveData = new MutableLiveData<>();
        this.singleListingAPIResponseMutableLiveData = new MutableLiveData<>();
        this.userAPIResponseMutableLiveData = new MutableLiveData<>();
        this.userTokenAPIResponseMutableLiveData = new MutableLiveData<>();
        this.desiredItemsAPIResponseMutableLiveData = new MutableLiveData<>();
        this.imageIDAPIResponseMutableLiveData = new MutableLiveData<>();


    }

    public APIService getApiService() {
        return apiService;
    }


    public MutableLiveData<CheckAvailableAPIResponse> getCheckAvailableAPIResponseMutableLiveData() {
        return CheckAvailableAPIResponseMutableLiveData;
    }

    public MutableLiveData<BaseAPIResponse> getBaseAPIResponseMutableLiveData() {
        return baseAPIResponseMutableLiveData;
    }

    public MutableLiveData<ListingIDAPIResponse> getListingIDAPIResponseMutableLiveData() {
        return listingIDAPIResponseMutableLiveData;
    }

    public MutableLiveData<SingleListingAPIResponse> getSingleListingAPIResponseMutableLiveData() {
        return singleListingAPIResponseMutableLiveData;
    }

    public MutableLiveData<UserAPIResponse> getUserAPIResponseMutableLiveData() {
        return userAPIResponseMutableLiveData;
    }

    public MutableLiveData<UserTokenAPIResponse> getUserTokenAPIResponseMutableLiveData() {
        return userTokenAPIResponseMutableLiveData;
    }


    public MutableLiveData<ListingListAPIResponse> getListingListAPIResponseMutableLiveData() {
        return listingListAPIResponseMutableLiveData;
    }

    public MutableLiveData<DesiredItemsAPIResponse> getDesiredItemsAPIResponseMutableLiveData() {
        return desiredItemsAPIResponseMutableLiveData;
    }

    public MutableLiveData<ImageIDAPIResponse> getImageIDAPIResponseMutableLiveData() {
        return imageIDAPIResponseMutableLiveData;
    }

    public void testConnectionNoAuth() {
        apiService.testConnection().enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    System.out.println("test request failed : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println("test request failed on Failure : " + t.toString());
            }
        });
    }

    public void testConnectionAuthenticated(String AuthHeaderToken) {
        apiService.testConnectionAuthenticated(AuthHeaderToken).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    baseAPIResponseMutableLiveData.setValue((response.body()));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {

            }
        });

    }

    public void checkUsernameUnique(String username) {
        apiService.checkUsernameUnique(username).enqueue(new Callback<CheckAvailableAPIResponse>() {
            @Override
            public void onResponse(Call<CheckAvailableAPIResponse> call, Response<CheckAvailableAPIResponse> response) {
                if (response.isSuccessful()) {
                    CheckAvailableAPIResponseMutableLiveData.setValue(response.body());

                }

            }

            @Override
            public void onFailure(Call<CheckAvailableAPIResponse> call, Throwable t) {

            }
        });
    }

    public void checkEmailUnique(String email) {
        apiService.checkEmailUnique(email).enqueue(new Callback<CheckAvailableAPIResponse>() {
            @Override
            public void onResponse(Call<CheckAvailableAPIResponse> call, Response<CheckAvailableAPIResponse> response) {
                if (response.isSuccessful()) {
                    CheckAvailableAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CheckAvailableAPIResponse> call, Throwable t) {

            }
        });
    }

    public void createListing(String AuthHeaderToken, Integer title, Integer body, String end_date, String location, List<Integer> pageNum, String main_photoID) {
        apiService.createListing(AuthHeaderToken, title, body, end_date, location, pageNum, main_photoID).enqueue(new Callback<ListingIDAPIResponse>() {
            @Override
            public void onResponse(Call<ListingIDAPIResponse> call, Response<ListingIDAPIResponse> response) {
                if (response.isSuccessful()) {
                    listingIDAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ListingIDAPIResponse> call, Throwable t) {

            }
        });
    }

    public void addListingtoWatchList(String AuthHeaderToken, String listingID) {
        apiService.addListingtoWatchList(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    baseAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {

            }
        });
    }

    public void removeListingfromWatchList(String AuthHeaderToken, String listingID) {
        apiService.removeListingfromWatchList(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    baseAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {

            }
        });
    }

    public void getWatchlist(String AuthHeaderToken) {
        apiService.getWatchlist(AuthHeaderToken).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()) {
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {

            }
        });
    }

    public void getRecentListings(String AuthHeaderToken, Integer pageNum) {
        apiService.getRecentListings(AuthHeaderToken, pageNum).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()) {
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {

            }
        });
    }


    public void getListingAuthenticated(String AuthHeaderToken, String listingID) {
        apiService.getListingAuthenticated(AuthHeaderToken, listingID).enqueue(new Callback<SingleListingAPIResponse>() {
            @Override
            public void onResponse(Call<SingleListingAPIResponse> call, Response<SingleListingAPIResponse> response) {
                if (response.isSuccessful()) {
                    singleListingAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SingleListingAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });

    }

    public void getListingNoAuth(String listingID) {
        apiService.getListingNoAuth(listingID).enqueue(new Callback<SingleListingAPIResponse>() {
            @Override
            public void onResponse(Call<SingleListingAPIResponse> call, Response<SingleListingAPIResponse> response) {
                if (response.isSuccessful()) {
                    singleListingAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SingleListingAPIResponse> call, Throwable t) {

            }
        });
    }

    public void reserveItems(String AuthHeaderToken, List<String> listingItemIds){
        apiService.reserveItemsRequest(AuthHeaderToken,listingItemIds).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                baseAPIResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {

            }
        });
    }

    public void getFrontPageListings() {
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
            }


        });
    }

    public void getFilteredListings(String searchString, Integer pageNum) {
        apiService.getFilteredListings(searchString, pageNum).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {

            }

            @Override
            public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {

            }
        });
    }

    public void getDesiredItems(Integer pageNum) {
        apiService.getDesiredItems(pageNum).enqueue(new Callback<DesiredItemsAPIResponse>() {
            @Override
            public void onResponse(Call<DesiredItemsAPIResponse> call, Response<DesiredItemsAPIResponse> response) {
                if (response.isSuccessful()) {
                    desiredItemsAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<DesiredItemsAPIResponse> call, Throwable t) {

            }
        });
    }

    public void login(String Username,String saltedHashedPassword) {
        String base64EncodedUP = AuthenticationHelper.getHeaderB64(Username,saltedHashedPassword);

        apiService.login(base64EncodedUP).enqueue(new Callback<UserTokenAPIResponse>() {
            @Override
            public void onResponse(Call<UserTokenAPIResponse> call, Response<UserTokenAPIResponse> response) {
                if (response.isSuccessful()) {
                    System.out.println("data set");
                    userTokenAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    System.out.println("NOT SUCCESFULL");
                }
            }

            @Override
            public void onFailure(Call<UserTokenAPIResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public void getUserProfile(String userID) {
        apiService.getUserProfile(userID).enqueue(new Callback<UserAPIResponse>() {
            @Override
            public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
                if (response.isSuccessful()) {
                    userAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserAPIResponse> call, Throwable t) {

            }
        });
    }

    public void changeWantedTags(String AuthHeaderToken, List<String> new_tags) {
        apiService.changeWantedTags(AuthHeaderToken, new_tags).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    baseAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {

            }
        });
    }

    public void createAccount(User userObj) {
        System.out.println("REPOSITORY RECEIVED");

        apiService.createAccount(userObj.getUsername(), userObj.getPassword(), userObj.getFirst_name(), userObj.getLast_name(), userObj.getEmail(), userObj.getPhone()).enqueue(new Callback<UserTokenAPIResponse>() {
            @Override
            public void onResponse(Call<UserTokenAPIResponse> call, Response<UserTokenAPIResponse> response) {
                if (response.isSuccessful()) {
                    userTokenAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    System.out.println("Not succesfull");
                    System.out.println(response.errorBody());
                }


            }

            @Override
            public void onFailure(Call<UserTokenAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void saveImageonServer(String temp_imageID, MultipartBody.Part file) {
        apiService.saveImageonServer(temp_imageID, file).enqueue(new Callback<ImageIDAPIResponse>() {
            @Override
            public void onResponse(Call<ImageIDAPIResponse> call, Response<ImageIDAPIResponse> response) {
                if (response.isSuccessful()) {
                    imageIDAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ImageIDAPIResponse> call, Throwable t) {

            }
        });
    }

    public void deleteListing(String AuthHeaderToken, String listingID) {
        apiService.deleteListing(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    baseAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {

            }
        });
    }


}