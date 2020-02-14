package com.example.reconstructv2.Fragments.AccountManagement.AccountMenu;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.R;


public class AccountMenuFragment extends Fragment {

    Button changeAccountButton;
    Button changePasswordButton;
    Button userListingsButton;
    Button watchedListingsButton;
    Button recentViewedListingsButton;
    Button desiredItemsButton;
    Button logOutButton;

    AccountMenuFragment.OnFragmentInteractionListener mListener;

    public AccountMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        initViews(view);
        setOnClickListeners();
    }

    private void setOnClickListeners() {

        changeAccountButton.setOnClickListener(v -> {
            if (userIsLoggedIn()) {
                Navigation.findNavController(v).navigate(AccountMenuFragmentDirections.actionGlobalAccountViewFragment());
            }
        });

        changePasswordButton.setOnClickListener(v -> {
            if (userIsLoggedIn()) {
                Navigation.findNavController(v).navigate(AccountMenuFragmentDirections.actionAccountMenuFragmentToChangePasswordFragment());
            }
        });

        userListingsButton.setOnClickListener(v -> {
            if (userIsLoggedIn()) {
                Navigation.findNavController(v).navigate(AccountMenuFragmentDirections.actionAccountMenuFragmentToUserListings());
            }
        });

        watchedListingsButton.setOnClickListener(v -> {
            if (userIsLoggedIn()) {
                Navigation.findNavController(v).navigate(AccountMenuFragmentDirections.actionAccountMenuFragmentToWatchedListingsFragment());
            }
        });

        recentViewedListingsButton.setOnClickListener(v -> {
            if (userIsLoggedIn()) {
                Navigation.findNavController(v).navigate(AccountMenuFragmentDirections.actionAccountMenuFragmentToRecentlyViewedListings());
            }
        });

        desiredItemsButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(AccountMenuFragmentDirections.actionAccountMenuFragmentToDesiredItemsFragment());
        });

        logOutButton.setOnClickListener(v -> {
            if (userIsLoggedIn()) {
                Navigation.findNavController(v).navigate(AccountMenuFragmentDirections.actionAccountMenuFragmentToLogoutFragment());
            }
        });
    }

    private boolean userIsLoggedIn() {
        if (UserInfo.getIsLoggedIn(getActivity())) {
            return true;
        } else {
            Toast.makeText(getContext(), "You need to be logged in", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void initViews(View view) {
        changeAccountButton = view.findViewById(R.id.accountSettingButton);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);
        userListingsButton = view.findViewById(R.id.viewUserListingsButton);
        watchedListingsButton = view.findViewById(R.id.viewWatchedListingsButton);
        recentViewedListingsButton = view.findViewById(R.id.viewRecentlyViewedListingsButton);
        desiredItemsButton = view.findViewById(R.id.viewDesiredItemsButton);
        logOutButton = view.findViewById(R.id.logoutButton);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_menu, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AccountMenuFragment.OnFragmentInteractionListener) {
            mListener = (AccountMenuFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
