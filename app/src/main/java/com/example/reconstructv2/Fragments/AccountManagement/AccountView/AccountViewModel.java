package com.example.reconstructv2.Fragments.AccountManagement.AccountView;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class AccountViewModel extends AndroidViewModel {

    private APIRepository apiRepository;
    private MutableLiveData<UserAPIResponse> userAPIResponse;
    private MutableLiveData<BaseAPIResponse> baseAPIResponse;

    private MutableLiveData<CheckAvailableAPIResponse> usernameIsUniqueResponse;
    private MutableLiveData<CheckAvailableAPIResponse> emailIsUniqueueResponse;

    public AccountViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);
        userAPIResponse = apiRepository.getUserAPIResponseMutableLiveData();
        baseAPIResponse = apiRepository.getBaseAPIResponseMutableLiveData();

        usernameIsUniqueResponse = apiRepository.getCheckUsernameAvailableAPIResponseMutableLiveData();
        emailIsUniqueueResponse = apiRepository.getCheckEmailAvailableAPIResponseMutableLiveData();

    }

    public MutableLiveData<UserAPIResponse> getUserAPIResponse() {
        return userAPIResponse;
    }

    public MutableLiveData<BaseAPIResponse> getBaseAPIResponse() {return baseAPIResponse;}

    public MutableLiveData<CheckAvailableAPIResponse> getUsernameIsUniqueResponse() {
        return usernameIsUniqueResponse;
    }

    public MutableLiveData<CheckAvailableAPIResponse> getEmailIsUniqueueResponse() {
        return emailIsUniqueueResponse;
    }

    public void getUserRequest(String userID){
        apiRepository.getUserProfile(userID);
    }

    public void saveUserRequest(Context context, User user){
        if (UserInfo.getIsLoggedIn(context)){
            apiRepository.saveUser( "Bearer " + UserInfo.getToken(context),user);
        } else {
            Toast.makeText(context, "you need to be logged in to save your profile", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUsernameIsUniqueue(String usename){
        apiRepository.checkUsernameUnique(usename);
    }

    public void getEmailIsUniqueue(String email){
        apiRepository.checkEmailUnique(email);
    }




}
