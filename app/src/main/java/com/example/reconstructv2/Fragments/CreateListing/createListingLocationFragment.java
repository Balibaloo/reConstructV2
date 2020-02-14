package com.example.reconstructv2.Fragments.CreateListing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;


public class createListingLocationFragment extends Fragment implements OnMapReadyCallback {


    private FloatingActionButton submitButton;
    private ListingFull listingArg;

    private LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private OnFragmentInteractionListener mListener;

//    private Geo mGeoDataClient;

    public createListingLocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_listing_location, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        listingArg = createListingLocationFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getListingArgument();

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        initViews(view);
        startLocationUpdates();
        setOnClickListeners(view);
        configureMap();

    }

    private void configureMap() {
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng currLoc = new LatLng(listingArg.getLocation_lon(), listingArg.getLocation_lat());
        googleMap.addMarker(new MarkerOptions().position(currLoc)
                .title("Your Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
    }


    private void startLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }

        locationManager.requestLocationUpdates("gps", 5000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        listingArg.setLocation_lat(location.getLatitude());
                        listingArg.setLocation_lon(location.getLatitude());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                }

        );
    }

    private void setOnClickListeners(View view) {
        submitButton.setOnClickListener(v -> {
            createListingLocationFragmentDirections.ActionCreateListingLocationFragmentToCreateListingFinish action = createListingLocationFragmentDirections.actionCreateListingLocationFragmentToCreateListingFinish(listingArg);
            Navigation.findNavController(view).navigate(action);
        });

    }

    private void initViews(View view) {
        submitButton = view.findViewById(R.id.create_listing_submit_Button);


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
