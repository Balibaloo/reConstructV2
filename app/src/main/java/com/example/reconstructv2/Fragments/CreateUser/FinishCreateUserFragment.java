package com.example.reconstructv2.Fragments.CreateUser;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reconstructv2.Helpers.AuthenticationHelper;
import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.R;


public class FinishCreateUserFragment extends Fragment {

    private FinishCreateUserViewModel finishCreateUserViewModel;

    private ConstraintLayout constraintLayoutContainer;


    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneNumberEditText;

    private Button testConnectionButton;
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

        phoneNumberEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    // call on click listener
                    submitButton.callOnClick();
                    handled = true;
                }

                return handled;
            }
        });



    }

    private boolean validateAll() {
        return (InputValidator.validateName(firstNameEditText, "first name")
                &&
                InputValidator.validateName(lastNameEditText,"last name")
                &&
                InputValidator.validatePhone(phoneNumberEditText, "phone number"));
    }

    private void initViews(View view){
        constraintLayoutContainer = view.findViewById(R.id.finishCreateUserContainer);

        firstNameEditText = view.findViewById(R.id.editTextFirstName);
        lastNameEditText = view.findViewById(R.id.editTextLastName);
        phoneNumberEditText = view.findViewById(R.id.editTextPhoneNumber);

        submitButton = view.findViewById(R.id.buttonSubmit);
        testConnectionButton = view.findViewById(R.id.buttonTestConnection);
    }

    private void initViewModel(){
        finishCreateUserViewModel = ViewModelProviders.of(this).get(FinishCreateUserViewModel.class);
    }

    private void sendCreateRequest() {

        String username = FinishCreateUserFragmentArgs.fromBundle(getArguments()).getUsername();
        String password = FinishCreateUserFragmentArgs.fromBundle(getArguments()).getPassword();
        String first_name = firstNameEditText.getText().toString();
        String last_name = lastNameEditText.getText().toString();
        String email = FinishCreateUserFragmentArgs.fromBundle(getArguments()).getEmail();
        Integer phone = Integer.parseInt(phoneNumberEditText.getText().toString());

        String saltedHashedPassword = AuthenticationHelper.hashAndSalt(getString(R.string.master_salt),password,username);
        User newUser = new User(username, saltedHashedPassword, first_name, last_name, email, phone);

        finishCreateUserViewModel.createUserRequest(newUser);
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateAll()) {
                    sendCreateRequest();
                } else {
                    Toast.makeText(getContext(), "Please check that the data entered is correct", Toast.LENGTH_SHORT).show();
                }

            }
        });


        constraintLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getActivity());
            }
        });

        testConnectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCreateUserViewModel.testConnectionRequest();
            }
        });
    }

    private void setLiveDataObservers(){
        finishCreateUserViewModel.getUserTokenAPIResponse().observe(this, new Observer<UserTokenAPIResponse>() {
            @Override
            public void onChanged(UserTokenAPIResponse userTokenAPIResponse) {
                // go to results
                // implement on failue flag

                UserInfo.setIsLoggedIn(getContext(),true);
                UserInfo.setToken(getContext(),userTokenAPIResponse.getUserToken());

                MainNavGraphDirections.ActionGlobalResultsFragment action = MainNavGraphDirections.actionGlobalResultsFragment(R.id.createUserFragment);
                action.setIsSuccess(true);
                action.setMessage(userTokenAPIResponse.getMessage());

                Navigation.findNavController(getView()).navigate(action);
            }
        });


        finishCreateUserViewModel.getBaseAPIResponse().observe(this, new Observer<BaseAPIResponse>() {
            @Override
            public void onChanged(BaseAPIResponse baseAPIResponse) {
                Toast.makeText(getContext(), baseAPIResponse.getTestVariable(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
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
