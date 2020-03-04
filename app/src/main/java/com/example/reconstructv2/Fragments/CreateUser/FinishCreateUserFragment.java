package com.example.reconstructv2.Fragments.CreateUser;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reconstructv2.Helpers.AuthenticationHelper;
import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.R;


public class FinishCreateUserFragment extends Fragment {

    private FinishCreateUserViewModel finishCreateUserViewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayoutContainer;


    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneNumberEditText;

    private Button submitButton;

    private OnFragmentInteractionListener mListener;

    public FinishCreateUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_create_user, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view = getView();

        initViews(view);
        initViewModel();
        setOnTextChangedListeners();
        setOnClickListeners();
        setLiveDataObservers();

        KeyboardHelper.submitOnEnterKey(phoneNumberEditText,submitButton);

    }


    // validate all input values
    private boolean validateAll() {
        return (InputValidator.validateName(firstNameEditText, "first name")
                &&
                InputValidator.validateName(lastNameEditText,"last name")
                &&
                InputValidator.validatePhone(phoneNumberEditText, "phone number"));
    }


    private void initViews(View view){
        swipeRefreshLayout = view.findViewById(R.id.finishCreateUserSwipeRefreshLayout);
        constraintLayoutContainer = view.findViewById(R.id.finishCreateUserContainer);

        firstNameEditText = view.findViewById(R.id.editTextFirstName);
        lastNameEditText = view.findViewById(R.id.editTextLastName);
        phoneNumberEditText = view.findViewById(R.id.editTextPhoneNumber);

        submitButton = view.findViewById(R.id.buttonSubmit);
    }

    private void initViewModel(){
        finishCreateUserViewModel = new ViewModelProvider(this).get(FinishCreateUserViewModel.class);
    }


    private void sendCreateRequest() {

        // get values from the passed values
        String username = FinishCreateUserFragmentArgs.fromBundle(getArguments()).getUsername();
        String password = FinishCreateUserFragmentArgs.fromBundle(getArguments()).getPassword();
        String email = FinishCreateUserFragmentArgs.fromBundle(getArguments()).getEmail();

        String first_name = firstNameEditText.getText().toString();
        String last_name = lastNameEditText.getText().toString();
        Integer phone = Integer.parseInt(phoneNumberEditText.getText().toString());

        // salt and hash the password
        String saltedHashedPassword = AuthenticationHelper.hashAndSalt(getString(R.string.master_salt),password,username);

        // create a new user
        User newUser = new User(username, saltedHashedPassword, first_name, last_name, email, phone);

        swipeRefreshLayout.setRefreshing(true);
        finishCreateUserViewModel.sendCreateUserRequest(newUser);
    }

    private void setOnTextChangedListeners(){
        firstNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputValidator.validateName(firstNameEditText,"first name");
            }
        });

        lastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputValidator.validateName(lastNameEditText, "last name");
            }
        });

        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputValidator.validatePhone(phoneNumberEditText,"phone number");
            }
        });
    }

    private void setOnClickListeners(){
        submitButton.setOnClickListener(v -> {
            if (validateAll()) {
                sendCreateRequest();
            } else {
                Toast.makeText(getContext(), "Please check that the data entered is correct", Toast.LENGTH_SHORT).show();
            }

        });


        constraintLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHelper.hideSoftKeyboard(getActivity());
            }
        });

    }


    private void setLiveDataObservers(){

        finishCreateUserViewModel.getUserTokenAPIResponse().observe(getViewLifecycleOwner(), response -> {
            swipeRefreshLayout.setRefreshing(false);

            MainNavGraphDirections.ActionGlobalResultsFragment action = MainNavGraphDirections.actionGlobalResultsFragment();

            action.setRetryDestination(R.id.createUserFragment);
            action.setIsSuccess(response.getIsSuccesfull());
            action.setMessage(response.getMessage());

            if (response.getIsSuccesfull()) {
                UserInfo.setIsLoggedIn(getContext(),true);
                UserInfo.setToken(getContext(),response.getUserToken());
            }

            Navigation.findNavController(getView()).navigate(action);
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
