package com.example.reconstructv2.Fragments.AccountManagement.ChangePassword;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.AuthenticationHelper;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.R;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class ChangePasswordViewModel extends AndroidViewModel {
    private Application mApplication;

    private APIRepository apiRepository;
    private MutableLiveData<BaseAPIResponse> baseAPIResponseMutableLiveData;

    public ChangePasswordViewModel(@NonNull Application application) {
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

    public void sendChangePasswordRequest(String password){
        String hashedPassword = AuthenticationHelper.hashAndSalt(mApplication.getString(R.string.master_salt),password,UserInfo.getUsername(mApplication));
        apiRepository.changePassword(UserInfo.getAuthenticationHeader(getApplication()),hashedPassword);
    }
}
