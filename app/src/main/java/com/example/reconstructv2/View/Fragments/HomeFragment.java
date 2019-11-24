package com.example.reconstructv2.View.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.R;
import com.example.reconstructv2.View.Adapters.ListingAdapter;
import com.example.reconstructv2.ViewModels.HomeViewModel;

import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {
    private Button toSingleListingButton;
    private RecyclerView listingRecyclerView;
    private SwipeRefreshLayout refreshLayout;
    private HomeFragment.OnFragmentInteractionListener mListener;

    private HomeViewModel homeViewModel;

    public HomeFragment() {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public  void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        refreshLayout = view.findViewById(R.id.homeSwipeRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                homeViewModel.deleteAll();
                homeViewModel.insert(new Listing("refreshed listing id","Randpom Autrhor id","refreshed TITLE", "refreshed body 1 2 3", "2002-07-15T10:30:05.000Z","2002-07-15T10:30:05.000Z","some Address",true));
                refreshLayout.setRefreshing(false);
            }
        });

        listingRecyclerView = view.findViewById(R.id.main_recycler_view);
        listingRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listingRecyclerView.setHasFixedSize(true);

        final ListingAdapter adapter = new ListingAdapter();
        listingRecyclerView.setAdapter(adapter);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getAllListings().observe(this, new Observer<List<Listing>>() {
            @Override
            public void onChanged(List<Listing> listings) {
                adapter.setListings(listings);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(listingRecyclerView);
        adapter.setOnItemClickListener(new ListingAdapter.OnClickListener() {
            @Override
            public void onItemClick(Listing listing) {

                MainNavGraphDirections.ActionGlobalSingleListingFragment action = SingleListingFragmentDirections.actionGlobalSingleListingFragment(listing);
                Navigation.findNavController(getView()).navigate(action);
                // figure out how to create an action
//                Bundle bundle = new Bundle();
//                bundle.("listingArgument",listing);
//
            }
        });


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
        void onHomeFragmentInteraction(Uri uri);
    }

}
