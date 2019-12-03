package com.example.reconstructv2.Fragments.CreateUser;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class FinishCreateUserViewModel extends AndroidViewModel {

    private APIRepository apiRepository;

    private MutableLiveData<UserTokenAPIResponse> userTokenAPIResponse;
    private MutableLiveData<BaseAPIResponse> baseAPIResponse;

    public FinishCreateUserViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);

        userTokenAPIResponse = apiRepository.getUserTokenAPIResponseMutableLiveData();
        baseAPIResponse = apiRepository.getBaseAPIResponseMutableLiveData();

    }

    public MutableLiveData<UserTokenAPIResponse> getUserTokenAPIResponse() {
        return userTokenAPIResponse;
    }

    public void createUserRequest(User user) {
        apiRepository.createAccount(user);
    }

    public MutableLiveData<BaseAPIResponse> getBaseAPIResponse(){
        return baseAPIResponse;
    }

    public void testConnectionRequest() {apiRepository.testConnectionNoAuth();}
}
