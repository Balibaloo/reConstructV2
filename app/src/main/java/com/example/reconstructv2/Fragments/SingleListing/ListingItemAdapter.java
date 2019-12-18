package com.example.reconstructv2.Fragments.SingleListing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListingItemAdapter extends RecyclerView.Adapter<ListingItemAdapter.ListingItemHolder>{
    private List<ListingItem> listingItems = new ArrayList<>();
    private OnClickListener listener;

    private Context mContext;


    public ListingItemAdapter(Context context){
        this.mContext = context;
    }


    @NonNull
    @Override
    public ListingItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        //Creates and returns a ListingHolder, the layout used in the recycler view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_item_card_small, parent, false);
        return new ListingItemHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ListingItemHolder holder, int position){

        ListingItem currItem = listingItems.get(position);

        String rootURL = mContext.getResources().getString(R.string.ROOTURL);

        String imageUrl = rootURL + "/getImage?imageID=" + currItem.getImageIDArray().get(0);
        Picasso.get().load(imageUrl).into(holder.itemImage);
        holder.TextViewname.setText(currItem.getName());
        holder.TextViewdescription.setText(currItem.getDescription());
    }

    @Override
    public int getItemCount()  {return listingItems.size();}

    public void setListingItems(List<ListingItem> items){
        this.listingItems =  items;
        notifyDataSetChanged();
    }

    class ListingItemHolder extends RecyclerView.ViewHolder {
        private TextView TextViewname;
        private TextView TextViewdescription;
        private ImageView itemImage;

        public ListingItemHolder(@NonNull View itemView){
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_imageView);
            TextViewname = itemView.findViewById(R.id.item_nameTextView);
            TextViewdescription = itemView.findViewById(R.id.item_bodyTextView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(listingItems.get(pos));
                    }
                }
            });
        }
    }


    public interface OnClickListener{
        void onItemClick(ListingItem listingItem);
    }


    public void setOnItemCLickListener(OnClickListener listener){this.listener = listener;}

}
