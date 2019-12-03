package com.example.reconstructv2.Fragments.AccountView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class AccountViewModel extends AndroidViewModel {

    private APIRepository apiRepository;
    private MutableLiveData<UserAPIResponse> userAPIResponse;

    public AccountViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);
        userAPIResponse = apiRepository.getUserAPIResponseMutableLiveData();

    }

    public MutableLiveData<UserAPIResponse> getUserAPIResponse() {
        return userAPIResponse;
    }

    public void getUserRequest(String userID){
        apiRepository.getUserProfile(userID);
    }

}
