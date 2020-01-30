package com.example.reconstructv2.Fragments.CreateListing;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.ImageIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingIDAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class ItemListEditViewModel extends AndroidViewModel {

    private APIRepository apiRepository;
    private MutableLiveData<ImageIDAPIResponse> imageIDAPIResponse;


    public ItemListEditViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);
        imageIDAPIResponse = apiRepository.getImageIDAPIResponseMutableLiveData();

    }


    public MutableLiveData<ImageIDAPIResponse> getImageIDAPIResponse() {
        return imageIDAPIResponse;
    }


    public void uploadImageRequest(Uri imageUri){
        apiRepository.saveImageonServer("Bearer " + UserInfo.getToken(getApplication()),imageUri);
    }
}
