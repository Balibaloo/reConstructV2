package com.example.reconstructv2.Fragments.Home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputEditText;

public class HomeFragment extends Fragment {


    private RecyclerView listingRecyclerView;
    private SwipeRefreshLayout refreshLayout;

    private TextInputEditText searchBar;
    private FloatingActionButton seartchFAB;

    private HomeFragment.OnFragmentInteractionListener mListener;

    private ListingAdapter recyclerAdapter;

    private HomeViewModel homeViewModel;

    public HomeFragment() {
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        initViewModel();
        initViews(view);
        setOnClickListeners();
        setLiveDataObservers();
        configureRecyclerViewAdapter();
        setViewListeners();
        fetchFrontPageListings();

    }

    private void fetchFrontPageListings() {
        homeViewModel.sendFrontPageListingsRequest();
        refreshLayout.setRefreshing(true);
    }

    private void setViewListeners() {
        refreshLayout.setOnRefreshListener(() -> fetchFrontPageListings());
    }

    private void setOnClickListeners(){
        seartchFAB.setOnClickListener(v -> sendSearchRequest());
    }

    private void setLiveDataObservers() {
        homeViewModel.getListingListAPIResponse().observe(getViewLifecycleOwner(), response -> {

            refreshLayout.setRefreshing(false);

            if (response.getIsSuccesfull()) {
                recyclerAdapter.setListings(response.getListings());

            } else {
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(View view) {
        refreshLayout = view.findViewById(R.id.homeSwipeRefreshLayout);

        listingRecyclerView = view.findViewById(R.id.main_recycler_view);

        searchBar = view.findViewById(R.id.homeSearchBar);
        seartchFAB = view.findViewById(R.id.homefloatingActionButton);
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void configureRecyclerViewAdapter() {

        recyclerAdapter = new ListingAdapter(getContext());
        listingRecyclerView.setAdapter(recyclerAdapter);

        listingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listingRecyclerView.setHasFixedSize(true); // (improves performance)

        // navigate to single listing view
        recyclerAdapter.setOnItemClickListener(listing -> {
            MainNavGraphDirections.ActionGlobalSingleListingFragment action = SingleListingFragmentDirections.actionGlobalSingleListingFragment(null, listing.getListingID());
            Navigation.findNavController(getView()).navigate(action);
        });
    }


    private void sendSearchRequest() {

        refreshLayout.setRefreshing(true);

        // if the user does not provide a search string, just fetch front page listings
        if (searchBar.getText().toString().equals("")) {
            homeViewModel.sendFrontPageListingsRequest();
        } else {
            homeViewModel.sendSearchRequest(searchBar.getText().toString(), 0);
        }
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
