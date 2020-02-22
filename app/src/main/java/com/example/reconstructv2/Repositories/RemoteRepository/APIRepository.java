package com.example.reconstructv2.Repositories.RemoteRepository;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

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
    private MutableLiveData<CheckAvailableAPIResponse> CheckAvailableAPIResponseMutableLiveData;


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

    // return current api service instance
    public APIService getApiService() {
        return apiService;
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

    // handles a succesfull response to a request
    private void responseHandler(Response response, Class apiResponseClass, MutableLiveData mutableLiveData){

        // check if the response is sucessfull
        if (response.isSuccessful()) {

            // set the response is succesfull
            response.body().setIsSuccesfull(true);

            // update the value of the MutableLiveData
            mutableLiveData.setValue(response.body());

        } else {
            // if the request is not succesfull (server error)
            // display an error message to the user
            RequestErrorHandler.displayErrorMessage(mContext, response);

            // create a new response object with the isSucessful value set to false
            Object tempResponse = apiResponseClass.newInstance(false);

            // set the message to the error message from the response
            tempResponse.setMessage(response.message());

            // update the value of the mutableLiveData
            mutableLiveData.setValue(tempResponse);
        }
    }

    // handles a request failure
    private void failureHandler(Throwable t, Class apiResponseClass, MutableLiveData mutableLiveData){

        // create a new response object with the isSucessful value set to false
        Object tempResponse = apiResponseClass.newInstance(false);
        
        // set the message to the error message from the response
        tempResponse.setMessage(t.getMessage());
    
        // update the value of the mutableLiveData
        mutableLiveData.setValue(tempResponse);
    }

    private Callback<BaseAPIResponse> BaseAPIResponseCallback = new Callback<BaseAPIResponse>() {

        @Override
        public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
            responseHandler(response, BaseAPIResponse.class, baseAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
            failureHandler(t, BaseAPIResponse.class, baseAPIResponseMutableLiveData)
        }
    };


    private Callback<CheckAvailableAPIResponse> CheckAvailableAPIResponseCallback = new Callback<CheckAvailableAPIResponse>() {

        @Override
        public void onResponse(Call<CheckAvailableAPIResponse> call, Response<CheckAvailableAPIResponse> response) {
            responseHandler(response, CheckAvailableAPIResponse.class, CheckAvailableAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<CheckAvailableAPIResponse> call, Throwable t) {
            failureHandler(t, CheckAvailableAPIResponse.class, CheckAvailableAPIResponseMutableLiveData)
        }
    };


    private Callback<DesiredItemsAPIResponse> DesiredItemsAPIResponseCallback = new Callback<DesiredItemsAPIResponse>() {

        @Override
        public void onResponse(Call<DesiredItemsAPIResponse> call, Response<DesiredItemsAPIResponse> response) {
            responseHandler(response, DesiredItemsAPIResponse.class, DesiredItemsAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<DesiredItemsAPIResponse> call, Throwable t) {
            failureHandler(t, DesiredItemsAPIResponse.class, DesiredItemsAPIResponseMutableLiveData)
        }
    };


    private Callback<ImageIDAPIResponse> ImageIDAPIResponseCallback = new Callback<ImageIDAPIResponse>() {

        @Override
        public void onResponse(Call<ImageIDAPIResponse> call, Response<ImageIDAPIResponse> response) {
            responseHandler(response, ImageIDAPIResponse.class, ImageIDAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<ImageIDAPIResponse> call, Throwable t) {
            failureHandler(t, ImageIDAPIResponse.class, ImageIDAPIResponseMutableLiveData)
        }
    };


    private Callback<ListingIDAPIResponse> ListingIDAPIResponseCallback = new Callback<ListingIDAPIResponse>() {

        @Override
        public void onResponse(Call<ListingIDAPIResponse> call, Response<ListingIDAPIResponse> response) {
            responseHandler(response, ListingIDAPIResponse.class, ListingIDAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<ListingIDAPIResponse> call, Throwable t) {
            failureHandler(t, ListingIDAPIResponse.class, ListingIDAPIResponseMutableLiveData)
        }
    };


    private Callback<ListingListAPIResponse> ListingListAPIResponseCallback = new Callback<ListingListAPIResponse>() {

        @Override
        public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {
            responseHandler(response, ListingListAPIResponse.class, ListingListAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {
            failureHandler(t, ListingListAPIResponse.class, ListingListAPIResponseMutableLiveData)
        }
    };


    private Callback<SingleListingAPIResponse> SingleListingAPIResponseCallback = new Callback<SingleListingAPIResponse>() {

        @Override
        public void onResponse(Call<SingleListingAPIResponse> call, Response<SingleListingAPIResponse> response) {
            responseHandler(response, SingleListingAPIResponse.class, SingleListingAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<SingleListingAPIResponse> call, Throwable t) {
            failureHandler(t, SingleListingAPIResponse.class, SingleListingAPIResponseMutableLiveData)
        }
    };


    private Callback<UserAPIResponse> UserAPIResponseCallback = new Callback<UserAPIResponse>() {

        @Override
        public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
            responseHandler(response, UserAPIResponse.class, UserAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<UserAPIResponse> call, Throwable t) {
            failureHandler(t, UserAPIResponse.class, UserAPIResponseMutableLiveData)
        }
    };


    private Callback<UserTokenAPIResponse> UserTokenAPIResponseCallback = new Callback<UserTokenAPIResponse>() {

        @Override
        public void onResponse(Call<UserTokenAPIResponse> call, Response<UserTokenAPIResponse> response) {
            responseHandler(response, UserTokenAPIResponse.class, UserTokenAPIResponseMutableLiveData);
        }

        @Override
        public void onFailure(Call<UserTokenAPIResponse> call, Throwable t) {
            failureHandler(t, UserTokenAPIResponse.class, UserTokenAPIResponseMutableLiveData)
        }
    };
    
    // TESTS

    public void testConnectionNoAuth() {
        apiService.testConnection().enqueue(BaseAPIResponseCallback);
    }

    public void testConnectionAuthenticated(String AuthHeaderToken) {
        apiService.testConnectionAuthenticated(AuthHeaderToken).enqueue(BaseAPIResponseCallback);

    }

    // UNIQUE CHECKS

    public void checkUsernameUnique(String username) {
        apiService.checkUsernameUnique(username).enqueue(CheckAvailableAPIResponseCallback);
    }

    public void checkEmailUnique(String email) {
        apiService.checkEmailUnique(email).enqueue(CheckAvailableAPIResponseCallback);
    }

    // LISTINGS

    public void createListing(String AuthHeaderToken, ListingFull listing) {
        apiService.createListing(AuthHeaderToken, listing).enqueue(ListingIDAPIResponseCallback);
    }

    public void addListingtoWatchList(String AuthHeaderToken, String listingID) {
        apiService.addListingtoWatchList(AuthHeaderToken, listingID).enqueue(BaseAPIResponseCallback);
    }

    public void removeListingfromWatchList(String AuthHeaderToken, String listingID) {
        apiService.removeListingfromWatchList(AuthHeaderToken, listingID).enqueue(BaseAPIResponseCallback);
    }

    public void getWatchlist(String AuthHeaderToken) {
        apiService.getWatchlist(AuthHeaderToken).enqueue(ListingListAPIResponseCallback);
    }

    ////
    public void getRecentListings(String AuthHeaderToken, Integer pageNum) {
        apiService.getRecentListings(AuthHeaderToken, pageNum, listingsPerPage).enqueue(ListingListAPIResponseCallback);
    }

    public void getListingAuthenticated(String AuthHeaderToken, String listingID) {
        apiService.getListingAuthenticated(AuthHeaderToken, listingID).enqueue(SingleListingAPIResponseCallback);
    }

    public void getListingNoAuth(String listingID) {
        apiService.getListingNoAuth(listingID).enqueue(SingleListingAPIResponseCallback);
    }

    public void reserveItems(String AuthHeaderToken, List<JsonObject> listingItems) {
        apiService.reserveItemsRequest(AuthHeaderToken, listingItems).enqueue(BaseAPIResponseCallback);
    }

    ///
    public void getFrontPageListings() {
        apiService.getFrontPageListings(0,listingsPerPage).enqueue(ListingListAPIResponseCallback);
    }

    public void getUserListings(String userID) {
        apiService.getUserListings(userID).enqueue(ListingListAPIResponseCallback);
    }

    ///
    public void getFilteredListings(String searchString, Integer pageNum) {
        apiService.getFilteredListings(searchString, pageNum,listingsPerPage).enqueue(ListingListAPIResponseCallback);
    }

    ///
    public void getDesiredItems(Integer pageNum) {
        apiService.getDesiredItems(pageNum,listingsPerPage).enqueue(DesiredItemsAPIResponseCallback);
    }

    // USER MANAGEMENT

    public void login(String Username, String saltedHashedPassword) {
        String base64EncodedUP = AuthenticationHelper.getHeaderB64(Username, saltedHashedPassword);

        apiService.login(base64EncodedUP).enqueue(UserTokenAPIResponseCallback);
    }

    public void getUserProfile(final String userID) {
        apiService.getUserProfile(userID).enqueue(UserAPIResponseCallback);
    }

    public void changeWantedTags(String AuthHeaderToken, List<String> new_tags) {
        apiService.changeWantedTags(AuthHeaderToken, new_tags).enqueue(BaseAPIResponseCallback);
    }

    public void createAccount(final User userObj) {

        apiService.createAccount(userObj).enqueue(UserTokenAPIResponseCallback);
    }

    public void saveUser(String authHeaderToken, User user) {
        apiService.saveUser(authHeaderToken, user).enqueue(BaseAPIResponseCallback);
    }

    public void saveImageonServer(String authHeader, Uri imageUri) {

        String[] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader = new CursorLoader(
                mContext,
                imageUri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        File imageFile = new File(cursor.getString(column_index));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        apiService.saveImageOnServer(authHeader, body).enqueue(ImageIDAPIResponseCallback);
    }

    public void deleteImage(String authHeader, List<String> imageIDs) {
        apiService.deleteImageFromServer(authHeader, imageIDs).enqueue(BaseAPIResponseCallback);
    }

    public void deleteListing(String AuthHeaderToken, String listingID) {
        apiService.deleteListing(AuthHeaderToken, listingID).enqueue(BaseAPIResponseCallback);
    }
}