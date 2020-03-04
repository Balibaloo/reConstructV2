package com.example.reconstructv2.Fragments.CreateListing.CreateListingMain;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Helpers.DateHelper;
import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


public class CreateListingFragment extends Fragment {

    private RecyclerView itemRecyclerView;

    private ListingFull newListing;

    private String mainImageId;
    private ImageView mainImageView;

    private TextView dateTextEdit;

    private DatePickerDialog datePicker;
    private EditText titleEditText;
    private EditText bodyEditText;


    private FloatingActionButton submitButton;
    private Button addItemButton;

    private CreateListingAdapter recyclerAdapter;
    private ConstraintLayout fragmentContainer;
    private SwipeRefreshLayout loadingRefeshLayout;

    private List<String> uploadedImageIDs;

    private CreateListingFragment.OnFragmentInteractionListener mListener;
    private CreateListingViewModel viewModel;

    public CreateListingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return inflater.inflate(R.layout.fragment_create_listing, container, false);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();



        mainImageId = null;

        // get the listing argument
        newListing = CreateListingFragmentArgs.fromBundle(getArguments()).getListingArgument();

        // check if a list of uploaded image ids was passed
        if (CreateListingFragmentArgs.fromBundle(getArguments()).getUploadedImageIDs() == null) {
            uploadedImageIDs = new ArrayList<String>() {};

        } else {
            uploadedImageIDs = Arrays.asList(CreateListingFragmentArgs.fromBundle(getArguments()).getUploadedImageIDs());
        }

        // create a new listing if the argument is null
        if (newListing == null) {
            newListing = new ListingFull(
                    null,
                    UserInfo.getSelfUserID(getContext()),
                    null,
                    null,
                    null,
                    null,
                    true,
                    null,
                    null,
                    null,
                    null);
        }



        initViews(view);
        initViewModel();
        initRecyclerViewAdapter();
        initDatePicker();
        setOnClickListeners();
        setLiveDataObservers();

