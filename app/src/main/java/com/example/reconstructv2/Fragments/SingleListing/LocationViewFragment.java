package com.example.reconstructv2.Fragments.SingleListing;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.reconstructv2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;


public class LocationViewFragment extends Fragment implements OnMapReadyCallback {

    OnFragmentInteractionListener mListener;

    LatLng listingLocation;

    GoogleMap map;
    LocationManager locationManager;

    public LocationViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_view, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get the location to display
        listingLocation = LocationViewFragmentArgs.fromBundle(getArguments()).getLocation();

        // start the map
        configureMap(savedInstanceState);
    }

    private void configureMap(Bundle savedInstanceState) {

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.view_map);
        mapFragment.getMapAsync(this);
        mapFragment.onCreate(savedInstanceState);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        // when the map is ready
        // save the map instance and display the location

        map = googleMap;

        displayLocation();

    }

    private void displayLocation() {

        // add a marker to the app
        map.addMarker(new MarkerOptions().position(listingLocation));

        // zoom in on the marker
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(listingLocation, 15));

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

