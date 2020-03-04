package com.example.reconstructv2.Fragments.CreateListing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;


public class CreateListingLocationFragment extends Fragment implements OnMapReadyCallback {


    private Button submitButton;
    private ListingFull listingArg;

    private GoogleMap map;

    private LocationManager locationManager;

    private OnFragmentInteractionListener mListener;

    public CreateListingLocationFragment() {
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

        // store the listing argument
        listingArg = CreateListingLocationFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getListingArgument();

        initViews(view);
        configureMap(savedInstanceState);
        setOnClickListeners(view);

    }

    private void configureMap(Bundle bundle) {

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.edit_map);
        mapFragment.getMapAsync(this);
        mapFragment.onCreate(bundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // when the map loads, save the map and start fetching client location
        map = googleMap;
        startLocationUpdates();
    }


    private void startLocationUpdates() {

        // check that the app has permissions
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            // request permissions
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                    , 10);

            return;
        }

        // start location updates
        locationManager.requestLocationUpdates("gps", 5000, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        // set the location on the listing
                        listingArg.setLocation_lat(location.getLatitude());
                        listingArg.setLocation_lon(location.getLongitude());

                        // check if the map is ready
                        if (map != null) {

                            LatLng currLocation = new LatLng(listingArg.getLocation_lat(), listingArg.getLocation_lon());

                            // add a marker to the map
                            map.addMarker(new MarkerOptions().position(currLocation)
                                    .title("Your Location"));

                            // zoom in on the marker
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 15));

                        }
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

            // check if the location properties are set to the default values
            if (listingArg.getLocation_lat() == 0.0
                    &&
                    listingArg.getLocation_lon() == 0.0){

                Toast.makeText(getContext(), "Please wait for your location to be fetched", Toast.LENGTH_SHORT).show();
            } else {

                // navigate to the finish fragment
                CreateListingLocationFragmentDirections.ActionCreateListingLocationFragmentToCreateListingFinish action = CreateListingLocationFragmentDirections.actionCreateListingLocationFragmentToCreateListingFinish(listingArg);
                Navigation.findNavController(view).navigate(action);
            }


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
