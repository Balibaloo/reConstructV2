package com.example.reconstructv2.Fragments.LogIn;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reconstructv2.Helpers.AuthenticationHelper;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.R;


public class LogInFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayout;
    private EditText usernameTextEdit;
    private EditText passwordTextEdit;
    private TextView forgotPassTextView;
    private Button logInButton;
    private Button logInSkipButton;
    private Button createAccountButton;

    private OnFragmentInteractionListener mListener;
    private LogInViewModel logInViewModel;

    public LogInFragment() {
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
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view = getView();



        initViews(view);
        initViewModel();
        setOnClickListeners();
        setLiveDataObservers();


    }

    private void initViews(View view){
        swipeRefreshLayout = view.findViewById(R.id.logInRefreshLayout);
        constraintLayout = view.findViewById(R.id.logInContainer);
        usernameTextEdit = view.findViewById(R.id.EditTextLogInUsername);
        passwordTextEdit = view.findViewById(R.id.EditTextLogInPassword);
        forgotPassTextView = view.findViewById(R.id.forgotPasswordTextView);
        logInButton = view.findViewById(R.id.logInButton);
        logInSkipButton = view.findViewById(R.id.skip_logIn_Button);
        createAccountButton = view.findViewById(R.id.create_account_Button);
    }

    private void initViewModel(){
        logInViewModel = new ViewModelProvider(this).get(LogInViewModel.class);
    }

    private void setOnClickListeners(){

        logInButton.setOnClickListener(v -> {
            // hash the password with the username then send a request to the server

            String username = usernameTextEdit.getText().toString();
            String password = passwordTextEdit.getText().toString();
            String saltedHashedPassword = AuthenticationHelper.hashAndSalt(getString(R.string.master_salt),username,password);

            sendLogInRequest(username,saltedHashedPassword);
        });

        // hide the keyboard if the user taps outside the entry fields
        constraintLayout.setOnClickListener(v -> KeyboardHelper.hideSoftKeyboard(getActivity()));

        // navigate to home fragment
        logInSkipButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.homeFragment2));

        // navigate to create an account
        createAccountButton.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.createUserFragment));

        KeyboardHelper.submitOnEnterKey(passwordTextEdit,logInButton);

    }

    private void sendLogInRequest(String username, String saltedHashedPassword){
        swipeRefreshLayout.setRefreshing(true);

        logInViewModel.sendLogInRequest(username,saltedHashedPassword);

        // hide keyboard
        KeyboardHelper.hideSoftKeyboard(getActivity());
    }

    private void setLiveDataObservers(){

        logInViewModel.getUserTokenAPIResponseMutableLiveData().observe(getViewLifecycleOwner(), response -> {
            swipeRefreshLayout.setRefreshing(false);


            if (response.getIsSuccesfull()) {
                // if the response is sucessfull
                // save user data to shared preferences
                // navigate to the results fragment

                UserInfo.setIsLoggedIn(getContext(),true);
                UserInfo.setToken(getContext(),response.getUserToken());
                UserInfo.setSelfUserID(getContext(),response.getUserID());
                UserInfo.setUsername(getContext(),response.getUsenrname());

                MainNavGraphDirections.ActionGlobalResultsFragment action = MainNavGraphDirections.actionGlobalResultsFragment();
                action.setIsSuccess(true);
                action.setMessage(response.getMessage());

                Navigation.findNavController(getView()).navigate(action);

            } else {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
