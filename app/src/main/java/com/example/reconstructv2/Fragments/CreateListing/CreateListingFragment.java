package com.example.reconstructv2.Fragments.CreateListing;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Observable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Fragments.SingleListing.ListingItemAdapter;
import com.example.reconstructv2.Helpers.DateHelper;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.ImageIDAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


public class CreateListingFragment extends Fragment {

    private RecyclerView itemRecyclerView;

    private ListingFull newListing;

    private String mainImageId;
    private ImageView mainImageView;

    private TextView dateTextEdit;
    private DatePickerDialog datePicker;
    private EditText titleEditText;

    private FloatingActionButton submitButton;
    private Button addItemButton;

    private CreateListingAdapter recyclerAdapter;
    private ConstraintLayout fragmentContainer;
    private SwipeRefreshLayout loadingRefeshLayout;

    private String[] uploadedImageIDs;

    private CreateListingFragment.OnFragmentInteractionListener mListener;
    private CreateListingViewModel viewModel;

    public CreateListingFragment() {}

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_listing,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        mainImageId = null;

        newListing = CreateListingFragmentArgs.fromBundle(getArguments()).getListingArgument();
        uploadedImageIDs = CreateListingFragmentArgs.fromBundle(getArguments()).getUploadedImageIDs();

        if (uploadedImageIDs == null) {
            uploadedImageIDs = new String[]{""};
        }

        if (newListing == null){
            ArrayList<ListingItem> listingItems = new ArrayList<>();
            listingItems.add(new ListingItem(
                    "temporary id",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null));

            newListing = new ListingFull(
                    null,
                    UserInfo.getSelfUserID(getContext()),
                    "Listing Title",
                    "Listing description",
                    null,
                    null,
                    null,
                    true,
                    null,
                    listingItems);
        }




