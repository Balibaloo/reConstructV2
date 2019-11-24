package com.example.reconstructv2.View.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.example.reconstructv2.R;
import com.example.reconstructv2.View.Fragments.HomeFragment;
import com.example.reconstructv2.View.Fragments.SingleListingFragment;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, SingleListingFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onHomeFragmentInteraction(Uri uri) {
        // Do stuff
    }

    @Override
    public void onSingleListingFragmentInteraction(Uri uri) {
        // Do different stuff
    }
}
