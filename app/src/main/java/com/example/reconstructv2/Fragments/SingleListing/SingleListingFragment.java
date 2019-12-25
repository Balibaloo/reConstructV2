package com.example.reconstructv2.Fragments.SingleListing;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.MainNavGraphDirections;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SingleListingFragment extends Fragment {
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
            this.getListingRequest(SingleListingFragmentArgs.fromBundle(getArguments()).getListingID());
        } else {
            System.out.println("should not refresh");
            listingData = SingleListingFragmentArgs.fromBundle(getArguments()).getListingFullArg();
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                getListingRequest(listingData.getListingID());
            }
        });
    }

    private void setOnClickListeners(){
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listingData instanceof ListingFull) {
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
        viewModel = ViewModelProviders.of(this).get(SingleListingViewModel.class);
    }

    private void getListingRequest(String listingID){
        viewModel.fetchListing(getContext(),listingID);
    }

    private void setLiveDataObservers(){
        viewModel.getBaseAPIResponseLiveData().observe(this, new Observer<BaseAPIResponse>() {
            @Override
            public void onChanged(BaseAPIResponse baseAPIResponse) {
                Toast.makeText(getContext(), baseAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        viewModel.getListingLiveData().observe(this, new Observer<SingleListingAPIResponse>() {
            @Override
            public void onChanged(SingleListingAPIResponse singleListingAPIResponse) {
                System.out.println("received data from server");
                listingData = singleListingAPIResponse.getListing();
                setListing(listingData);
                recyclerAdapter.setListingItems(listingData.getItemList());
                refreshLayout.setRefreshing(false);
            }
        });

    }

    private void setListing(ListingFull listing){
        titleTextView.setText(listing.getTitle());
        bodyTextView.setText(listing.getBody());

        String rootURL = getContext().getResources().getString(R.string.ROOTURL);
        String imageUrl = rootURL + "/getImage?imageID=" + listing.getMainImageID();
        Picasso.get().load(imageUrl).into(listingImage);


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
