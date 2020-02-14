package com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.ItemListEdit;

import android.app.Activity;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconstructv2.Helpers.InputValidator;
import com.example.reconstructv2.Helpers.KeyboardHelper;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

public class EditListingItemAdapter extends RecyclerView.Adapter<EditListingItemAdapter.ListingItemHolder>{
    private List<ListingItem> listingItems = new ArrayList<>();
    private EditListingItemAdapter.OnClickListener listener;
    private EditListingItemAdapter.OnLongPressListener longClickListener;

    private OnEditTextChanged onEditTextChanged;
    private OnImageUploadRequest onImageUploadRequest;
    private Activity mActivity;

    public EditListingItemAdapter(Activity activity, OnEditTextChanged onEditTextChanged, OnImageUploadRequest onImageUploadRequest){
        this.mActivity = activity;
        this.onEditTextChanged = onEditTextChanged;
        this.onImageUploadRequest = onImageUploadRequest;
    }

    @NonNull
    @Override
    public EditListingItemAdapter.ListingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        //Creates and returns a ListingHolder, the layout used in the recycler view

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_item_card_full_edit, parent, false);

        return new EditListingItemAdapter.ListingItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EditListingItemAdapter.ListingItemHolder holder, final int position){

        ListingItem currItem = listingItems.get(position);

        if (currItem.getImageIDArray().isEmpty()){
            holder.itemImage.setImageResource(R.drawable.ic_default_image);
        } else {
            loadImageInto(holder.itemImage,currItem.getImageIDArray().get(0));
        }

        holder.TextEditTitle.setText(currItem.getName());
        holder.TextEditDescription.setText(currItem.getDescription());

        InputValidator.validateItemText(holder.TextEditTitle,"name");
        InputValidator.validateItemText(holder.TextEditDescription,"description");


        if (position == listingItems.size()-1){
            holder.addItemButton.setVisibility(View.VISIBLE);
        } else {
            holder.addItemButton.setVisibility(View.GONE);
        }

        if(!currItem.getAvailable()){
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_un_available);
        } else if (currItem.getIsSelected()) {
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_selected);
        } else {
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_base);
        }

        holder.TextEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onEditTextChanged.onTextChanged(position,s.toString(),"title");
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
                onEditTextChanged.onTextChanged(position,s.toString(),"body");
            }
        });


        holder.itemImage.setOnClickListener(v -> onImageUploadRequest.onImageUploadRequest(position,0));

        holder.itemLayout.setOnClickListener(v -> KeyboardHelper.hideSoftKeyboard(mActivity));

    }

    private void loadImageInto(ImageView iamgeView, String imageID){
        try{
            String rootURL = mActivity.getResources().getString(R.string.ROOTURL);

            String imageUrl = rootURL + "/getImage?imageID=" + imageID;
            Picasso.get().load(imageUrl).into(iamgeView);
        }catch (Exception e){
            System.out.println(e.toString());
        }

    }

    @Override
    public int getItemCount()  {return listingItems.size();}

    public void setListingItems(List<ListingItem> items){
        this.listingItems =  items;
        notifyDataSetChanged();
    }

    public List<ListingItem> getListingItems() {
        return listingItems;
    }

    public void addListingItem(ListingItem item){
        this.listingItems.add(item);
        notifyDataSetChanged();
    }

    class ListingItemHolder extends RecyclerView.ViewHolder {
        private CardView itemLayout;
        private EditText TextEditTitle;
        private EditText TextEditDescription;
        private ImageView itemImage;
        private ImageButton addItemButton;

        public ListingItemHolder(@NonNull View itemView){
            super(itemView);
            itemLayout = itemView.findViewById(R.id.listing_item_edit_title_CardView);
            itemImage = itemView.findViewById(R.id.listing_item_edit_imageView);
            TextEditTitle = itemView.findViewById(R.id.listing_item_edit_title_editText);
            TextEditDescription = itemView.findViewById(R.id.listing_item_edit_body_editText);
            addItemButton = itemView.findViewById(R.id.addNewItemButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(listingItems.get(pos));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if (longClickListener != null && pos != RecyclerView.NO_POSITION) {
                        longClickListener.onLongPress(listingItems.get(pos));
                    }

                    return true;
                }
            });

            addItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listingItems.add(new ListingItem(
                            "temporary id",
                            null,
                            null,
                            null,
                            null,
                            null
                    ));
                    notifyDataSetChanged();
                }
            });

        }

    }

    public interface OnEditTextChanged {
        void onTextChanged(int position, String charSeq,String field);
    }

    public interface OnClickListener{
        void onItemClick(ListingItem listingItem);
    }

    public interface OnLongPressListener{
        void onLongPress(ListingItem listingItem);
    }

    public interface OnImageUploadRequest{
        void onImageUploadRequest(int itemPos,int imagePos);
    }

    public void setOnItemCLickListener(EditListingItemAdapter.OnClickListener listener){this.listener = listener;}

    public void setLongClickListener(EditListingItemAdapter.OnLongPressListener longClickListener) {
        this.longClickListener = longClickListener;
    }


}
