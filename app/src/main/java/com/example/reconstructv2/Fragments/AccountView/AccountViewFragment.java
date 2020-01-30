package com.example.reconstructv2.Fragments.AccountView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragment;
import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.R;


public class AccountViewFragment extends Fragment {

    private User userAccount;

    private AccountViewFragment.OnFragmentInteractionListener mListener;
    private AccountViewModel accountViewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayout;

    private EditText editTextUsername;
    private ImageView usernameStatusIco;
    private Boolean usernameIsUniqueue;

    private EditText editTextEmail;
    private ImageView emailStatusIco;
    private Boolean emailIsUniqueue;


    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPhoneNumber;

    private TextView textEditChangePassword;
    private Button buttonSaveUser;

    public AccountViewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_view,container,false);
    }


    @Override
    public  void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        userAccount = null;
        usernameIsUniqueue = null;
        emailIsUniqueue = null;

        initViews(view);
        initViewModel();
        setOnClickListeners();
        setLiveDataObservers();
        setRefreshListener();
        setOnTextChangedListeners();

        refreshData();
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
        displayUser(userAccount);
    }

    private void initViews(View view){
        swipeRefreshLayout = view.findViewById(R.id.accountViewRefreshLayout);
        constraintLayout = view.findViewById(R.id.accountViewContainer);

        editTextUsername = view.findViewById(R.id.editTextAccViewUsername);
        usernameStatusIco = view.findViewById(R.id.accViewIconUsernameStatus);
        usernameStatusIco.setImageResource(R.drawable.ic_check_white_24dp);

        editTextEmail = view.findViewById(R.id.editTextAccViewEmail);
        emailStatusIco = view.findViewById(R.id.accViewIcoEmailStatus);
        emailStatusIco.setImageResource(R.drawable.ic_check_white_24dp);

        editTextFirstName = view.findViewById(R.id.editTextAccViewFirstName);
        editTextLastName = view.findViewById(R.id.editTextAccViewLastName);
        editTextPhoneNumber = view.findViewById(R.id.editTextAccViewPhoneNumber);
        textEditChangePassword = view.findViewById(R.id.changePasswordAccountViewTextView);
        buttonSaveUser = view.findViewById(R.id.textSaveUserCredentials);
    }

    private void initViewModel(){
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
    }

    private void setOnClickListeners(){
        buttonSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAccount == null) {
                    Toast.makeText(getContext(), "Please wait for you account to be fetched from the server", Toast.LENGTH_LONG).show();

                } else if (UserInfo.getIsLoggedIn(getContext())) {

                    if (validateAll()) {

                        if (usernameIsUniqueue == null) {
                            System.out.println("checking username uniqueue");
                            accountViewModel.getUsernameIsUniqueue(editTextUsername.getText().toString());
                            usernameStatusIco.setImageResource(R.drawable.ic_refresh_white_24dp);
                            // set text view to refresh
                        }

                        if (emailIsUniqueue == null) {
                            System.out.println("checking email uniqueue");
                            accountViewModel.getEmailIsUniqueue(editTextEmail.getText().toString());
                            emailStatusIco.setImageResource(R.drawable.ic_refresh_white_24dp);
                            // set text view to refresh
                        }

                        if (usernameIsUniqueue != null && emailIsUniqueue != null) {
                            if (emailIsUniqueue && usernameIsUniqueue) {
                                String username = editTextUsername.getText().toString();
                                String first_name = editTextFirstName.getText().toString();
                                String last_name = editTextLastName.getText().toString();
                                String email = editTextEmail.getText().toString();
                                String phoneString = editTextPhoneNumber.getText().toString().trim();
                                Integer phone = Integer.parseInt(phoneString);

                                User userToSave = new User(username, null, first_name, last_name, email, phone);
                                accountViewModel.saveUserRequest(getContext(), userToSave);

                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "you need to be logged in to save your profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textEditChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "navigate to change password", Toast.LENGTH_SHORT).show();
                // navigate to change password fragment
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHelper.hideSoftKeyboard(getActivity());
                System.out.println("clicked on constraint");
            }
        });

        swipeRefreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked on refresh");
            }
        });
    }

    private void setRefreshListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                refreshData();
            }
        });
    }

    private void setLiveDataObservers(){
        accountViewModel.getBaseAPIResponse().observe(this, new Observer<BaseAPIResponse>() {
            @Override
            public void onChanged(BaseAPIResponse response) {
                if (response.getIsSuccesfull()) {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }

                swipeRefreshLayout.setRefreshing(false);

            }
        });

        accountViewModel.getUserAPIResponse().observe(this, new Observer<UserAPIResponse>() {
            @Override
            public void onChanged(UserAPIResponse response) {
                if (response.getIsSuccesfull()) {
                    setUserAccount(response.getUserProfile());
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        accountViewModel.getUsernameIsUniqueResponse().observe(this, new Observer<CheckAvailableAPIResponse>() {
            @Override
            public void onChanged(CheckAvailableAPIResponse response) {
                if (response.getIsSuccesfull()) {
                    if (response.getIs_unused()) {
                        usernameStatusIco.setImageResource(R.drawable.ic_check_white_24dp);
                        usernameIsUniqueue = true;
                    } else {
                        editTextUsername.setError("This Username is Already in Use");
                        usernameStatusIco.setImageResource(R.drawable.ic_cross_red_24dp);
                        usernameIsUniqueue = false;
                    }
                }
            }

        });

        accountViewModel.getEmailIsUniqueueResponse().observe(this, new Observer<CheckAvailableAPIResponse>() {
            @Override
            public void onChanged(CheckAvailableAPIResponse response) {
                if (response.getIsSuccesfull()) {

                    if (response.getIs_unused()) {
                        emailStatusIco.setImageResource(R.drawable.ic_check_white_24dp);
                        emailIsUniqueue = true;
                    } else {
                        editTextEmail.setError("This Email is Already in Use");
                        emailStatusIco.setImageResource(R.drawable.ic_cross_red_24dp);
                        emailIsUniqueue = false;
                }}
            }
        });
    }

    private void refreshData(){
        if (UserInfo.getIsLoggedIn(getContext())){
            swipeRefreshLayout.setRefreshing(true);
            accountViewModel.getUserRequest(UserInfo.getSelfUserID(getContext()));

        } else {
            Toast.makeText(getContext(), "Please log In", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUser(User user){
        editTextUsername.setText(user.getUsername());
        editTextFirstName.setText(user.getFirst_name());
        editTextLastName.setText(user.getLast_name());
        editTextEmail.setText(user.getEmail());
        editTextPhoneNumber.setText(user.getPhone().toString());
    }

    private void setOnTextChangedListeners(){

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (userAccount == null){

                } else if (!userAccount.getUsername().equals(s.toString())) {
                    usernameIsUniqueue = null;
                    InputValidator.validateUsername(editTextUsername);
                } else {
                    usernameIsUniqueue = true;
                }

            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (userAccount == null){

                } else if (!userAccount.getEmail().equals(s.toString())) {
                    emailIsUniqueue = null;
                    InputValidator.validateUsername(editTextEmail);
                } else {
                    emailIsUniqueue = true;
                }
            }
        });

        editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputValidator.validateName(editTextFirstName,"first name");
            }
        });

        editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputValidator.validateName(editTextLastName, "last name");
            }
        });

        editTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputValidator.validatePhone(editTextPhoneNumber, "phone number");
            }
        });




    }

    private boolean validateAll(){
        return (InputValidator.validateUsername(editTextUsername)
                &&
                InputValidator.validateEmail(editTextEmail)
                &&
                InputValidator.validateName(editTextFirstName,"first name")
                &&
                InputValidator.validateName(editTextLastName,"last name")
                &&
                InputValidator.validatePhone(editTextPhoneNumber,"phone number")
                );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SingleListingFragment.OnFragmentInteractionListener) {
            mListener = (AccountViewFragment.OnFragmentInteractionListener) context;
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
