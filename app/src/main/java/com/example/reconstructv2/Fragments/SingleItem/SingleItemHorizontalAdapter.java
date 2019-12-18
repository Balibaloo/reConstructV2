package com.example.reconstructv2.Fragments.SingleItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconstructv2.Fragments.SingleListing.ListingItemAdapter;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;

import java.util.ArrayList;
import java.util.List;

public class SingleItemHorizontalAdapter extends RecyclerView.Adapter<SingleItemHorizontalAdapter.ItemHolder> {
    private List<ListingItem> listingItems = new ArrayList<>();
    private OnClickListener listener;

    private Context mContext;

    private TextView titleTextView;
    private ImageView itemImageView;
    private TextView bodyTextView;

    public SingleItemHorizontalAdapter(Context context){
        this.mContext = context;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        //Creates and returns a ListingHolder, the layout used in the recycler view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_item_card_full, parent, false);
        return new SingleItemHorizontalAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        ListingItem currItem = listingItems.get(position);
        titleTextView.setText(currItem.getName());

        bodyTextView.setText(currItem.getDescription());
        // set data on views
    }

    @Override
    public int getItemCount() {
        return listingItems.size();
    }

    public void setListingItems(List<ListingItem> items){
        this.listingItems =  items;
        notifyDataSetChanged();
    }

    class ItemHolder extends RecyclerView.ViewHolder {


        public ItemHolder(@NonNull View itemView){
            super(itemView);
            titleTextView = itemView.findViewById(R.id.itemCardFullTitleTextView);
            itemImageView = itemView.findViewById(R.id.itemCardFullImageView);
            bodyTextView = itemView.findViewById(R.id.itemCardFullBodyTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        /// do somethjing ig
                    }
                }
            });
        }
    }

    public interface OnClickListener{
        void onItemClick(ListingItem listingItem);
    }

    public void setOnItemCLickListener(SingleItemHorizontalAdapter.OnClickListener listener){this.listener = listener;}

}
