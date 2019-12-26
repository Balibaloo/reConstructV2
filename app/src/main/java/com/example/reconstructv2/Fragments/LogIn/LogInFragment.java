package com.example.reconstructv2.Fragments.LogIn;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



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

        logInViewModel = ViewModelProviders.of(this).get(LogInViewModel.class);

        initViewObjects(view);
        initOnClickListeners(view);
        setLiveDataObservers();


    }

    private void initViewObjects(View view){
        swipeRefreshLayout = view.findViewById(R.id.logInRefreshLayout);
        constraintLayout = view.findViewById(R.id.logInContainer);
        usernameTextEdit = view.findViewById(R.id.EditTextLogInUsername);
        passwordTextEdit = view.findViewById(R.id.EditTextLogInPassword);
        forgotPassTextView = view.findViewById(R.id.forgotPasswordTextView);
        logInButton = view.findViewById(R.id.logInButton);
        logInSkipButton = view.findViewById(R.id.skip_logIn_Button);
        createAccountButton = view.findViewById(R.id.create_account_Button);
    }

    private void initOnClickListeners(View  view){
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();
                String saltedHashedPassword = AuthenticationHelper.hashAndSalt(getString(R.string.master_salt),username,password);

                logIn(username,saltedHashedPassword);


            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardHelper.hideSoftKeyboard(getActivity());
            }
        });

        logInSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.homeFragment2);
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.createUserFragment);
            }
        });


        passwordTextEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Boolean handled = false;
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    // call on click listener
                    logInButton.callOnClick();
                    handled = true;
                }
                return handled;

            }
        });
    }



    private void logIn(String username, String saltedHashedPassword){
        swipeRefreshLayout.setRefreshing(true);
        logInViewModel.fetchLogInUser(username,saltedHashedPassword);
        KeyboardHelper.hideSoftKeyboard(getActivity());

    }

    private void setLiveDataObservers(){
        logInViewModel.getUserTokenAPIResponseMutableLiveData().observe(this, new Observer<UserTokenAPIResponse>() {
            @Override
            public void onChanged(UserTokenAPIResponse userTokenAPIResponse) {
                swipeRefreshLayout.setRefreshing(false);
                UserInfo.setIsLoggedIn(getContext(),true);
                UserInfo.setToken(getContext(),userTokenAPIResponse.getUserToken());
                UserInfo.setSelfUserID(getContext(),userTokenAPIResponse.getUserID());
                Navigation.findNavController(getView()).navigate(R.id.homeFragment2);

                Toast.makeText(getContext(), userTokenAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
