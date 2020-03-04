package com.example.reconstructv2.Fragments.CreateListing.CreateListingMain;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ImageIDAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.ListingIDAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

import java.util.List;

public class CreateListingViewModel extends AndroidViewModel {

    private APIRepository apiRepository;

    private MutableLiveData<BaseAPIResponse> baseAPIResponse;
    private MutableLiveData<ListingIDAPIResponse> listingIDAPIResponse;
    private MutableLiveData<ImageIDAPIResponse> imageIDAPIResponse;

    public CreateListingViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);
        baseAPIResponse = apiRepository.getBaseAPIResponseMutableLiveData();
        listingIDAPIResponse = apiRepository.getListingIDAPIResponseMutableLiveData();
        imageIDAPIResponse = apiRepository.getImageIDAPIResponseMutableLiveData();

    }

    // getters
    public MutableLiveData<ListingIDAPIResponse> getListingIDAPIResponse() {
        return listingIDAPIResponse;
    }

    public MutableLiveData<ImageIDAPIResponse> getImageIDAPIResponse() {
        return imageIDAPIResponse;
    }

    public MutableLiveData<BaseAPIResponse> getBaseAPIResponse(){
        return baseAPIResponse;
    }


    // requests to server
    public void sendCreateListingRequest(ListingFull listing) {
        apiRepository.createListing(UserInfo.getAuthenticationHeader(getApplication()),listing);
    }

    public void sendUploadImageRequest(Uri imageUri){
        apiRepository.saveImageOnServer(UserInfo.getAuthenticationHeader(getApplication()),imageUri);
    }

    public void sendDeleteImageRequest(List<String> imageID){
        apiRepository.deleteImage(UserInfo.getAuthenticationHeader(getApplication()), imageID);

    }


}
