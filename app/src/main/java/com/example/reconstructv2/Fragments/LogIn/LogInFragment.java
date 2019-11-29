package com.example.reconstructv2.Fragments.LogIn;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reconstructv2.Models.ApiResponses.UserTokenAPIResponse;
import com.example.reconstructv2.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LogInFragment extends Fragment {


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

        usernameTextEdit = view.findViewById(R.id.username_Edit_Text);
        passwordTextEdit = view.findViewById(R.id.password_Edit_Text);
        forgotPassTextView = view.findViewById(R.id.forgotPasswordTextView);
        logInButton = view.findViewById(R.id.logInButton);
        logInSkipButton = view.findViewById(R.id.skip_logIn_Button);
        createAccountButton = view.findViewById(R.id.create_account_Button);


        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameTextEdit.getText().toString();
                String password = passwordTextEdit.getText().toString();

                try {
                    MessageDigest md
                            = MessageDigest.getInstance("MD5");

                    String master_salt = getString(R.string.master_salt);
                    md.update(master_salt.getBytes());
                    String saltedHashedPassword = md.digest((password + username).getBytes()).toString();

                    logIn(username,saltedHashedPassword);
                }

                catch (NoSuchAlgorithmException e) {

                    System.out.println("Exception thrown : " + e);
                }
                catch (NullPointerException e) {

                    System.out.println("Exception thrown : " + e);
                }

                //U = romankubiv101
                //P = mypassd

            }
        });

        logInSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.homeFragment2);
            }
        });



    }

    private void logIn(String username, String saltedHashedPassword){
        logInViewModel.fetchLogInUser(username,saltedHashedPassword);
        logInViewModel.getUserTokenAPIResponseMutableLiveData().observe(this, new Observer<UserTokenAPIResponse>() {
            @Override
            public void onChanged(UserTokenAPIResponse userTokenAPIResponse) {
                // add code to "log in" user
                if (userTokenAPIResponse.getUserToken() != null){
                    System.out.println("RECEIVED USER TOKEN = " + userTokenAPIResponse.getUserToken());
                }
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
