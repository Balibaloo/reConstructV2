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

    private Integer pageNumber;

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
        return inflater.inflate(R.layout.fragment_listing_list_view, container, false);
    }

    // method that gets called when the fragment is ready
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        pageNumber = 0;

        initViews(view);
        initViewModel();
        initRecyclerView();
        setOnRefreshListener();
        setLiveDataObservers();

        sendUserListingsRequest();
    }

    private void sendUserListingsRequest(){

        viewModel.sendFetchUserListingsRequest(pageNumber);
    }

    private void setLiveDataObservers() {
        viewModel.getListingListAPIResponse().observe(getViewLifecycleOwner(),
                response -> {
                    refreshLayout.setRefreshing(false);

                    if (response.getIsSuccesfull()) {

                        // if the end of the results has been reached/ set the page number to -1
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


    private void setOnRefreshListener() {
        refreshLayout.setOnRefreshListener(() -> {

            pageNumber = 0;
            sendUserListingsRequest();

        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(userListingsViewModel.class);
    }

    private void initViews(View view) {
        listingRecyclerView = view.findViewById(R.id.listing_list_view_recycler_view);
        refreshLayout = view.findViewById(R.id.listing_list_view_refresh_layout);
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

        recyclerAdapter.setOnBottomReachedListener(() -> {

            // if the end of the results hasn't been reached
            if (pageNumber != -1){

                pageNumber ++;
                sendUserListingsRequest();
            }
        });

        recyclerAdapter.initListings();
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

