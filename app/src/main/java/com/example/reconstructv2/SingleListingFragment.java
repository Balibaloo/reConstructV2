package com.example.reconstructv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class SingleListingFragment extends Fragment {

    private Button toHomeButton;

    public SingleListingFragment() {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_single_listing,container,false);
    }


    @Override
    public  void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        toHomeButton = view.findViewById(R.id.buttonToHome);
        toHomeButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.homeFragment2));

        System.out.println("WHOOO HOOO in listing frag");
    }

}
