package com.example.reconstructv2.Fragments.AccountManagement.AccountEdit;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class AccountEditViewModel extends AndroidViewModel {

    private APIRepository apiRepository;
    private MutableLiveData<UserAPIResponse> userAPIResponse;
    private MutableLiveData<BaseAPIResponse> baseAPIResponse;

    private MutableLiveData<CheckAvailableAPIResponse> usernameIsUniqueResponse;
    private MutableLiveData<CheckAvailableAPIResponse> emailIsUniqueResponse;

    public AccountEditViewModel(@NonNull Application application) {
        super(application);

        // fetch a new instance of the API repository
        apiRepository = new APIRepository(application);

        userAPIResponse = apiRepository.getUserAPIResponseMutableLiveData();
        baseAPIResponse = apiRepository.getBaseAPIResponseMutableLiveData();

        usernameIsUniqueResponse = apiRepository.getCheckUsernameAvailableAPIResponseMutableLiveData();
        emailIsUniqueResponse = apiRepository.getCheckEmailAvailableAPIResponseMutableLiveData();

    }

    // Getters for MutableLiveData

    public MutableLiveData<UserAPIResponse> getUserAPIResponseMutableLiveData() {
        return userAPIResponse;
    }

    public MutableLiveData<BaseAPIResponse> getBaseAPIResponseMutableLiveData() {return baseAPIResponse;}

    public MutableLiveData<CheckAvailableAPIResponse> getUsernameIsUniqueResponseMutableLiveData() {
        return usernameIsUniqueResponse;
    }

    public MutableLiveData<CheckAvailableAPIResponse> getEmailIsUniqueResponseMutableLiveData() {
        return emailIsUniqueResponse;
    }


    // functions that send requests to the server

    public void sendGetUserRequest(String userID){
        apiRepository.getUserProfile(userID);
    }

    public void sendSaveUserRequest(Context context, User user){
        apiRepository.saveUser( UserInfo.getAuthenticationHeader(context),user);
    }

    public void sendGetUsernameIsUniqueRequest(String username){
        apiRepository.checkUsernameUnique(username);
    }

    public void sendGetEmailIsUniqueRequest(String email){
        apiRepository.checkEmailUnique(email);
    }




}
