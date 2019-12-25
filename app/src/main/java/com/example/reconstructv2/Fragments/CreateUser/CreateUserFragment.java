package com.example.reconstructv2.Fragments.CreateUser;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Models.ApiResponses.CheckAvailableAPIResponse;
import com.example.reconstructv2.R;


public class CreateUserFragment extends Fragment {

    private CreateUserViewModel viewModel;

    private EditText usernameEditText;
    private ImageView usernameStatusIco;
    private Boolean usernameIsUniqueue;

    private EditText emailEditText;
    private ImageView emailStatusIco;
    private Boolean emailIsUniqueue;

    private EditText passwordEditText;
    private EditText passwordAgainEditText;



    private Button nextButton;

    private OnFragmentInteractionListener mListener;


    public CreateUserFragment() {
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
        return inflater.inflate(R.layout.fragment_create_user, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view = getView();

        usernameIsUniqueue = null;
        emailIsUniqueue = null;

        initViews(view);
        initViewModel();
        setOnClickListeners();
        setLiveDataObservers();
        setOnTextChangedListeners();

    }

    private boolean validateAll(){
        return (
                InputValidator.validatePasswordsMatch(passwordEditText,passwordAgainEditText)
                &&
                InputValidator.validateUsername(usernameEditText)
                &&
                InputValidator.validatePassword(passwordEditText,usernameEditText.getText().toString().trim())
                &&
                InputValidator.validateEmail(emailEditText));
    }

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(CreateUserViewModel.class);
    }

    private void initViews(View view){



        usernameEditText = view.findViewById(R.id.editTextCreateUsername);
        usernameStatusIco = view.findViewById(R.id.iconUsernameStatus);
        usernameStatusIco.setImageResource(R.drawable.ic_check_white_24dp);

        emailEditText = view.findViewById(R.id.editTextCreateEmail);
        emailStatusIco = view.findViewById(R.id.iconEmailStatus);
        emailStatusIco.setImageResource(R.drawable.ic_check_white_24dp);


        passwordEditText = view.findViewById(R.id.editTextCreatePassword);
        passwordAgainEditText = view.findViewById(R.id.editTextCreatePasswordRepeat);


        nextButton = view.findViewById(R.id.buttonNext);
    }

    private void setOnClickListeners(){
        // on next button pressed navigate to finish create with arguments
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateAll()) {

                    if (usernameIsUniqueue == null) {
                        System.out.println("checking username uniqueue");
                        viewModel.getUsernameUniqueueRequest(usernameEditText.getText().toString());
                        usernameStatusIco.setImageResource(R.drawable.ic_refresh_white_24dp);
                        // set text view to refresh
                    }

                    if (emailIsUniqueue == null) {
                        System.out.println("checking email uniqueue");
                        viewModel.getEmailUniqueue(emailEditText.getText().toString());
                        emailStatusIco.setImageResource(R.drawable.ic_refresh_white_24dp);
                        // set text view to refresh
                    }

                    if ( usernameIsUniqueue != null && emailIsUniqueue != null) {
                        if (emailIsUniqueue && usernameIsUniqueue) {
                            String username = usernameEditText.getText().toString();
                            String password = passwordEditText.getText().toString();
                            String email = emailEditText.getText().toString();

                            CreateUserFragmentDirections.ActionCreateUserFragmentToFinishCreateUserFragment action = CreateUserFragmentDirections.actionCreateUserFragmentToFinishCreateUserFragment(email, username, password);
                            Navigation.findNavController(getView()).navigate(action);
                        }
                    }
                }
            }
        });
    }

    private void setLiveDataObservers(){

        viewModel.getUsernameIsAvailableLiveData().observe(this, new Observer<CheckAvailableAPIResponse>() {
            @Override
            public void onChanged(CheckAvailableAPIResponse checkAvailableAPIResponse) {
                if (checkAvailableAPIResponse.getIs_unused()) {
                    usernameStatusIco.setImageResource(R.drawable.ic_check_white_24dp);
                    usernameIsUniqueue = true;
                } else {
                    usernameEditText.setError("This Username is Already in Use");
                    usernameStatusIco.setImageResource(R.drawable.ic_cross_red_24dp);
                    usernameIsUniqueue = false;
                }
            }
        });


        viewModel.getEmailIsAvailableLiveData().observe(this, new Observer<CheckAvailableAPIResponse>() {
            @Override
            public void onChanged(CheckAvailableAPIResponse checkAvailableAPIResponse) {
                if (checkAvailableAPIResponse.getIs_unused()) {
                    emailStatusIco.setImageResource(R.drawable.ic_check_white_24dp);
                    emailIsUniqueue = true;
                } else {
                    emailEditText.setError("This Email is Already in Use");
                    emailStatusIco.setImageResource(R.drawable.ic_cross_red_24dp);
                    emailIsUniqueue = false;
                }
            }
        });
    }

    private void setOnTextChangedListeners(){
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usernameIsUniqueue = null;
                InputValidator.validateUsername(usernameEditText);
            }
        });

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emailIsUniqueue = null;
                InputValidator.validateEmail(emailEditText);
            }
        });

        //passwordEditText

        //passwordAgainEditText
    };

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
        void onFragmentInteraction(Uri uri);
    }
}
