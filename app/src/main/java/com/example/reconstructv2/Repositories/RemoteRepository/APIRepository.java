package com.example.reconstructv2.Repositories.RemoteRepository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
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
import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import rx.functions.FuncN;


public class APIRepository {
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
        this.apiService = RetroFactory.getRetrofitInstance();

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

        this.mContext = application.getApplicationContext();

    }

    public APIService getApiService() {
        return apiService;
    }

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

    public void testConnectionNoAuth() {
        apiService.testConnection().enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(new BaseAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void testConnectionAuthenticated(String AuthHeaderToken) {
        apiService.testConnectionAuthenticated(AuthHeaderToken).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue((response.body()));
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(new BaseAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });

    }

    public void checkUsernameUnique(String username) {
        apiService.checkUsernameUnique(username).enqueue(new Callback<CheckAvailableAPIResponse>() {
            @Override
            public void onResponse(Call<CheckAvailableAPIResponse> call, Response<CheckAvailableAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    CheckUsernameAvailableAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    CheckUsernameAvailableAPIResponseMutableLiveData.setValue(new CheckAvailableAPIResponse(false));
                }

            }

            @Override
            public void onFailure(Call<CheckAvailableAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void checkEmailUnique(String email) {
        apiService.checkEmailUnique(email).enqueue(new Callback<CheckAvailableAPIResponse>() {
            @Override
            public void onResponse(Call<CheckAvailableAPIResponse> call, Response<CheckAvailableAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    CheckEmailAvailableAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    CheckUsernameAvailableAPIResponseMutableLiveData.setValue(new CheckAvailableAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<CheckAvailableAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void createListing(String AuthHeaderToken, ListingFull listing) {
        apiService.createListing(AuthHeaderToken,listing).enqueue(new Callback<ListingIDAPIResponse>() {
            @Override
            public void onResponse(Call<ListingIDAPIResponse> call, Response<ListingIDAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingIDAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    listingIDAPIResponseMutableLiveData.setValue(new ListingIDAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<ListingIDAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void addListingtoWatchList(String AuthHeaderToken, String listingID) {
        apiService.addListingtoWatchList(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(new BaseAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void removeListingfromWatchList(String AuthHeaderToken, String listingID) {
        apiService.removeListingfromWatchList(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(new BaseAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void getWatchlist(String AuthHeaderToken) {
        apiService.getWatchlist(AuthHeaderToken).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    listingListAPIResponseMutableLiveData.setValue(new ListingListAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void getRecentListings(String AuthHeaderToken, Integer pageNum) {
        apiService.getRecentListings(AuthHeaderToken, pageNum).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    listingListAPIResponseMutableLiveData.setValue(new ListingListAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }


    public void getListingAuthenticated(String AuthHeaderToken, String listingID) {
        apiService.getListingAuthenticated(AuthHeaderToken, listingID).enqueue(new Callback<SingleListingAPIResponse>() {
            @Override
            public void onResponse(Call<SingleListingAPIResponse> call, Response<SingleListingAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    singleListingAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    singleListingAPIResponseMutableLiveData.setValue(new SingleListingAPIResponse(false));
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
                    response.body().setIsSuccesfull(true);
                    singleListingAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    singleListingAPIResponseMutableLiveData.setValue(new SingleListingAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<SingleListingAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void reserveItems(String AuthHeaderToken, List<JsonObject> listingItems){

        apiService.reserveItemsRequest(AuthHeaderToken,listingItems).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(new BaseAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void getFrontPageListings() {
        apiService.getFrontPageListings(0).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    listingListAPIResponseMutableLiveData.setValue(new ListingListAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {
                System.out.println(t);
            }


        });
    }

    public void getFilteredListings(String searchString, Integer pageNum) {
        apiService.getFilteredListings(searchString, pageNum).enqueue(new Callback<ListingListAPIResponse>() {
            @Override
            public void onResponse(Call<ListingListAPIResponse> call, Response<ListingListAPIResponse> response) {
                if (response.isSuccessful()){
                    listingListAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    listingListAPIResponseMutableLiveData.setValue(new ListingListAPIResponse(false));
                }

            }

            @Override
            public void onFailure(Call<ListingListAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void getDesiredItems(Integer pageNum) {
        apiService.getDesiredItems(pageNum).enqueue(new Callback<DesiredItemsAPIResponse>() {
            @Override
            public void onResponse(Call<DesiredItemsAPIResponse> call, Response<DesiredItemsAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    desiredItemsAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    desiredItemsAPIResponseMutableLiveData.setValue(new DesiredItemsAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<DesiredItemsAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void login(String Username,String saltedHashedPassword) {
        String base64EncodedUP = AuthenticationHelper.getHeaderB64(Username,saltedHashedPassword);

        apiService.login(base64EncodedUP).enqueue(new Callback<UserTokenAPIResponse>() {
            @Override
            public void onResponse(Call<UserTokenAPIResponse> call, Response<UserTokenAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    userTokenAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    userTokenAPIResponseMutableLiveData.setValue(new UserTokenAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<UserTokenAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void getUserProfile(final String userID) {
        apiService.getUserProfile(userID).enqueue(new Callback<UserAPIResponse>() {
            @Override
            public void onResponse(Call<UserAPIResponse> call, Response<UserAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    userAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    userAPIResponseMutableLiveData.setValue(new UserAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<UserAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void changeWantedTags(String AuthHeaderToken, List<String> new_tags) {
        apiService.changeWantedTags(AuthHeaderToken, new_tags).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(new BaseAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void createAccount(final User userObj) {

        apiService.createAccount(userObj).enqueue(new Callback<UserTokenAPIResponse>() {
            @Override
            public void onResponse(Call<UserTokenAPIResponse> call, Response<UserTokenAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    userTokenAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    userTokenAPIResponseMutableLiveData.setValue(new UserTokenAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<UserTokenAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void saveUser(String authHeaderToken,User user){
        apiService.saveUser(authHeaderToken,user).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()){
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(new BaseAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    public void saveImageonServer(String authHeader, Uri imageUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        CursorLoader cursorLoader = new CursorLoader(
                mContext,
                imageUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        File imageFile = new File(cursor.getString(column_index));

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part body =MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        apiService.saveImageOnServer(authHeader, body).enqueue(new Callback<ImageIDAPIResponse>() {
            @Override
            public void onResponse(Call<ImageIDAPIResponse> call, Response<ImageIDAPIResponse> response) {
                if (response.isSuccessful()){
                    response.body().setIsSuccesfull(true);
                    imageIDAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    imageIDAPIResponseMutableLiveData.setValue(new ImageIDAPIResponse(false));
                }

            }

            @Override
            public void onFailure(Call<ImageIDAPIResponse> call, Throwable t) {

            }
        });
    }

    public void deleteImage(String authHeader, String imageID) {

        apiService.deleteImageFromServer(authHeader,imageID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.body().getIsSuccesfull()){
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
            }
        });

    }

    public void deleteListing(String AuthHeaderToken, String listingID) {
        apiService.deleteListing(AuthHeaderToken, listingID).enqueue(new Callback<BaseAPIResponse>() {
            @Override
            public void onResponse(Call<BaseAPIResponse> call, Response<BaseAPIResponse> response) {
                if (response.isSuccessful()) {
                    response.body().setIsSuccesfull(true);
                    baseAPIResponseMutableLiveData.setValue(response.body());
                } else {
                    RequestErrorHandler.displayErrorMessage(mContext, response);
                    baseAPIResponseMutableLiveData.setValue(new BaseAPIResponse(false));
                }
            }

            @Override
            public void onFailure(Call<BaseAPIResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}