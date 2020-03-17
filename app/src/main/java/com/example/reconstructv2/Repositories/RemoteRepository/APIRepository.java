package com.example.reconstructv2.Repositories.RemoteRepository;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;
import androidx.loader.content.CursorLoader;

import com.example.reconstructv2.Helpers.AuthenticationHelper;
import com.example.reconstructv2.Helpers.RequestErrorHandler;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.DesiredItemsAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ImageIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.User;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class APIRepository {

    private Integer listingsPerPage = 10;

    private APIService apiService;

    private MutableLiveData<CheckAvailableAPIResponse> CheckUsernameAvailableAPIResponseMutableLiveData;
    private MutableLiveData<CheckAvailableAPIResponse> CheckEmailAvailableAPIResponseMutableLiveData;

    private MutableLiveData<BaseAPIResponse> baseAPIResponseMutableLiveData;
    private MutableLiveData<ListingIDAPIResponse> listingIDAPIResponseMutableLiveData;
    private MutableLiveData<ListingListAPIResponse> listingListAPIResponseMutableLiveData;
    private MutableLiveData<SingleListingAPIResponse> singleListingAPIResponseMutableLiveData;
    private MutableLiveData<UserAPIResponse> userAPIResponseMutableLiveData;
    private MutableLiveData<UserTokenAPIResponse> userTokenAPIResponseMutableLiveData;
    private MutableLiveData<DesiredItemsAPIResponse> desiredItemsAPIResponseMutableLiveData;
    private MutableLiveData<ImageIDAPIResponse> imageIDAPIResponseMutableLiveData;

    private Context mContext;

    public APIRepository(Application application) {
        this.mContext = application.getApplicationContext();

        this.apiService = RetroFactory.getRetrofitInstance(mContext);

        this.CheckUsernameAvailableAPIResponseMutableLiveData = new MutableLiveData<>();
        this.CheckEmailAvailableAPIResponseMutableLiveData = new MutableLiveData<>();
        this.baseAPIResponseMutableLiveData = new MutableLiveData<>();
        this.listingIDAPIResponseMutableLiveData = new MutableLiveData<>();
        this.listingListAPIResponseMutableLiveData = new MutableLiveData<>();
        this.singleListingAPIResponseMutableLiveData = new MutableLiveData<>();
        this.userAPIResponseMutableLiveData = new MutableLiveData<>();
        this.userTokenAPIResponseMutableLiveData = new MutableLiveData<>();
        this.desiredItemsAPIResponseMutableLiveData = new MutableLiveData<>();
        this.imageIDAPIResponseMutableLiveData = new MutableLiveData<>();


    }

    //// getters for mutable live data for each data type
    // mutable live data is observed to get the results from a request

    public MutableLiveData<CheckAvailableAPIResponse> getCheckUsernameAvailableAPIResponseMutableLiveData() {
        return CheckUsernameAvailableAPIResponseMutableLiveData;
    }

    public MutableLiveData<CheckAvailableAPIResponse> getCheckEmailAvailableAPIResponseMutableLiveData() {
        return CheckEmailAvailableAPIResponseMutableLiveData;
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

    //
    // each function sends a request to the route defined in api service
    // then the callback is called when the request returns
    // if the request is successful
    // the isSuccessful property is set to true and the mutable live data object is updated
    // if the request is not successful, the error is handled
    // then a new response object is created containing the message from the response
    // and the mutable live data is updated so that
    // the fragment can react accordingly


    // TESTS

    public void testConnectionNoAuth() {
        apiService.testConnection().enqueue(new Callback<BaseAPIResponse>() {

            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {

                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());

                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);

                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void testConnectionAuthenticated(String AuthHeaderToken) {
        apiService.testConnectionAuthenticated(AuthHeaderToken).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue((response.body()));
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);

                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });

    }

    // UNIQUE CHECKS

    public void checkUsernameUnique(String username) {
        apiService.checkUsernameUnique(username).enqueue(new Callback<CheckAvailableAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<CheckAvailableAPIResponse> call, @NotNull Response<CheckAvailableAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    CheckUsernameAvailableAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);

                    CheckAvailableAPIResponse tempResponse = new CheckAvailableAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    CheckUsernameAvailableAPIResponseMutableLiveData.setValue(tempResponse);
                }

            }

            @Override
            public void onFailure(@NotNull Call<CheckAvailableAPIResponse> call, @NotNull Throwable t) {
                CheckAvailableAPIResponse tempResponse = new CheckAvailableAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                CheckUsernameAvailableAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void checkEmailUnique(String email) {
        apiService.checkEmailUnique(email).enqueue(new Callback<CheckAvailableAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<CheckAvailableAPIResponse> call, @NotNull Response<CheckAvailableAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    CheckEmailAvailableAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    CheckAvailableAPIResponse tempResponse = new CheckAvailableAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    CheckUsernameAvailableAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CheckAvailableAPIResponse> call, @NotNull Throwable t) {
                CheckAvailableAPIResponse tempResponse = new CheckAvailableAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                CheckUsernameAvailableAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    // LISTINGS

    public void createListing(String AuthHeaderToken, ListingFull listing) {
        apiService.createListing(AuthHeaderToken, listing).enqueue(new Callback<ListingIDAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<ListingIDAPIResponse> call, @NotNull Response<ListingIDAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingIDAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    ListingIDAPIResponse tempResponse = new ListingIDAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    listingIDAPIResponseMutableLiveData.setValue(tempResponse);

                }
            }

            @Override
            public void onFailure(@NotNull Call<ListingIDAPIResponse> call, @NotNull Throwable t) {
                ListingIDAPIResponse tempResponse = new ListingIDAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                listingIDAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void addListingtoWatchList(String AuthHeaderToken, String listingID) {
        apiService.addListingtoWatchList(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void removeListingfromWatchList(String AuthHeaderToken, String listingID) {
        apiService.removeListingfromWatchList(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void getWatchlist(String AuthHeaderToken, Integer pageNumber) {
        apiService.getWatchlist(AuthHeaderToken, pageNumber, listingsPerPage).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<ListingListAPIResponse> call, @NotNull Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    listingListAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ListingListAPIResponse> call, @NotNull Throwable t) {
                ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                listingListAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    ////
    public void getRecentListings(String AuthHeaderToken, Integer pageNum) {
        apiService.getRecentListings(AuthHeaderToken, pageNum, listingsPerPage).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<ListingListAPIResponse> call, @NotNull Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    listingListAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ListingListAPIResponse> call, @NotNull Throwable t) {
                ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                listingListAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }


    public void getListingAuthenticated(String AuthHeaderToken, String listingID) {
        apiService.getListingAuthenticated(AuthHeaderToken, listingID).enqueue(new Callback<SingleListingAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<SingleListingAPIResponse> call, @NotNull Response<SingleListingAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    singleListingAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    SingleListingAPIResponse tempResponse = new SingleListingAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    singleListingAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<SingleListingAPIResponse> call, @NotNull Throwable t) {
                SingleListingAPIResponse tempResponse = new SingleListingAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                singleListingAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });

    }

    public void getListingNoAuth(String listingID) {
        apiService.getListingNoAuth(listingID).enqueue(new Callback<SingleListingAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<SingleListingAPIResponse> call, @NotNull Response<SingleListingAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    singleListingAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    SingleListingAPIResponse tempResponse = new SingleListingAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    singleListingAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<SingleListingAPIResponse> call, @NotNull Throwable t) {
                SingleListingAPIResponse tempResponse = new SingleListingAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                singleListingAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void reserveItems(String AuthHeaderToken, List<JsonObject> listingItems) {

        apiService.reserveItemsRequest(AuthHeaderToken, listingItems).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }


    public void getFrontPageListings(double locationFilterLimit, double userLat, double userLon, String postDateSortType, String endDateSortType, String distanceSortType, Integer pageNum) {
        apiService.getFrontPageListings(locationFilterLimit, userLat, userLon, postDateSortType, endDateSortType, distanceSortType, pageNum, listingsPerPage).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<ListingListAPIResponse> call, @NotNull Response<ListingListAPIResponse> response) {


                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    listingListAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ListingListAPIResponse> call, @NotNull Throwable t) {
                ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                listingListAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void getUserListings(String userID, Integer pageNumber) {
        apiService.getUserListings(userID, pageNumber, listingsPerPage).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<ListingListAPIResponse> call, @NotNull Response<ListingListAPIResponse> response) {

                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    listingListAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ListingListAPIResponse> call, @NotNull Throwable t) {
                ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                listingListAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }


    public void getFilteredListings(String searchString, double locationFilterLimit, double userLat, double userLon, String postDateSortType, String endDateSortType, String distanceSortType, Integer pageNum) {
        apiService.getFilteredListings(searchString, locationFilterLimit, userLat, userLon, postDateSortType, endDateSortType, distanceSortType, pageNum, listingsPerPage).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<ListingListAPIResponse> call, @NotNull Response<ListingListAPIResponse> response) {


                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    listingListAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ListingListAPIResponse> call, @NotNull Throwable t) {
                ListingListAPIResponse tempResponse = new ListingListAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                listingListAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    ///
    public void getDesiredItems(Integer pageNum) {
        apiService.getDesiredItems(pageNum, listingsPerPage).enqueue(new Callback<DesiredItemsAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<DesiredItemsAPIResponse> call, @NotNull Response<DesiredItemsAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    desiredItemsAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    DesiredItemsAPIResponse tempResponse = new DesiredItemsAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    desiredItemsAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<DesiredItemsAPIResponse> call, @NotNull Throwable t) {
                DesiredItemsAPIResponse tempResponse = new DesiredItemsAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                desiredItemsAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    // USER MANAGEMENT

    public void login(String Username, String saltedHashedPassword) {
        String base64EncodedUP = AuthenticationHelper.getHeaderB64(Username, saltedHashedPassword);

        apiService.login(base64EncodedUP).enqueue(new Callback<UserTokenAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserTokenAPIResponse> call, @NotNull Response<UserTokenAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    userTokenAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    UserTokenAPIResponse tempResponse = new UserTokenAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    userTokenAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserTokenAPIResponse> call, @NotNull Throwable t) {
                UserTokenAPIResponse tempResponse = new UserTokenAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                userTokenAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void logOut(String authHeader) {

        apiService.logout(authHeader).enqueue(new Callback<BaseAPIResponse>() {

            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void getUserProfile(final String userID) {
        apiService.getUserProfile(userID).enqueue(new Callback<UserAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserAPIResponse> call, @NotNull Response<UserAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    userAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    UserAPIResponse tempResponse = new UserAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    userAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserAPIResponse> call, @NotNull Throwable t) {
                UserAPIResponse tempResponse = new UserAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                userAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void changeWantedTags(String AuthHeaderToken, List<String> new_tags) {
        apiService.changeWantedTags(AuthHeaderToken, new_tags).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void createAccount(final User userObj) {

        apiService.createAccount(userObj).enqueue(new Callback<UserTokenAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<UserTokenAPIResponse> call, @NotNull Response<UserTokenAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    userTokenAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    UserTokenAPIResponse tempResponse = new UserTokenAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    userTokenAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserTokenAPIResponse> call, @NotNull Throwable t) {
                UserTokenAPIResponse tempResponse = new UserTokenAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                userTokenAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void saveUser(String authHeaderToken, User user) {
        apiService.saveUser(authHeaderToken, user).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void changePassword(String authHeader, String newPassword) {
        apiService.updatePassword(authHeader, newPassword).enqueue(new Callback<BaseAPIResponse>() {

            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void saveImageOnServer(String authHeader, Uri imageUri) {

        String[] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader = new CursorLoader(
                mContext,
                imageUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        File imageFile = new File(cursor.getString(column_index));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        apiService.saveImageOnServer(authHeader, body).enqueue(new Callback<ImageIDAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<ImageIDAPIResponse> call, @NotNull Response<ImageIDAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    imageIDAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    ImageIDAPIResponse tempResponse = new ImageIDAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    imageIDAPIResponseMutableLiveData.setValue(tempResponse);
                }

            }

            @Override
            public void onFailure(Call<ImageIDAPIResponse> call, Throwable t) {
                ImageIDAPIResponse tempResponse = new ImageIDAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                imageIDAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }

    public void deleteImage(String authHeader, List<String> imageIDs) {

        apiService.deleteImageFromServer(authHeader, imageIDs).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });

    }

    public void deleteListing(String AuthHeaderToken, String listingID) {
        apiService.deleteListing(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(@NotNull Call<BaseAPIResponse> call, @NotNull Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                    tempResponse.setMessage(response.message());
                    baseAPIResponseMutableLiveData.setValue(tempResponse);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseAPIResponse> call, @NotNull Throwable t) {
                BaseAPIResponse tempResponse = new BaseAPIResponse(false);
                tempResponse.setMessage(t.getMessage());
                baseAPIResponseMutableLiveData.setValue(tempResponse);
            }
        });
    }
}