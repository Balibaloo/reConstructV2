package com.example.reconstructv2.Fragments.SingleListing.SingleItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SingleItemHorizontalAdapter extends RecyclerView.Adapter<SingleItemHorizontalAdapter.ItemHolder> {
    private List<ListingItem> listingItems = new ArrayList<>();
    private OnLongClickListener listener;

    private Context mContext;



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
        holder.titleTextView.setText(currItem.getName());

        try {
            String rootURL = mContext.getResources().getString(R.string.ROOTURL);
            String imageUrl = rootURL + "/getImage?imageID=" + currItem.getImageIDArray().get(0);
            Picasso.get().load(imageUrl).into(holder.itemImageView);

        } catch (Exception e ){
            System.out.println(e.toString());
        }

        holder.bodyTextView.setText(currItem.getDescription());

        if(!currItem.getAvailable()){
            holder.itemContainer.setBackgroundResource(R.drawable.listing_item_un_available);
        } else if (currItem.getIsSelected()) {
            holder.itemContainer.setBackgroundResource(R.drawable.listing_item_selected);
        } else {
            holder.itemContainer.setBackgroundResource(R.drawable.listing_item_base);
        }


    }


    @Override
    public int getItemCount() {
        return listingItems.size();
    }

    public void setListingItems(List<ListingItem> items){
        this.listingItems =  items;
        notifyDataSetChanged();
    }

    class ItemHolder extends RecyclerView.ViewHolder{

        private CardView itemContainer;
        private TextView titleTextView;
        private ImageView itemImageView;
        private TextView bodyTextView;


        public ItemHolder(@NonNull View itemView){
            super(itemView);
            itemContainer = itemView.findViewById(R.id.itemCardFullCardContainer);
            titleTextView = itemView.findViewById(R.id.itemCardFullTitleTextView);
            itemImageView = itemView.findViewById(R.id.itemCardFullImageView);
            bodyTextView = itemView.findViewById(R.id.itemCardFullBodyTextView);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onLongItemClick(listingItems.get(pos));
                    }

                    return true;
                }
            });
        }
    }

    public interface OnLongClickListener{
        void onLongItemClick(ListingItem listingItem);
    }

    public void setOnLongItemCLickListener(SingleItemHorizontalAdapter.OnLongClickListener listener){this.listener = listener;}

}
