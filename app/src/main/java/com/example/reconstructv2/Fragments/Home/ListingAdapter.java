package com.example.reconstructv2.Fragments.Home;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingHolder> {
    private Context mContext;
    private ArrayList<Listing> mListingList;

    private List<Listing> listings = new ArrayList<>();
    private OnClickListener listener;
    private OnBottomReachedListener onBottomReachedListener;

    public ListingAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public ListingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Creates and returns a ListingHolder, the layout used in the recycler view

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listing_card, parent, false);
        return new ListingHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ListingHolder holder, int position) {

        // if the last view is being loaded, call on bottom reached
        if (position == listings.size() -1 ){
            onBottomReachedListener.onBottomReached();
        }

        Listing currListing = listings.get(position);

        // puts data from listing object into ListingHolder views

        holder.textViewTitle.setText(currListing.getTitle());
        holder.textViewBody.setText(currListing.getBody());


        // set item status colour
        if (!currListing.getIsActive()){
            holder.container.setBackgroundResource(R.drawable.listing_item_un_available);
        } else {
            holder.container.setBackgroundResource(R.drawable.listing_item_base);
        }

        loadImageInto(holder.listingImage,currListing.getMainImageID());
    }

    // load an image id into an image view
    private void loadImageInto(ImageView imageView, String imageID) {
        try {
            String rootURL = mContext.getResources().getString(R.string.ROOTURL);

            String imageUrl = rootURL + "/getImage?imageID=" + imageID;
            Picasso.get().load(imageUrl).into(imageView);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public void addListings(List<Listing> listings) {
        this.listings.addAll(listings);

        notifyDataSetChanged();
    }

    public void initListings(){
        this.listings = new ArrayList<>();
        notifyDataSetChanged();

    }
    class ListingHolder extends RecyclerView.ViewHolder {
        // holds listing layout views

        private TextView textViewTitle;
        private TextView textViewBody;
        private ImageView listingImage;
        private RelativeLayout container;

        public ListingHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewBody = itemView.findViewById(R.id.text_view_body);
            listingImage = itemView.findViewById(R.id.imageViewListing);
            container = itemView.findViewById(R.id.listing_card_container);

            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    listener.onItemClick(listings.get(pos));
                }
            });
        }
    }

    public interface OnClickListener {
        void onItemClick(Listing listing);
    }

    public interface OnBottomReachedListener{
        void onBottomReached();
    }

    public void setOnItemClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }
}
