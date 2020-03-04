package com.example.reconstructv2.Fragments.AccountManagement.AccountView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.R;


public class AccountViewFragment extends Fragment {

    private AccountViewViewModel viewModel;
    private SwipeRefreshLayout refreshLayout;

    private String userID;

    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView fNameTextView;
    private TextView lNameTextView;
    private TextView phoneTextView;


    private OnFragmentInteractionListener mListener;

    public AccountViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view = getView();

        initViews(view);
        initViewModel();
        setRefreshListener();
        setLiveDataObserver();

        userID = AccountViewFragmentArgs.fromBundle(getArguments()).getUserID();

        refreshUserData(userID);

    }


    private void setLiveDataObserver() {

        // if the response is successfull show the used details
        viewModel.getUserAPIResponseMutableLiveData().observe(getViewLifecycleOwner(), userAPIResponse -> {

            refreshLayout.setRefreshing(false);
            if (userAPIResponse.getIsSuccesfull()) {
                showUserData(userAPIResponse.getUserProfile());

            } else {
                Toast.makeText(getContext(), userAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    // display the user details in the views
    private void showUserData(User user) {

        usernameTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmail());
        fNameTextView.setText(user.getFirst_name());
        lNameTextView.setText(user.getLast_name());
        //phoneTextView.setText(user.getPhone());

    }

    private void refreshUserData(String userID) {
        viewModel.sendGetUserRequest(userID);
    }

    private void setRefreshListener() {
        refreshLayout.setOnRefreshListener(() -> refreshUserData(userID));
    }


    private void initViews(View view) {
        refreshLayout = view.findViewById(R.id.accountViewRefreshLayout);

        usernameTextView = view.findViewById(R.id.viewTextAccviewUsername);
        emailTextView = view.findViewById(R.id.viewTextAccviewEmail);
        fNameTextView = view.findViewById(R.id.viewTextAccviewFirstName);
        lNameTextView = view.findViewById(R.id.viewTextAccviewLastName);
        phoneTextView = view.findViewById(R.id.viewTextAccviewPhoneNumber);
    }


    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(AccountViewViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_view, container, false);
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
