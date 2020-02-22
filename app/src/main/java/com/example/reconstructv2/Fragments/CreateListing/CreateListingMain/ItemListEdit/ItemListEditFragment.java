package com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.ItemListEdit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class ItemListEditFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout refreshLayout;
    private ListingFull listing;

    private EditListingItemAdapter recyclerAdapter;
    private RecyclerView horisontalRecyclerView;

    private FloatingActionButton backButton;

    private Integer currListingPosition;
    private Integer currListingImagePosition;

    private ItemListEditViewModel viewModel;

    public ItemListEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list_edit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final View view = getView();



        initViews(view);
        initViewModel();
        configureRecyclerViewAdapter();
        setOnClickListeners();
        setLiveDataObservers();

        currListingPosition = 0;
        listing = ItemListEditFragmentArgs.fromBundle(getArguments()).getListing();
        Integer itemPos = ItemListEditFragmentArgs.fromBundle(getArguments()).getFocusedItemIndex();
        recyclerAdapter.setListingItems(listing.getItemList());
        horisontalRecyclerView.scrollToPosition(itemPos);

    }

    private void initViews(View view){
        horisontalRecyclerView = view.findViewById(R.id.item_list_edit_recyclerView);
        refreshLayout = view.findViewById(R.id.item_list_edit_SwipeRefreshLayout);
        backButton = view.findViewById(R.id.item_list_edit_return_FAB);
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(this).get(ItemListEditViewModel.class);
    }

    private void configureRecyclerViewAdapter(){
        recyclerAdapter = new EditListingItemAdapter(getActivity(), (position, charSeq, field) -> {
            List<ListingItem> tempList = listing.getItemList();
            ListingItem editedItem = tempList.get(position);

            if (field.equals("title")) {
                editedItem.setName(charSeq);
            } else if (field.equals("body")) {
                editedItem.setDescription(charSeq);
            }

            tempList.set(position, editedItem);
            listing.setItemList(tempList);

        }, (itemPos, imagePos)-> {
            if (UserInfo.getIsLoggedIn(getContext())){
                Boolean hasWritePermissions = ContextCompat.checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (hasWritePermissions){

                    currListingPosition = itemPos;
                    currListingImagePosition = imagePos;
                    pickImage();

                } else {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{}, 1144);
                }
            } else {
                Toast.makeText(getContext(), "you need to be logged in to upload an image", Toast.LENGTH_SHORT).show();
            }
        });

        horisontalRecyclerView.setAdapter(recyclerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        horisontalRecyclerView.setLayoutManager(layoutManager);
        horisontalRecyclerView.setHasFixedSize(true);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(horisontalRecyclerView);

        recyclerAdapter.setLongClickListener(listingItem -> Toast.makeText(getContext(), "are you sure you want to delete this item?", Toast.LENGTH_SHORT).show());
    }

    private void setOnClickListeners(){
        backButton.setOnClickListener(v -> {
            ItemListEditFragmentDirections.ActionItemListEditFragmentToCreateListingFragment action = ItemListEditFragmentDirections.actionItemListEditFragmentToCreateListingFragment();
            action.setListingArgument(listing);
            Navigation.findNavController(getView()).navigate(action);
        });
    }

    private void setLiveDataObservers(){
        viewModel.getImageIDAPIResponse().observe(getViewLifecycleOwner(), imageIDAPIResponse -> {
            if (imageIDAPIResponse.getIsSuccesfull()){

                listing.setListingItemImage(currListingPosition, currListingImagePosition ,imageIDAPIResponse.getImageID());
                recyclerAdapter.setListingItems(listing.getItemList());

            } else {
                Toast.makeText(getContext(), imageIDAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void pickImage(){
        CropImage.startPickImageActivity(getContext(),this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // fetch result from image selection
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK){
            viewModel.uploadImageRequest(data.getData());
        }}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1144 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            pickImage();
        }
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
