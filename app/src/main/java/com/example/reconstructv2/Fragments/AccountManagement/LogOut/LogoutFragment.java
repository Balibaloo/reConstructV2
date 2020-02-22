package com.example.reconstructv2.Fragments.AccountManagement.LogOut;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutFragment extends Fragment {

    Button logOutButton;
    Button cancelButton;

    public LogoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_out, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();


        initViews(view);
        setOnClickListeners();
    }

    private void initViews(View view) {
        logOutButton = view.findViewById(R.id.logoutConfirmButton);
        cancelButton = view.findViewById(R.id.logoutCancelButton);
    }

    private void setOnClickListeners() {
        logOutButton.setOnClickListener(v -> {
            UserInfo.logOut(getContext());
            Navigation.findNavController(getView()).navigate(R.id.homeFragment2);
        });

        cancelButton.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.accountMenuFragment));
    }
}
