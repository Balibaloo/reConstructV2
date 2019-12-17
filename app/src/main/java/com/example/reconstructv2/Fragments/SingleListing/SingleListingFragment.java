package com.example.reconstructv2.Fragments.SingleListing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;


public class SingleListingFragment extends Fragment {
    private Listing listingData;
    private RecyclerView itemRecyclerView;
    private ListingItemAdapter recyclerAdapter;

    private SingleListingFragment.OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout refreshLayout;
    private TextView titleTextView;
    private TextView bodyTextView;
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

        initViews(view);
        initViewModel();
        setLiveDataObservers();
        configureRecyclerViewAdapter();
        setRefreshListener();

        listingData = SingleListingFragmentArgs.fromBundle(getArguments()).getListingArgument();
        this.getListingRequest(listingData.getListingID());

    }

    private void initViews(View view){
        titleTextView = view.findViewById(R.id.singleListingTitleTextView);
        bodyTextView = view.findViewById(R.id.singleListingBodyTextView);
        itemRecyclerView = view.findViewById(R.id.singleListingRecyclerView);

        refreshLayout = view.findViewById(R.id.singleListingSwipeRefreshLayout);
        listingImage = view.findViewById(R.id.singleListingImageView);
        reserveButton = view.findViewById(R.id.singleListingReserveButton);
    }

    private void setRefreshListener(){
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getListingRequest(listingData.getListingID());
            }
        });
    }

    private void configureRecyclerViewAdapter(){
      recyclerAdapter = new ListingItemAdapter(getContext());
      itemRecyclerView.setAdapter(recyclerAdapter);

      itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      itemRecyclerView.setHasFixedSize(true);

      new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,0) {
          @Override
          public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
              return false;
          }

          @Override
          public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

          }
      }).attachToRecyclerView(itemRecyclerView);

      SnapHelper helper = new LinearSnapHelper();
      helper.attachToRecyclerView(itemRecyclerView);



      recyclerAdapter.setOnItemCLickListener(new ListingItemAdapter.OnClickListener() {
          @Override
          public void onItemClick(ListingItem listingItem) {
              // navigate to item in item view
          }
      });

      // add long press
    };

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(SingleListingViewModel.class);
    }

    private void getListingRequest(String listingID){
        viewModel.fetchListing(getContext(),listingID);
    }

    private void setLiveDataObservers(){
        viewModel.getListingLiveData().observe(this, new Observer<SingleListingAPIResponse>() {
            @Override
            public void onChanged(SingleListingAPIResponse singleListingAPIResponse) {
                System.out.println("listing items updated " + singleListingAPIResponse.getListing().getItemList().get(0).getName());
                setListing(singleListingAPIResponse.getListing());
                recyclerAdapter.setListingItems(singleListingAPIResponse.getListing().getItemList());
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void setListing(Listing listing){
        titleTextView.setText(listing.getTitle());
        bodyTextView.setText(listing.getBody());

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