        setViewData();
    }



    private void initViews(View view) {
        loadingRefeshLayout = view.findViewById(R.id.create_listing_loading_RefreshLayout);
        loadingRefeshLayout.setVisibility(View.GONE);

        fragmentContainer = view.findViewById(R.id.create_listing_container);
        itemRecyclerView = view.findViewById(R.id.create_listing_item_recyclerView);
        mainImageView = view.findViewById(R.id.create_listing_main_imageView);
        titleEditText = view.findViewById(R.id.create_listing_title_EditText);
        bodyEditText = view.findViewById(R.id.create_listing_bodyTextEdit);
        dateTextEdit = view.findViewById(R.id.create_listing_date_TextView);

        submitButton = view.findViewById(R.id.create_listing_submit_Button);
        addItemButton = view.findViewById(R.id.create_listing_add_item_button);
    }

    // load an imageID into a image view
    private void loadImageInto(ImageView iamgeView, String imageID) {

        try {
            String rootURL = getContext().getResources().getString(R.string.ROOTURL);

            String imageUrl = rootURL + "/getImage?imageID=" + imageID;
            Picasso.get().load(imageUrl).into(iamgeView);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // delete all uploaded images from the server
    public void deleteSavedImages() {
        viewModel.sendDeleteImageRequest(uploadedImageIDs);
        viewModel.getBaseAPIResponse().observe(this, baseAPIResponse -> {

        });
    }


    private void setOnClickListeners() {
        mainImageView.setOnClickListener(v -> {

            if (UserInfo.getIsLoggedIn(getContext())) {

                // check if permissions are granted
                if (ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImage();

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, 1144);
                }

            } else {
                Toast.makeText(getContext(), "you need to be logged in to create a listing", Toast.LENGTH_SHORT).show();
            }

        });

        dateTextEdit.setOnClickListener(v -> datePicker.show());


        submitButton.setOnClickListener(v -> {

            // check if data valid
            if (enteredDataValid()) {

                // sync field data and listing object data
                syncListingData();

                // navigate to location fragment
                CreateListingFragmentDirections.ActionCreateListingFragmentToCreateListingLocationFragment2 action = CreateListingFragmentDirections.actionCreateListingFragmentToCreateListingLocationFragment2(newListing);
                Navigation.findNavController(v).navigate(action);

            }
        });

        addItemButton.setOnClickListener(v -> {

            ListingItem item = new ListingItem(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            List<ListingItem> tempList = newListing.getItemList();
            tempList.add(item);
            newListing.setItemList(tempList);
            recyclerAdapter.setListingItems(newListing.getItemList());
        });

        fragmentContainer.setOnClickListener(v -> KeyboardHelper.hideSoftKeyboard(getActivity()));
    }

    private void setViewData() {
        // set data on views

        titleEditText.setText(newListing.getTitle());
        bodyEditText.setText(newListing.getBody());


        if (newListing.getEnd_date() == null) {
            dateTextEdit.setText(getResources().getString(R.string.dateEntryText));

        } else {
            String dateString = newListing.getEnd_date().split("T")[0].replace("-", "/");
            dateTextEdit.setText("Date Listing Ends : " + dateString);
        }

        mainImageId = newListing.getMainImageID();

        if (mainImageId == null) {
            mainImageView.setImageResource(R.drawable.ic_default_image);
        } else {
            loadImageInto(mainImageView, mainImageId);
        }
    }


    private void syncListingData() {
        newListing.setMainImageID(mainImageId);
        newListing.setTitle(titleEditText.getText().toString());
        newListing.setBody(bodyEditText.getText().toString());

        // check that the user has entered data to fill
        if (!dateTextEdit.getText().toString().equals(getResources().getString(R.string.dateEntryText))) {
            newListing.setEnd_date(DateHelper.formatTextDate(dateTextEdit.getText().toString().split(" ")[4]));
        }
    }

    private boolean enteredDataValid() {
        boolean allCorrect = true;

        // check if the date is not equal to the default string
        if (dateTextEdit.getText().toString().equals(getResources().getString(R.string.dateEntryText))) {
            allCorrect = false;

            dateTextEdit.setError("please select a date");
        }

        // check if the user added at least one item
        if (recyclerAdapter.getItemCount() == 0) {
            allCorrect = false;

            addItemButton.setError("Please add at least one item");
        }

        // check that the user uploaded an image
        if (mainImageId == null) {
            allCorrect = false;

            Toast.makeText(getContext(), "Please select an image for your listing", Toast.LENGTH_LONG).show();
        }

        // check that the user has entered a title
        if (titleEditText.getText().toString().length() <= 3) {
            allCorrect = false;

            titleEditText.setError("Please Enter a title longer than 3 characters");
        }

        // check that the data entered in the items is valid
        if (!listingItemDataValid()) {
            allCorrect = false;
        }

        return allCorrect;
    }

    private boolean listingItemDataValid() {
        // if one item is invalid,
        // the and operation means that the final value will be false

        boolean allValid = true;

        for (ListingItem item : newListing.getItemList()) {
            allValid = allValid && itemDataValid(item);
        }

        return allValid;
    }

    private boolean itemDataValid(ListingItem item) {
        // validate item data

        boolean allValid = true;

        // check if the user has chosen an image for an item
        if (item.getImageIDArray().isEmpty()) {
            Toast.makeText(getContext(), "Please make sure every item has an image", Toast.LENGTH_SHORT).show();

            allValid = false;
        }

        // validate input text
        allValid = (
                InputValidator.validateItemText(item.getName()) &&
                        InputValidator.validateItemText(item.getDescription()) &&
                        allValid
        );

        return allValid;
    }

    private void setLiveDataObservers() {
        viewModel.getImageIDAPIResponse().observe(getViewLifecycleOwner(), imageIDAPIResponse -> {

            // hide the loading view
            loadingRefeshLayout.setRefreshing(false);
            loadingRefeshLayout.setVisibility(View.GONE);


            if (imageIDAPIResponse.getIsSuccesfull()) {
                mainImageId = imageIDAPIResponse.getImageID();

                loadImageInto(mainImageView,mainImageId);


            } else {
                Toast.makeText(getContext(), imageIDAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CreateListingViewModel.class);
    }


    private void initRecyclerViewAdapter() {
        recyclerAdapter = new CreateListingAdapter(getContext());
        itemRecyclerView.setAdapter(recyclerAdapter);

        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemRecyclerView.setHasFixedSize(true);



        recyclerAdapter.setItemLongClickListener(listingItem -> {
            Integer itemIndex = newListing.getItemList().indexOf(listingItem);

            // create new alert
            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

            // set alert the message
            alert.setMessage("are you sure you want to delete this item?")
                    .setCancelable(true);

            // if the user wishes to delete the item
            alert.setNegativeButton("Discard", (dialog, which) -> {

                this.recyclerAdapter.deleteItem(itemIndex);

            }).setPositiveButtonIcon(getResources()
                    .getDrawable(R.drawable.ic_check_white_24dp));

            // if the user cancels, hide the alert
            alert.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel()).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

            // show the alert
            alert.show();
        });


        recyclerAdapter.setOnItemCLickListener(listingItem -> {
            syncListingData();

            // navigate to item edit fragment and scroll to that item
            CreateListingFragmentDirections.ActionCreateListingFragmentToItemListEditFragment action = CreateListingFragmentDirections.actionCreateListingFragmentToItemListEditFragment(newListing);
            action.setFocusedItemIndex(newListing.getItemList().indexOf(listingItem));

            Navigation.findNavController(getView()).navigate(action);

        });

        recyclerAdapter.setListingItems(newListing.getItemList());

    }

    private void initDatePicker() {

        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
            // when date is picked

            // display the date and save it to the item
            dateTextEdit.setText("Date Listing Ends : " + year1 + "/" + month1 + "/" + dayOfMonth);
            newListing.setEnd_date(DateHelper.formatTextDate(dayOfMonth + "/" + month1 + "/" + year1));

            // clear the date view error
            dateTextEdit.setError(null);

        }, year, month, day);


        // set max date limit to 3 months ahead of now
        Calendar dateMaxLimit = Calendar.getInstance();
        dateMaxLimit.add(Calendar.MONTH, 3);
        datePicker.getDatePicker().setMaxDate(dateMaxLimit.getTimeInMillis());

        // set min date limit to now
        Calendar dateMinLimit = Calendar.getInstance();
        datePicker.getDatePicker().setMinDate(dateMinLimit.getTimeInMillis());
    }

    // start pick image activity
    public void pickImage() {
        CropImage.startPickImageActivity(getContext(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if results returned ok
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {

            // if an image is already in the image view, a request is sent to delete it off the sever
            if (mainImageId != null) {

                viewModel.sendDeleteImageRequest(Arrays.asList(mainImageId));
                viewModel.getBaseAPIResponse().observe(this, baseAPIResponse -> {

                    if (baseAPIResponse.getIsSuccesfull()) {
                        uploadedImageIDs.remove(mainImageId);
                        uploadMainImage(data.getData());

                    } else {
                        Toast.makeText(getContext(), baseAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                uploadMainImage(data.getData());
            }
        }
    }

    // upload the main image from a uri
    private void uploadMainImage(Uri uri) {

        loadingRefeshLayout.setVisibility(View.VISIBLE);
        loadingRefeshLayout.setRefreshing(true);

        viewModel.sendUploadImageRequest(uri);
        viewModel.getImageIDAPIResponse().observe(getViewLifecycleOwner(), imageIDAPIResponse -> {

            loadingRefeshLayout.setVisibility(View.INVISIBLE);
            loadingRefeshLayout.setRefreshing(false);

            if (imageIDAPIResponse.getIsSuccesfull()){
                mainImageId = imageIDAPIResponse.getImageID();
                uploadedImageIDs.add(imageIDAPIResponse.getImageID());
            } else {

                Toast.makeText(getContext(), imageIDAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // check that the correct permissions were granted
        if (requestCode == 1144 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateListingFragment.OnFragmentInteractionListener) {
            mListener = (CreateListingFragment.OnFragmentInteractionListener) context;
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
        void onFragmentInteraction(Uri uri);
    }


}
