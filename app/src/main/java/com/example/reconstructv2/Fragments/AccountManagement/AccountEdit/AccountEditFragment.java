package com.example.reconstructv2.Fragments.AccountManagement.AccountEdit;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragment;
import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.R;


public class AccountEditFragment extends Fragment {

    private User userAccount;

    private AccountEditFragment.OnFragmentInteractionListener mListener;
    private AccountEditViewModel accountEditViewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayout;

    private EditText editTextUsername;
    private ImageView usernameStatusIcon;
    private Boolean usernameIsUnique;

    private EditText editTextEmail;
    private ImageView emailStatusIcon;
    private Boolean emailIsUnique;


    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPhoneNumber;

    private TextView textEditChangePassword;
    private Button buttonSaveUser;

    public AccountEditFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_edit, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();


        userAccount = null;
        usernameIsUnique = null;
        emailIsUnique = null;

        initViews(view);
        initViewModel();
        setOnClickListeners();
        setLiveDataObservers();
        setRefreshListener();
        setOnTextChangedListeners();

        refreshData();
    }

    // update the user account in the fragment
    public void setUserAccount(User userAccount) {

        this.userAccount = userAccount;
        displayUser(userAccount);
    }

    // get references to all xml objects in the layout
    private void initViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.accountEditRefreshLayout);
        constraintLayout = view.findViewById(R.id.accountEditContainer);

        editTextUsername = view.findViewById(R.id.editTextAccEditUsername);
        usernameStatusIcon = view.findViewById(R.id.accEditIconUsernameStatus);
        usernameStatusIcon.setImageResource(R.drawable.ic_check_white_24dp);

        editTextEmail = view.findViewById(R.id.editTextAccEditEmail);
        emailStatusIcon = view.findViewById(R.id.accEditIcoEmailStatus);
        emailStatusIcon.setImageResource(R.drawable.ic_check_white_24dp);

        editTextFirstName = view.findViewById(R.id.editTextAccEditFirstName);
        editTextLastName = view.findViewById(R.id.editTextAccEditLastName);
        editTextPhoneNumber = view.findViewById(R.id.editTextAccEditPhoneNumber);
        textEditChangePassword = view.findViewById(R.id.changePasswordAccountEditTextView);
        buttonSaveUser = view.findViewById(R.id.textSaveUserCredentials);

    }

    // get a new view model instance
    private void initViewModel() {
        accountEditViewModel = new ViewModelProvider(this).get(AccountEditViewModel.class);
    }

    // set on click listeners
    private void setOnClickListeners() {

        buttonSaveUser.setOnClickListener(v -> {

            // check if the user is trying to save the changes before an account is fetched from the server
            if (userAccount == null) {
                Toast.makeText(getContext(), "Please wait for you account to be fetched from the server", Toast.LENGTH_LONG).show();


            } else if (UserInfo.getIsLoggedIn(getContext())) {

                // check if the data entered in the fields is valid
                if (validateAll()) {

                    // check if the user has entered a unique username and email
                    // that have been verified by the server

                    if (usernameIsUnique == null) {

                        accountEditViewModel.sendGetUsernameIsUniqueRequest(editTextUsername.getText().toString());
                        usernameStatusIcon.setImageResource(R.drawable.ic_refresh_white_24dp);
                        // set text view to refresh
                    }

                    if (emailIsUnique == null) {

                        accountEditViewModel.sendGetEmailIsUniqueRequest(editTextEmail.getText().toString());
                        emailStatusIcon.setImageResource(R.drawable.ic_refresh_white_24dp);
                        // set text view to refresh
                    }


                    // if both are unique
                    if (usernameIsUnique == true && emailIsUnique == true) {

                        // extract values from fields
                        String username = editTextUsername.getText().toString();
                        String first_name = editTextFirstName.getText().toString();
                        String last_name = editTextLastName.getText().toString();
                        String email = editTextEmail.getText().toString();
                        String phoneString = editTextPhoneNumber.getText().toString().trim();
                        Integer phone = Integer.parseInt(phoneString);

                        // create a new user object and send a request to save it
                        User userToSave = new User(username, null, first_name, last_name, email, phone);
                        accountEditViewModel.sendSaveUserRequest(getContext(), userToSave);
                    }

                } else {
                    Toast.makeText(getContext(), "you need to be logged in to save your profile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textEditChangePassword.setOnClickListener(v -> {
            Toast.makeText(getContext(), "navigate to change password", Toast.LENGTH_SHORT).show();
            // navigate to change password fragment
        });

        // if the user clicks on empty space in the layout
        constraintLayout.setOnClickListener(v -> {

            // hide the keyboard
            KeyboardHelper.hideSoftKeyboard(getActivity());
        });

    }

    private void setRefreshListener() {

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(true);
            refreshData();
        });
    }

    // handle server responses
    private void setLiveDataObservers() {

        accountEditViewModel.getBaseAPIResponseMutableLiveData().observe(getViewLifecycleOwner(), response -> {
            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);

        });

        accountEditViewModel.getUserAPIResponseMutableLiveData().observe(getViewLifecycleOwner(), response -> {

            if (response.getIsSuccesfull()) {
                setUserAccount(response.getUserProfile());
            }

            swipeRefreshLayout.setRefreshing(false);
        });

        accountEditViewModel.getUsernameIsUniqueResponseMutableLiveData().observe(getViewLifecycleOwner(), response -> {

            if (response.getIsSuccesfull()) {

                // if the username is unused
                if (response.getIs_unused()) {
                    usernameStatusIcon.setImageResource(R.drawable.ic_check_white_24dp);
                    usernameIsUnique = true;
                } else {
                    editTextUsername.setError("This Username is Already in Use");
                    usernameStatusIcon.setImageResource(R.drawable.ic_cross_red_24dp);
                    usernameIsUnique = false;
                }
            } else {
                usernameStatusIcon.setImageResource(R.drawable.ic_cross_red_24dp);
                usernameIsUnique = false;
            }


        });

        accountEditViewModel.getEmailIsUniqueResponseMutableLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response.getIsSuccesfull()) {

                // if the email is unused
                if (response.getIs_unused()) {
                    emailStatusIcon.setImageResource(R.drawable.ic_check_white_24dp);
                    emailIsUnique = true;
                } else {
                    editTextEmail.setError("This Email is Already in Use");
                    emailStatusIcon.setImageResource(R.drawable.ic_cross_red_24dp);
                    emailIsUnique = false;
                }
            } else {
                emailStatusIcon.setImageResource(R.drawable.ic_cross_red_24dp);
                emailIsUnique = false;
            }
        });
    }

    // refresh user data
    private void refreshData() {

        if (UserInfo.getIsLoggedIn(getContext())) {

            swipeRefreshLayout.setRefreshing(true);
            accountEditViewModel.sendGetUserRequest(UserInfo.getSelfUserID(getContext()));

        } else {
            Toast.makeText(getContext(), "Please log In", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUser(User user) {

        // show user data in fields in layout
        editTextUsername.setText(user.getUsername());
        editTextFirstName.setText(user.getFirst_name());
        editTextLastName.setText(user.getLast_name());
        editTextEmail.setText(user.getEmail());
        editTextPhoneNumber.setText(String.valueOf(user.getPhone()));
    }

    private void setOnTextChangedListeners() {
        // listen for text changing in text edits

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // check if the user is trying to enter text before their account is fetched
                if (userAccount == null) {
                    Toast.makeText(getContext(), "Please wait for your account to be fetched from the server", Toast.LENGTH_SHORT).show();

                } else if (userAccount.getUsername().equals(s.toString())) {
                    // if the input text matches the users username
                    // allow the user to update their data
                    usernameIsUnique = true;

                } else {
                    // if the text entered doesent match the users username
                    // invalidate the is unique check and validate the input text
                    usernameIsUnique = null;
                    InputValidator.validateUsername(editTextUsername);
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

                // check if the user is trying to enter text before their account is fetched
                if (userAccount == null) {
                    Toast.makeText(getContext(), "Please wait for your account to be fetched from the server", Toast.LENGTH_SHORT).show();

                } else if (userAccount.getEmail().equals(s.toString())) {
                    // if the input text matches the users email
                    // allow the user to update their data
                    emailIsUnique = true;

                } else {
                    // if the text entered doesent match the users email
                    // invalidate the is unique check and validate the input text
                    emailIsUnique = null;
                    InputValidator.validateUsername(editTextEmail);
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
                // if the text changes validate the text
                InputValidator.validateName(editTextFirstName, "first name");
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
                // if the text changes validate the text
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
                // if the text changes validate the text
                InputValidator.validatePhone(editTextPhoneNumber, "phone number");
            }
        });


    }

    // check if all data is valid
    private boolean validateAll() {
        return (InputValidator.validateUsername(editTextUsername)
                &&
                InputValidator.validateEmail(editTextEmail)
                &&
                InputValidator.validateName(editTextFirstName, "first name")
                &&
                InputValidator.validateName(editTextLastName, "last name")
                &&
                InputValidator.validatePhone(editTextPhoneNumber, "phone number")
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SingleListingFragment.OnFragmentInteractionListener) {
            mListener = (AccountEditFragment.OnFragmentInteractionListener) context;
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
