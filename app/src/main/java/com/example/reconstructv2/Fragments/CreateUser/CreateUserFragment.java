package com.example.reconstructv2.Fragments.CreateUser;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.R;


public class CreateUserFragment extends Fragment {
    private ConstraintLayout container;

    private CreateUserViewModel viewModel;

    private EditText usernameEditText;
    private ImageView usernameStatusIco;
    private Boolean usernameIsUnique;

    private EditText emailEditText;
    private ImageView emailStatusIco;
    private Boolean emailIsUnique;

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

        usernameIsUnique = null;
        emailIsUnique = null;

        initViews(view);
        initViewModel();
        setOnClickListeners();
        setLiveDataObservers();
        setOnTextChangedListeners();

    }

    private boolean allDataValid() {
        return (
                InputValidator.validatePasswordsMatch(passwordEditText, passwordAgainEditText)
                        &&
                        InputValidator.validateUsername(usernameEditText)
                        &&
                        InputValidator.validatePassword(passwordEditText, usernameEditText.getText().toString().trim())
                        &&
                        InputValidator.validateEmail(emailEditText));
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CreateUserViewModel.class);
    }

    private void initViews(View view) {

        container = view.findViewById(R.id.containerCreate);

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

    private void setOnClickListeners() {
        container.setOnClickListener(v -> KeyboardHelper.hideSoftKeyboard(getActivity()));

        // on next button pressed navigate to finish create with arguments
        nextButton.setOnClickListener(v -> {

            if (allDataValid()) {

                // if the username hasnt been verified yet
                if (usernameIsUnique == null) {
                    viewModel.getUsernameUniqueueRequest(usernameEditText.getText().toString());
                    usernameStatusIco.setImageResource(R.drawable.ic_refresh_white_24dp);
                    // set text view to refresh
                }

                // if the email hasnt been verified yet
                if (emailIsUnique == null) {
                    viewModel.getEmailUniqueue(emailEditText.getText().toString());
                    emailStatusIco.setImageResource(R.drawable.ic_refresh_white_24dp);
                    // set text view to refresh
                }

                // if both fields are unique
                // navigate to the next fragment with data
                if (emailIsUnique != null && usernameIsUnique != null)
                if (emailIsUnique && usernameIsUnique) {

                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String email = emailEditText.getText().toString();

                    CreateUserFragmentDirections.ActionCreateUserFragmentToFinishCreateUserFragment action = CreateUserFragmentDirections.actionCreateUserFragmentToFinishCreateUserFragment(email, username, password);
                    Navigation.findNavController(getView()).navigate(action);

                }
            }
        });
    }

    private void setLiveDataObservers() {

        viewModel.getUsernameIsAvailableLiveData().observe(getViewLifecycleOwner(), response -> {

            if (response.getIsSuccesfull()) {

                if (response.getIs_unused()) {
                    usernameStatusIco.setImageResource(R.drawable.ic_check_white_24dp);
                    usernameIsUnique = true;

                } else {
                    usernameEditText.setError("This Username is Already in Use");
                    usernameStatusIco.setImageResource(R.drawable.ic_cross_red_24dp);
                    usernameIsUnique = false;
                }
            } else {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        viewModel.getEmailIsAvailableLiveData().observe(getViewLifecycleOwner(), response -> {

            if (response.getIsSuccesfull()) {
                if (response.getIs_unused()) {
                    emailStatusIco.setImageResource(R.drawable.ic_check_white_24dp);
                    emailIsUnique = true;
                } else {
                    emailEditText.setError("This Email is Already in Use");
                    emailStatusIco.setImageResource(R.drawable.ic_cross_red_24dp);
                    emailIsUnique = false;
                }
            } else {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setOnTextChangedListeners() {

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // if the user changes the value of the field, invalidate the is unique check
                usernameIsUnique = null;
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
                // if the user changes the value of the field, invalidate the is unique check
                emailIsUnique = null;
                InputValidator.validateEmail(emailEditText);
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputValidator.validatePassword(passwordEditText,usernameEditText.getText().toString());
            }
        });

        passwordAgainEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InputValidator.validatePasswordsMatch(passwordEditText,passwordAgainEditText);
            }
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
        void onFragmentInteraction(Uri uri);
    }
}
