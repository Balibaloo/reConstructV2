package com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.ItemListEdit;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.ImageIDAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class ItemListEditViewModel extends AndroidViewModel {

    private APIRepository apiRepository;
    private MutableLiveData<ImageIDAPIResponse> imageIDAPIResponse;


    public ItemListEditViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);
        imageIDAPIResponse = apiRepository.getImageIDAPIResponseMutableLiveData();

    }

    // getter

    public MutableLiveData<ImageIDAPIResponse> getImageIDAPIResponse() {
        return imageIDAPIResponse;
    }

    // send requests

    public void sendUploadImageRequest(Uri imageUri){
        apiRepository.saveImageOnServer(UserInfo.getAuthenticationHeader(getApplication()),imageUri);
    }
}
