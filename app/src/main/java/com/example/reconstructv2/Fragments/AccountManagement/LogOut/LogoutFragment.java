package com.example.reconstructv2.Fragments.AccountManagement.LogOut;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.R;


public class LogoutFragment extends Fragment {

    Button logOutButton;
    Button cancelButton;

    LogoutViewModel viewModel;

    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_out, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        initViewModel();
        initViews(view);
        setOnClickListeners();
        setLiveDataObservers();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LogoutViewModel.class);

    }

    private void initViews(View view) {
        logOutButton = view.findViewById(R.id.logoutConfirmButton);
        cancelButton = view.findViewById(R.id.logoutCancelButton);
    }

    private void setOnClickListeners() {

        logOutButton.setOnClickListener(v -> viewModel.sendLogOutRequest());

        // go back
        cancelButton.setOnClickListener(v -> Navigation.findNavController(getView()).navigateUp());
    }

    private void setLiveDataObservers() {

        viewModel.getBaseAPIResponseMutableLiveData().observe(getViewLifecycleOwner(), baseAPIResponse -> {

            // create a new action to the results fragment
            MainNavGraphDirections.ActionGlobalResultsFragment action = MainNavGraphDirections.actionGlobalResultsFragment();
            action.setIsSuccess(baseAPIResponse.getIsSuccesfull());
            action.setMessage(baseAPIResponse.getMessage());

            if (baseAPIResponse.getIsSuccesfull()) {

                // log out localy
                UserInfo.logOut(getContext());


            } else {
                action.setRetryDestination(R.id.logoutFragment);
            }

            // navigate
            Navigation.findNavController(getView()).navigate(action);

        });
    }
}