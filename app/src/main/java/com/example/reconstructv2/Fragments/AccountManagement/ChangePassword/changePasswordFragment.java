package com.example.reconstructv2.Fragments.AccountManagement.ChangePassword;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.R;

public class changePasswordFragment extends Fragment {

    private SwipeRefreshLayout loadingView;

    private EditText passwordTextEdit;
    private EditText passwordSecondTextEdit;
    private Button submitButton;

    private ChangePasswordViewModel viewModel;

    public changePasswordFragment() {
        // Required empty public constructor
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

    // get references for the xml layout objects
    private void initViews(View view) {

        loadingView = view.findViewById(R.id.changePassword_loading_RefreshLayout);
        loadingView.setVisibility(View.INVISIBLE);

        passwordTextEdit = view.findViewById(R.id.changePassword_password_EditText);
        passwordSecondTextEdit = view.findViewById(R.id.changePassword_passwordConfirm_EditText);
        submitButton = view.findViewById(R.id.changePassword_submit_Button);

    }


    public void setOnClickListeners() {

        submitButton.setOnClickListener(v -> {
            if (allDataValid()) {
                if (UserInfo.getIsLoggedIn(getActivity())) {

                    viewModel.sendChangePasswordRequest(passwordTextEdit.getText().toString());

                    // display the loading view
                    loadingView.setVisibility(View.VISIBLE);
                    loadingView.setRefreshing(true);

                } else {
                    Toast.makeText(getContext(), "You need to be logged in to change your password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void setLiveDataObservers() {

        viewModel.getBaseAPIResponseMutableLiveData().observe(getViewLifecycleOwner(), baseAPIResponse -> {

            // create an action to navigate to the results fragment
            MainNavGraphDirections.ActionGlobalResultsFragment action = MainNavGraphDirections.actionGlobalResultsFragment();

            loadingView.setVisibility(View.INVISIBLE);
            loadingView.setRefreshing(false);

            // set action parameters
            if (baseAPIResponse.getIsSuccesfull()) {
                action.setIsSuccess(true);
                action.setMessage(baseAPIResponse.getMessage());

            } else {
                action.setRetryDestination(R.id.changePasswordFragment);
                action.setIsSuccess(false);
                action.setMessage(baseAPIResponse.getMessage());
            }

            // navigate
            Navigation.findNavController(getView()).navigate(action);
        });
    }


    // check if all data is valid
    private boolean allDataValid() {

        return InputValidator.validatePassword(passwordTextEdit, "")
                &&
                InputValidator.validatePasswordsMatch(passwordTextEdit, passwordSecondTextEdit);
    }


    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

}
