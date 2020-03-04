package com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.ItemListEdit;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class ItemListEditFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout refreshLayout;
    private ListingFull listing;

    private EditListingItemAdapter recyclerAdapter;
    private RecyclerView horizontalRecyclerView;

    private FloatingActionButton backButton;

    private Integer currListingItemPosition;
    private Integer currListingItemImagePosition;

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

    // when the fragment is ready
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view = getView();

        initViews(view);
        initViewModel();

        configureRecyclerViewAdapter();

        setOnClickListeners();
        setLiveDataObservers();

        currListingItemPosition = 0;

        listing = ItemListEditFragmentArgs.fromBundle(getArguments()).getListing();

        Integer itemPos = ItemListEditFragmentArgs.fromBundle(getArguments()).getFocusedItemIndex();
        recyclerAdapter.setListingItems(listing.getItemList());

        horizontalRecyclerView.scrollToPosition(itemPos);

    }

    private void initViews(View view) {

        horizontalRecyclerView = view.findViewById(R.id.item_list_edit_recyclerView);
        refreshLayout = view.findViewById(R.id.item_list_edit_SwipeRefreshLayout);
        backButton = view.findViewById(R.id.item_list_edit_return_FAB);
    }


    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ItemListEditViewModel.class);
    }

    private void configureRecyclerViewAdapter() {

        recyclerAdapter = new EditListingItemAdapter(getActivity(),

                // on image upload request
                (itemPos, imagePos) -> {
                    if (UserInfo.getIsLoggedIn(getContext())) {

                        // check if we have permissions to read local storage
                        Boolean hasReadPermissions = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                        if (hasReadPermissions) {

                            // save item and image positions so that when the request is receive
                            // the image id can be written to the correct location
                            currListingItemPosition = itemPos;
                            currListingItemImagePosition = imagePos;

                            // start an activity to pick an image from library
                            pickImage();

                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, 1144);
                        }
                    } else {
                        Toast.makeText(getContext(), "you need to be logged in to upload an image", Toast.LENGTH_SHORT).show();
                    }
                },

                // on item long press
                listingItem -> {

                    Integer itemIndex = listing.getItemList().indexOf(listingItem);


                    // create new alert
                    final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    // set alert the message
                    alert.setMessage("are you sure you want to delete this item?")
                            .setCancelable(true);

                    // if the user wishes to delete the item
                    alert.setNegativeButton("Discard", (dialog, which) -> {
                        recyclerAdapter.deleteItem(itemIndex);
                    }).setPositiveButtonIcon(getResources()
                            .getDrawable(R.drawable.ic_check_white_24dp));

                    // if the user cancels, hide the alert
                    alert.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel()).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

                    // show the alert
                    alert.show();

                }, horizontalRecyclerView);

        horizontalRecyclerView.setAdapter(recyclerAdapter);

        // set a new layout manager
        horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizontalRecyclerView.setHasFixedSize(true); // (improves performance)

        // create a snap helper so that items snap to positions on the screen
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(horizontalRecyclerView);
    }


    private void setOnClickListeners() {
        backButton.setOnClickListener(v -> {

            // create a new navigation action
            ItemListEditFragmentDirections.ActionItemListEditFragmentToCreateListingFragment action = ItemListEditFragmentDirections.actionItemListEditFragmentToCreateListingFragment();
            action.setListingArgument(listing);

            // navigate
            Navigation.findNavController(getView()).navigate(action);
        });
    }

    private void setLiveDataObservers() {

        viewModel.getImageIDAPIResponse().observe(getViewLifecycleOwner(),
                imageIDAPIResponse -> {
                    if (imageIDAPIResponse.getIsSuccesfull()) {

                        // saves image id to the location defined in on image upload request
                        listing.setListingItemImage(currListingItemPosition, currListingItemImagePosition, imageIDAPIResponse.getImageID());
                        recyclerAdapter.notifyItemChanged(currListingItemPosition);

                    } else {
                        Toast.makeText(getContext(), imageIDAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void pickImage() {
        CropImage.startPickImageActivity(getContext(), this);
    }


    // when activities return with results
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // fetch result from image selection
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            // send upload request
            viewModel.sendUploadImageRequest(data.getData());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // check if the permissions were granted
        if (requestCode == 1144 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
