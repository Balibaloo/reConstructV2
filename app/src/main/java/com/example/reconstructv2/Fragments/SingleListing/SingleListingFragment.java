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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


public class SingleListingFragment extends Fragment {

    private String listingID;
    private ListingFull listingData;
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
        setOnClickListeners();
        setLiveDataObservers();
        configureRecyclerViewAdapter();
        setRefreshListener();

        if (SingleListingFragmentArgs.fromBundle(getArguments()).getShouldRefresh()) {
            System.out.println("should refresh");
            listingID = SingleListingFragmentArgs.fromBundle(getArguments()).getListingID();
            this.getListingRequest(listingID);
        } else {
            System.out.println("should not refresh");
            listingData = SingleListingFragmentArgs.fromBundle(getArguments()).getListingFullArg();
            listingID = listingData.getListingID();
            setListing(listingData);
            recyclerAdapter.setListingItems(listingData.getItemList());
        }
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
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            getListingRequest(listingID);
        });
    }

    private void setOnClickListeners(){
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listingData instanceof ListingFull) {
                    refreshLayout.setRefreshing(true);
                    viewModel.reserveItemsRequest(getContext(),listingData);
                } else {
                    Toast.makeText(getContext() , "please wait for the listing to be fetched from the server", Toast.LENGTH_SHORT).show();
                }
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

      recyclerAdapter.setOnItemCLickListener(new ListingItemAdapter.OnClickListener() {
          @Override
          public void onItemClick(ListingItem listingItem) {
              Integer itemPosition = listingData.getItemList().indexOf(listingItem);
              SingleListingFragmentDirections.ActionSingleListingFragmentToSingleItemViewFragment action = SingleListingFragmentDirections.actionSingleListingFragmentToSingleItemViewFragment(listingData,itemPosition);
              Navigation.findNavController(getView()).navigate(action);
          }
      });

      recyclerAdapter.setLongClickListener(new ListingItemAdapter.OnLongPressListener() {
          @Override
          public void onLongPress(ListingItem listingItem) {
              // check if available
              if (listingItem.getAvailable()){
                  listingItem.toggleIsSelected();
                  recyclerAdapter.notifyDataSetChanged();
              } else {
                  Toast.makeText(getContext(), "this item is un available", Toast.LENGTH_SHORT).show();
              }

          }
      });

    };

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(SingleListingViewModel.class);
    }

    private void getListingRequest(String listingID){
        viewModel.fetchListing(getContext(),listingID);
        viewModel.getListingLiveData().observe(getViewLifecycleOwner(), response -> {
            refreshLayout.setRefreshing(false);

            if (response.getIsSuccesfull()){
                listingData = response.getListing();
                setListing(listingData);
                recyclerAdapter.setListingItems(listingData.getItemList());
                recyclerAdapter.notifyDataSetChanged();
            }
        });

    }

    private void setLiveDataObservers(){
        viewModel.getBaseAPIResponseLiveData().observe(getViewLifecycleOwner(), response -> {
            refreshLayout.setRefreshing(false);

            if (response.getIsSuccesfull()){
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });




    }

    private void setListing(ListingFull listing){
        titleTextView.setText(listing.getTitle());
        bodyTextView.setText(listing.getBody());

        try {
            String rootURL = getContext().getResources().getString(R.string.ROOTURL);
            String imageUrl = rootURL + "/getImage?imageID=" + listing.getMainImageID();
            Picasso.get().load(imageUrl).into(listingImage);
        } catch (Exception e){
            System.out.println(e.toString());
        }



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
