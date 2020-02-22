package com.example.reconstructv2.Fragments.CreateUser;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class CreateUserViewModel extends AndroidViewModel {
    private APIRepository apiRepository;
    private MutableLiveData<CheckAvailableAPIResponse> usernameIsAvailableLiveData;
    private MutableLiveData<CheckAvailableAPIResponse> emailIsAvailableLiveData;


    public CreateUserViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);
        usernameIsAvailableLiveData = apiRepository.getCheckUsernameAvailableAPIResponseMutableLiveData();
        emailIsAvailableLiveData = apiRepository.getCheckEmailAvailableAPIResponseMutableLiveData();

    }

    public MutableLiveData<CheckAvailableAPIResponse> getUsernameIsAvailableLiveData() {
        return usernameIsAvailableLiveData;
    }

    public MutableLiveData<CheckAvailableAPIResponse> getEmailIsAvailableLiveData() {
        return emailIsAvailableLiveData;
    }


    public void getUsernameUniqueueRequest(String username){
        apiRepository.checkUsernameUnique(username);
    }

    public void getEmailUniqueue(String email){
        apiRepository.checkEmailUnique(email);
    }
}
