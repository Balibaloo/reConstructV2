package com.example.reconstructv2.Fragments.SingleListing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.R;


public class SingleListingFragment extends Fragment {

    private SingleListingFragment.OnFragmentInteractionListener mListener;

    private TextView titleTextView;
    private TextView bodyTextView;
    private RecyclerView itemRecyclerView;
    private ImageView listingImage;
    private Button reserveButton;

    private SingleListingViewModel viewModel;

    public SingleListingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_single_listing,container,false);
    }


    @Override
    public  void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        initViewModel();

        Listing listing = SingleListingFragmentArgs.fromBundle(getArguments()).getListingArgument();

        getListingRequest(listing.getListingID());

        initViews(view);



    }

    private void initViews(View view){
        titleTextView = view.findViewById(R.id.singleListingTitleTextView);
        bodyTextView = view.findViewById(R.id.singleListingBodyTextView);
        itemRecyclerView = view.findViewById(R.id.singleListingRecyclerView);

        listingImage = view.findViewById(R.id.singleListingImageView);
        reserveButton = view.findViewById(R.id.singleListingReserveButton);


    }

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(SingleListingViewModel.class);
    }

    private void getListingRequest(String listingID){
        viewModel.fetchListing(listingID);
    }

    private void setLiveDataObservers(){
        viewModel.getListingLiveData().observe(this, new Observer<SingleListingAPIResponse>() {
            @Override
            public void onChanged(SingleListingAPIResponse singleListingAPIResponse) {
                setListing(singleListingAPIResponse.getListing());
            }
        });

    }

    private void setListing(Listing listing){
        titleTextView.setText(listing.getTitle());
        bodyTextView.setText(listing.getBody());

    }


    private void refreshImage(String imageID){

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SingleListingFragment.OnFragmentInteractionListener) {
            mListener = (SingleListingFragment.OnFragmentInteractionListener) context;
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
