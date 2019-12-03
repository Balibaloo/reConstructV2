package com.example.reconstructv2.Fragments.CreateUser;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.reconstructv2.R;


public class CreateUserFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private EditText emailEditText;

    private Boolean dataCorrect;

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

        // add dynamic input validation to fields

        // on next button pressed navigate to finish create with arguments
        usernameEditText = view.findViewById(R.id.editTextCreateUsername);
        passwordEditText = view.findViewById(R.id.editTextCreatePassword);
        passwordAgainEditText = view.findViewById(R.id.editTextCreatePasswordRepeat);
        emailEditText = view.findViewById(R.id.editTextCreateEmail);

        nextButton = view.findViewById(R.id.buttonNext);

        dataCorrect = true;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dataCorrect) {

                    String email = emailEditText.getText().toString();
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    CreateUserFragmentDirections.ActionCreateUserFragmentToFinishCreateUserFragment action = CreateUserFragmentDirections.actionCreateUserFragmentToFinishCreateUserFragment(email, username, password);
                    Navigation.findNavController(getView()).navigate(action);
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
        void onFragmentInteraction(Uri uri);
    }
}
