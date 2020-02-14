package com.example.reconstructv2.Fragments.CreateListing;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.CreateListingMainViewModel;
import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;


public class CreateListingFinish extends Fragment {

    private ListingFull listingArg;
    private FloatingActionButton floatingActionButton;

    private CreateListingMainViewModel viewModel;

    public CreateListingFinish() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get the listing argument from the navigation action
        listingArg = CreateListingFinishArgs.fromBundle(Objects.requireNonNull(getArguments())).getListingArg();

        // initialise the view model
        viewModel = new ViewModelProvider(this).get(CreateListingMainViewModel.class);

        // get the view of the floating action button
        floatingActionButton = getView().findViewById(R.id.finishCreateListingFAB);

        // set an onClickListener on the floating action button
        floatingActionButton.setOnClickListener(v -> {

            // create an alert to make sure the user wants to upload the listing
            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

            alert.setMessage("would you like to upload the listing?")
                    .setCancelable(true);

            alert.setNegativeButton("Cancel",
                    (dialog, which) -> dialog.cancel()
            ).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

            alert.setPositiveButton("Upload", (dialog, which) -> {
                // upload the listing
                viewModel.createListingRequest(listingArg);
                viewModel.getListingIDAPIResponse().observe(getViewLifecycleOwner(), listingIDAPIResponse -> {

                    // navigate to the results framgment with the results
                    if (listingIDAPIResponse.getIsSuccesfull()){
                        MainNavGraphDirections.ActionGlobalSingleListingFragment action = MainNavGraphDirections.actionGlobalSingleListingFragment(null,listingIDAPIResponse.getListingID());
                        Navigation.findNavController(getView()).navigate(action);
                    } else {
                        MainNavGraphDirections.ActionGlobalResultsFragment action = MainNavGraphDirections.actionGlobalResultsFragment(R.id.createListingFragment);
                        action.setIsSuccess(false);
                        action.setMessage(listingIDAPIResponse.getMessage());
                        Navigation.findNavController(getView()).navigate(action);
                    }


                });

            }).setPositiveButtonIcon(getResources()
                    .getDrawable(R.drawable.ic_check_white_24dp));

            alert.show();
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_listing_finish, container, false);
    }

}
