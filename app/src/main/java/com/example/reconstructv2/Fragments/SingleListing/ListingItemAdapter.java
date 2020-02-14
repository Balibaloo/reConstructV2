package com.example.reconstructv2.Fragments.SingleListing;

import android.content.Context;
import android.media.Image;
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

public class ListingItemAdapter extends RecyclerView.Adapter<ListingItemAdapter.ListingItemHolder>{
    private List<ListingItem> listingItems = new ArrayList<>();
    private OnClickListener listener;
    private OnLongPressListener longClickListener;

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
        try {
            loadImageInto(holder.itemImage,currItem.getImageIDArray().get(0));
        } catch (Exception e){
            System.out.println(e.toString());
        }
        holder.TextViewname.setText(currItem.getName());
        holder.TextViewdescription.setText(currItem.getDescription());


        if(!currItem.getAvailable()){
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_un_available);
        } else if (currItem.getIsSelected()) {
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_selected);
        } else {
            holder.itemLayout.setBackgroundResource(R.drawable.listing_item_base);
        }

    }

    private void loadImageInto(ImageView iamgeView,String imageID){
        try{
            String rootURL = mContext.getResources().getString(R.string.ROOTURL);

            String imageUrl = rootURL + "/getImage?imageID=" + imageID;
            Picasso.get().load(imageUrl).into(iamgeView);
        } catch (Exception e){
            System.out.println(e.toString());
        }

    }

    @Override
    public int getItemCount()  {return listingItems.size();}

    public void setListingItems(List<ListingItem> items){
        this.listingItems =  items;
        notifyDataSetChanged();
    }

    public void addListingItem(ListingItem item){
        this.listingItems.add(item);
        notifyDataSetChanged();
    }

    class ListingItemHolder extends RecyclerView.ViewHolder {
        private CardView itemLayout;
        private TextView TextViewname;
        private TextView TextViewdescription;
        private ImageView itemImage;

        public ListingItemHolder(@NonNull View itemView){
            super(itemView);
            itemLayout = itemView.findViewById(R.id.item_cardView);
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
        }

    }

    public interface OnClickListener{
        void onItemClick(ListingItem listingItem);
    }

    public interface OnLongPressListener{
        void onLongPress(ListingItem listingItem);
    }


    public void setOnItemCLickListener(OnClickListener listener){this.listener = listener;}

    public void setLongClickListener(OnLongPressListener longClickListener) {
        this.longClickListener = longClickListener;
    }

}
