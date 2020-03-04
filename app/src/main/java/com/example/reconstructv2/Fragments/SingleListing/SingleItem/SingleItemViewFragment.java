package com.example.reconstructv2.Fragments.SingleListing.SingleItem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.R;


public class SingleItemViewFragment extends Fragment {

    private ListingFull listing;

    private RecyclerView horisontalRecyclerView;
    private Button backButton;

    private SingleItemHorizontalAdapter recyclerAdapter;
    private OnFragmentInteractionListener mListener;

    public SingleItemViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view = getView();

        initViews(view);
        configureRecyclerViewAdapter();
        setOnClickListeners();

        listing = SingleItemViewFragmentArgs.fromBundle(getArguments()).getListing();
        Integer itemPos = SingleItemViewFragmentArgs.fromBundle(getArguments()).getItemPosition();
        recyclerAdapter.setListingItems(listing.getItemList());
        horisontalRecyclerView.scrollToPosition(itemPos);

    }

    private void initViews(View view){
        horisontalRecyclerView = view.findViewById(R.id.singleListingItemsRecyclerView);
        backButton = view.findViewById(R.id.singleListingItemsBackButton);

    }

    private void configureRecyclerViewAdapter(){
        recyclerAdapter = new SingleItemHorizontalAdapter(getContext());
        horisontalRecyclerView.setAdapter(recyclerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        horisontalRecyclerView.setLayoutManager(layoutManager);
        horisontalRecyclerView.setHasFixedSize(true);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(horisontalRecyclerView);

        recyclerAdapter.setOnLongItemCLickListener(listingItem -> {
            if (listingItem.getAvailable()){
                listingItem.toggleIsSelected();
                recyclerAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "this item is un available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setOnClickListeners(){
        backButton.setOnClickListener(v -> {
            SingleItemViewFragmentDirections.ActionSingleItemViewFragmentToSingleListingFragment action = SingleItemViewFragmentDirections.actionSingleItemViewFragmentToSingleListingFragment(listing,null);
            action.setShouldRefresh(false);
            Navigation.findNavController(getView()).navigate(action);
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_listing_item,container,false);
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
