package com.example.reconstructv2.Fragments.LogIn;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class LogInViewModel extends AndroidViewModel {
    private APIRepository apiRepository;

    private MutableLiveData<UserTokenAPIResponse> userTokenAPIResponseMutableLiveData;

    public LogInViewModel(@NonNull Application application){
        super(application);

        apiRepository = new APIRepository(application);

        userTokenAPIResponseMutableLiveData = apiRepository.getUserTokenAPIResponseMutableLiveData();
    }


    public MutableLiveData<UserTokenAPIResponse> getUserTokenAPIResponseMutableLiveData() {
        return userTokenAPIResponseMutableLiveData;
    }


    public void sendLogInRequest(String username, String saltedHashedPassword){
        apiRepository.login(username,saltedHashedPassword);
    }
}
