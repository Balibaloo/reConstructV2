package com.example.reconstructv2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {
    private Button toSingleListingButton;

    public HomeFragment() {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public  void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        toSingleListingButton = view.findViewById(R.id.buttonToSingleListing);
        toSingleListingButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.singleListingFragment));

        System.out.println("WHOOO HOOO in home frag");
    }

}
