package com.example.reconstructv2.Fragments.Home;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragmentDirections;
import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment {

    private LocationManager locationManager;

    private Double userLat;
    private Double userLon;

    private CheckBox postDateSortBox;
    private Switch postDateSelectorSwitch;

    private CheckBox endDateSortBox;
    private Switch endDateSelectorSwitch;

    private CheckBox distanceSortCheckBox;
    private EditText rangeEditText;

    String postDateSortType;
    String endDateSortType;
    String distanceSortType;
    double maxDistance;

    private Integer pageNumber;


    private RecyclerView listingRecyclerView;
    private SwipeRefreshLayout refreshLayout;

    private EditText searchBar;
    private FloatingActionButton searchFAB;

    private HomeFragment.OnFragmentInteractionListener mListener;

    private ListingAdapter recyclerAdapter;

    private HomeViewModel homeViewModel;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        // initialise the page number to load
        pageNumber = 0;

        initViewModel();
        initViews(view);
        setOnClickListeners();
        setLiveDataObservers();
        startLocationUpdates();
        configureRecyclerViewAdapter();
        setViewListeners();
        fetchFrontPageListings();


    }

    private void fetchFrontPageListings() {
        syncFilterSettings();

        // check if the users location has been fetched
        if (userLat != null) {
            homeViewModel.sendFrontPageListingsRequest(maxDistance, userLat, userLon, postDateSortType, endDateSortType, distanceSortType, pageNumber);
            refreshLayout.setRefreshing(true);
        }
    }

    private void setViewListeners() {
        refreshLayout.setOnRefreshListener(() -> searchFAB.callOnClick());
    }

    private void setOnClickListeners() {

        searchFAB.setOnClickListener(v -> {

            // reset the page number to load
            pageNumber = 0;

            // delete all the listings from the recycler view
            recyclerAdapter.initListings();

            sendSearchRequest();
        });
    }

    private void setLiveDataObservers() {
        homeViewModel.getListingListAPIResponse().observe(getViewLifecycleOwner(), response -> {

            refreshLayout.setRefreshing(false);

            if (response.getIsSuccesfull()) {

                // if the results fetched are empty, the end of the results have been reached
                // the page number is set to -1 as a flag to stop fetching next pages
                if (response.getListings().size() == 0){
                    pageNumber = -1;

                } else {
                    recyclerAdapter.addListings(response.getListings());
                }

            } else {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(View view) {

        postDateSortBox = view.findViewById(R.id.postDateCheckBox);
        postDateSelectorSwitch = view.findViewById(R.id.postDateAscDescSwitch);

        endDateSortBox = view.findViewById(R.id.endDateCheckBox);
        endDateSelectorSwitch = view.findViewById(R.id.endDateAscDescSwitch);

        distanceSortCheckBox = view.findViewById(R.id.distanceCheckBox);
        rangeEditText = view.findViewById(R.id.rangeTextEdit);

        refreshLayout = view.findViewById(R.id.homeSwipeRefreshLayout);

        listingRecyclerView = view.findViewById(R.id.main_recycler_view);

        searchBar = view.findViewById(R.id.homeSearchBar);
        searchFAB = view.findViewById(R.id.homeFloatingActionButton);
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    // synchronise the search settings with the variables in the fragment
    private void syncFilterSettings() {

        if (postDateSortBox.isChecked()) {
            postDateSortType = postDateSelectorSwitch.isChecked() ? "ASC" : "DESC";
        } else {
            postDateSortType = "NULL";
        }

        if (endDateSortBox.isChecked()) {
            endDateSortType = endDateSelectorSwitch.isChecked() ? "ASC" : "DESC";
        } else {
            endDateSortType = "NULL";
        }

        if (distanceSortCheckBox.isChecked()) {
            distanceSortType = "ASC";
        } else {
            distanceSortType = "NULL";
        }

        if (rangeEditText.getText().toString().equals("")) {
            maxDistance = 0;
        } else {
            maxDistance = Double.parseDouble(rangeEditText.getText().toString().trim());
        }

    }

    private void configureRecyclerViewAdapter() {

        recyclerAdapter = new ListingAdapter(getContext());
        listingRecyclerView.setAdapter(recyclerAdapter);

        listingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listingRecyclerView.setHasFixedSize(true); // doesent check the height for each item

        // navigate to single listing view
        recyclerAdapter.setOnItemClickListener(listing -> {

            MainNavGraphDirections.ActionGlobalSingleListingFragment action = SingleListingFragmentDirections.actionGlobalSingleListingFragment(null, listing.getListingID());
            Navigation.findNavController(getView()).navigate(action);

        });



        recyclerAdapter.setOnBottomReachedListener(() -> {

            // if the end hasn't been reached
            // fetch next page
            if (pageNumber != -1){
                pageNumber++;

                sendSearchRequest();
            }

        });




    }


    private void sendSearchRequest() {



        refreshLayout.setRefreshing(true);
        syncFilterSettings();

        // if the user does not provide a search string, just fetch front page listings
        if (searchBar.getText().toString().equals("")) {
            homeViewModel.sendFrontPageListingsRequest(maxDistance, userLat, userLon, postDateSortType, endDateSortType, distanceSortType, pageNumber);
        } else {

            // check if the users location has been fetched
            if (userLat != null) {
                homeViewModel.sendSearchRequest(searchBar.getText().toString(), maxDistance, userLat, userLon, postDateSortType, endDateSortType, distanceSortType, pageNumber);

            } else {
                Toast.makeText(getContext(), "Please wait for your location to be fetched", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void startLocationUpdates() {

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);


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
                        userLat = location.getLatitude();
                        userLon = location.getLongitude();
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragment.OnFragmentInteractionListener) {
            mListener = (HomeFragment.OnFragmentInteractionListener) context;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
