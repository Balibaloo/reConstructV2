package com.example.reconstructv2.Fragments.AccountManagement.AccountFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.reconstructv2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class desiredItemsFragment extends Fragment {


    public desiredItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_desired_items, container, false);
    }

}
