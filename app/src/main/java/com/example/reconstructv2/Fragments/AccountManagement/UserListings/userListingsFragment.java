package com.example.reconstructv2.Fragments.AccountManagement.UserListings;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Fragments.Home.ListingAdapter;
import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragmentDirections;
import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.R;

public class userListingsFragment extends Fragment {

    private ListingAdapter recyclerAdapter;
    private RecyclerView listingRecyclerView;
    private userListingsViewModel viewModel;
    private SwipeRefreshLayout refreshLayout;

    private OnFragmentInteractionListener mListener;

    public userListingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recently_viewed_listings, container, false);
    }

    // method that gets called when the fragment is ready
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        initViews(view);
        initViewModel();
        initRecyclerView();
        setOnRefreshListener();
        setLiveDataObservers();

        // fetch user listings
        viewModel.sendFetchUserListingsRequest();

    }

    private void setLiveDataObservers() {
        viewModel.getListingListAPIResponse().observe(getViewLifecycleOwner(),
                response -> {
                    refreshLayout.setRefreshing(false);

                    if (response.getIsSuccesfull()) {
                        recyclerAdapter.setListings(response.getListings());
                    } else {
                        Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setOnRefreshListener() {
        refreshLayout.setOnRefreshListener(() -> viewModel.sendFetchUserListingsRequest());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(userListingsViewModel.class);
    }

    private void initViews(View view) {
        listingRecyclerView = view.findViewById(R.id.recently_viewed_recycler_view);
        refreshLayout = view.findViewById(R.id.recently_viewed_refresh_layout);
    }



    private void initRecyclerView() {

        recyclerAdapter = new ListingAdapter(getContext());
        listingRecyclerView.setAdapter(recyclerAdapter);

        listingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listingRecyclerView.setHasFixedSize(true); // improves performance

        recyclerAdapter.setOnItemClickListener(listing -> {
            MainNavGraphDirections.ActionGlobalSingleListingFragment action = SingleListingFragmentDirections.actionGlobalSingleListingFragment(null, listing.getListingID());
            Navigation.findNavController(getView()).navigate(action);
        });
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