        initViews(view);
        initViewModel();
        initRecyclerViewAdapter();
        initDatePicker();
        setOnClickListeners();
        setLiveDataObservers();

    }


    private void initViews(View view){
        loadingRefeshLayout = view.findViewById(R.id.create_listing_loading_RefreshLayout);
        loadingRefeshLayout.setVisibility(View.GONE);

        fragmentContainer = view.findViewById(R.id.create_listing_container);
        itemRecyclerView = view.findViewById(R.id.create_listing_item_recyclerView);
        mainImageView = view.findViewById(R.id.create_listing_main_imageView);
        titleEditText = view.findViewById(R.id.create_listing_title_EditText);
        dateTextEdit = view.findViewById(R.id.create_listing_date_TextView);

        submitButton = view.findViewById(R.id.create_listing_submit_Button);
        addItemButton = view.findViewById(R.id.create_listing_add_item_button);
    }

    private void setOnClickListeners(){

        mainImageView.setOnClickListener(v -> {
            if (UserInfo.getIsLoggedIn(getContext())){

                if (mainImageId == null) { // MOVE  CHECK FROM HERE

                    Boolean hasWritePermissions = ContextCompat.checkSelfPermission(getContext(),WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                    if (hasWritePermissions){

                        pickImage();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),new String[]{}, 1144);
                    }
                } else {
                    viewModel.deleteImageRequest(mainImageId);
                    pickImage();
                }




            } else {
                Toast.makeText(getContext(), "you need to be logged in to create a listing", Toast.LENGTH_SHORT).show();
            }

        });

        submitButton.setOnClickListener(v -> {

            // ADD CHECKS FOR FIELDS BEING COMPLETED

            if (checkDataEntred()) {
                // upload all images, save them as imageIDs then upload listing

                List<Observable<?>> requests = new ArrayList<>();

                String authorID = UserInfo.getSelfUserID(getContext());
                String title = titleEditText.getText().toString();
                String body = ""; // ADD BODY TEXT FIELD
                String dateString = DateHelper.formatTextDate(dateTextEdit.getText().toString().split(" ")[4]);
                String location = "";
                String mainimageid = mainImageId;
                List<ListingItem> itemList = new ArrayList<>();

                ListingFull newListing = new ListingFull("",authorID,title,body,"",dateString,location,true,mainimageid,itemList);

                viewModel.createListingRequest(newListing);
            }
        });

        dateTextEdit.setOnClickListener(v -> datePicker.show());

        addItemButton.setOnClickListener(v -> {

            ListingItem item = new ListingItem(
                    "temporary id",
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
    };

    private boolean checkDataEntred(){
        boolean allValid = true;







        return allValid;

    }


    private void setLiveDataObservers(){
        viewModel.getImageIDAPIResponse().observe(getViewLifecycleOwner(), imageIDAPIResponse -> {

            // CONVERT TO BULK IMAGE UPLOAD

            loadingRefeshLayout.setRefreshing(false);
            loadingRefeshLayout.setVisibility(View.GONE);
            if (imageIDAPIResponse.getIsSuccesfull()){
                System.out.println("image saved");
                System.out.println("imageID = " + imageIDAPIResponse.getImageID());
                mainImageId = imageIDAPIResponse.getImageID();

                String rootURL = getContext().getResources().getString(R.string.ROOTURL);
                String imageUrl = rootURL + "/getImage?imageID=" + mainImageId;
                Picasso.get().load(imageUrl).into(mainImageView);

            } else {
                Toast.makeText(getContext(), imageIDAPIResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(CreateListingViewModel.class);
    }

    private void initRecyclerViewAdapter(){
    recyclerAdapter = new CreateListingAdapter(getContext());
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

    recyclerAdapter.setOnItemCLickListener(listingItem -> {
        Integer itemIndex = newListing.getItemList().indexOf(listingItem);
        CreateListingFragmentDirections.ActionCreateListingFragmentToItemListEditFragment action = CreateListingFragmentDirections.actionCreateListingFragmentToItemListEditFragment(newListing);
        action.setFocusedItemIndex(itemIndex);
        Navigation.findNavController(getView()).navigate(action);
    });

    recyclerAdapter.setLongClickListener(listingItem -> Toast.makeText(getContext(), "long press on listing item", Toast.LENGTH_SHORT).show());

    recyclerAdapter.setListingItems(newListing.getItemList());

    }

    private void initDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        datePicker  = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateTextEdit.setText("Date Listing Ends : " + dayOfMonth + "/" + month + "/" + year);
            }
        },year,month,day);

        Calendar dateMaxLimit = Calendar.getInstance();
        dateMaxLimit.add(Calendar.MONTH, 2);
        datePicker.getDatePicker().setMaxDate(dateMaxLimit.getTimeInMillis()); // set max date to 2 months ahead

        Calendar dateMinLimit = Calendar.getInstance();
        datePicker.getDatePicker().setMinDate(dateMinLimit.getTimeInMillis()); // set min date to now
    };


    public void pickImage(){
        CropImage.startPickImageActivity(getContext(),this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadingRefeshLayout.setRefreshing(false);
        loadingRefeshLayout.setVisibility(View.GONE);
        // fetch result from image selection
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK){
            // set main image imageID to uri


            // MOVE CHECK TO HERE


            loadingRefeshLayout.setVisibility(View.VISIBLE);
            loadingRefeshLayout.setRefreshing(true);

            viewModel.uploadImageRequest(data.getData());
            viewModel.getImageIDAPIResponse().observe(getViewLifecycleOwner(), imageIDAPIResponse -> {
                mainImageId = imageIDAPIResponse.getImageID();
                uploadedImageIDs = arrayAdd(uploadedImageIDs,imageIDAPIResponse.getImageID());

            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1144 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
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

    public static String[] arrayAdd(String[] stringArray, String newValue)
    {
        String[] tempArray = new String[ stringArray.length + 1 ];
        for (int i=0; i<stringArray.length; i++)
        {
            tempArray[i] = stringArray[i];
        }
        tempArray[stringArray.length] = newValue;
        return tempArray;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Uri uri);
    }


}
