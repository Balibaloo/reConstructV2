package com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.ItemListEdit;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EditListingItemAdapter extends RecyclerView.Adapter<EditListingItemAdapter.ListingItemHolder> {

    private List<ListingItem> listingItems = new ArrayList<>();

    private EditListingItemAdapter.OnLongPressListener longClickListener;
    private OnImageUploadRequestListener onImageUploadRequestListener;
    private RecyclerView recyclerView;

    private Activity mActivity;

    public EditListingItemAdapter(Activity activity, OnImageUploadRequestListener onImageUploadRequestListener, OnLongPressListener onLongPressListener, RecyclerView recyclerView) {
        this.mActivity = activity;
        this.longClickListener = onLongPressListener;
        this.onImageUploadRequestListener = onImageUploadRequestListener;
        this.recyclerView = recyclerView;
    }


    @NonNull
    @Override
    public EditListingItemAdapter.ListingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Creates and returns a ListingHolder, the layout used in the recycler view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_item_card_full_edit, parent, false);

        return new EditListingItemAdapter.ListingItemHolder(itemView);
    }


    // sets up each individual item in the recycler view
    @Override
    public void onBindViewHolder(@NonNull EditListingItemAdapter.ListingItemHolder holder, final int position) {

        ListingItem currItem = listingItems.get(position);

        if (currItem.getImageIDArray().isEmpty()) {
            // if no image is found, load the default image
            holder.itemImage.setImageResource(R.drawable.ic_default_image);
        } else {
            // load image into image view
            loadImageInto(holder.itemImage, currItem.getImageIDArray().get(0));
        }

        // set text on fields
        holder.TextEditTitle.setText(currItem.getName());
        holder.TextEditDescription.setText(currItem.getDescription());

        // validate the input text
        InputValidator.validateItemText(holder.TextEditTitle, "name");
        InputValidator.validateItemText(holder.TextEditDescription, "description");

        // if the current item is the last item, display the add item button
        if (position == listingItems.size() - 1) {
            holder.addItemButton.setVisibility(View.VISIBLE);
        } else {
            holder.addItemButton.setVisibility(View.GONE);
        }

        // sets the item background colour based on the items status
        if (!currItem.getAvailable()) {
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_un_available);

        } else if (currItem.getIsSelected()) {
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_selected);

        } else {
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_base);
        }

        // adds a new item to the end of the item list
        holder.addItemButton.setOnClickListener(v -> {
            listingItems.add(new ListingItem(
                    "temporary id",
                    null,
                    null,
                    null,
                    null,
                    null
            ));


            // notify that data has changed and scroll to the new item
            notifyItemInserted(listingItems.size() - 1);
            if (listingItems.size() > 1) {
                notifyItemChanged(listingItems.size() - 2);
            }
            recyclerView.scrollToPosition(listingItems.size() - 1);
        });

        // set a long click listener on the item layout
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onLongPress(listingItems.get(position));
            return false;
        });

        // set on click listeners
        holder.itemImage.setOnClickListener(v -> onImageUploadRequestListener.onImageUploadRequest(position, 0));
        holder.itemLayout.setOnClickListener(v -> KeyboardHelper.hideSoftKeyboard(mActivity));

        holder.TextEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // update item data
                ListingItem currItem = listingItems.get(position);
                currItem.setName(s.toString());
                listingItems.set(position, currItem);
            }
        });


        holder.TextEditDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // update item data
                ListingItem currItem = listingItems.get(position);
                currItem.setDescription(s.toString());
                listingItems.set(position, currItem);
            }
        });

    }

    // load imageID into an image view
    private void loadImageInto(ImageView imageView, String imageID) {
        try {
            String rootURL = mActivity.getResources().getString(R.string.ROOTURL);
            String imageUrl = rootURL + "/getImage?imageID=" + imageID;
            Picasso.get().load(imageUrl).into(imageView);

        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return listingItems.size();
    }


    public void deleteItem(Integer position) {
        listingItems.remove((int) position);

        // notify data changed
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, listingItems.size());
    }

    public void setListingItems(List<ListingItem> items) {
        this.listingItems = items;
        notifyDataSetChanged();
    }

    public interface OnLongPressListener {
        void onLongPress(ListingItem listingItem);
    }


    // define interfaces for listeners
    public interface OnImageUploadRequestListener {
        void onImageUploadRequest(int itemPos, int imagePos);
    }

    // holds fields for each item in the recyclerview
    class ListingItemHolder extends RecyclerView.ViewHolder {
        private CardView itemLayout;
        private EditText TextEditTitle;
        private EditText TextEditDescription;
        private ImageView itemImage;
        private ImageButton addItemButton;

        public ListingItemHolder(@NonNull View itemView) {
            super(itemView);

            // get references to the layout objects
            itemLayout = itemView.findViewById(R.id.listing_item_edit_title_CardView);
            itemImage = itemView.findViewById(R.id.listing_item_edit_imageView);
            TextEditTitle = itemView.findViewById(R.id.listing_item_edit_title_editText);
            TextEditDescription = itemView.findViewById(R.id.listing_item_edit_body_editText);
            addItemButton = itemView.findViewById(R.id.addNewItemButton);
        }

    }


}
