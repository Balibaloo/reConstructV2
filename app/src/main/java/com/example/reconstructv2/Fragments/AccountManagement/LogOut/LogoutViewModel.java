package com.example.reconstructv2.Fragments.AccountManagement.LogOut;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;


public class LogoutViewModel extends AndroidViewModel {
    private Application mApplication;

    private APIRepository apiRepository;
    private MutableLiveData<BaseAPIResponse> baseAPIResponseMutableLiveData;

    public LogoutViewModel(@NonNull Application application) {
        super(application);

        mApplication = application;

        apiRepository = new APIRepository(application);

        baseAPIResponseMutableLiveData = apiRepository.getBaseAPIResponseMutableLiveData();

    }

    // getter for Mutable Live Data
    public MutableLiveData<BaseAPIResponse> getBaseAPIResponseMutableLiveData() {
        return baseAPIResponseMutableLiveData;
    }

    // function to send a request to the server

    public void sendLogOutRequest() {
        apiRepository.logOut(UserInfo.getAuthenticationHeader(mApplication));
    }
}
