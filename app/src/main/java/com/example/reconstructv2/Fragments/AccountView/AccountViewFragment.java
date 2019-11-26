package com.example.reconstructv2.Fragments.AccountView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.reconstructv2.Fragments.Home.HomeViewModel;
import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragment;
import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.R;


public class AccountViewFragment extends Fragment {

    private AccountViewFragment.OnFragmentInteractionListener mListener;

    private AccountViewModel accountViewModel;

    private TextView textView;

    public AccountViewFragment() {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_view,container,false);
    }


    @Override
    public  void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        textView = view.findViewById(R.id.textView);

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);

        accountViewModel.getFrontPageListings();
        accountViewModel.getAPIListingResponse().observe(this, new Observer<ListingListAPIResponse>() {
            @Override
            public void onChanged(ListingListAPIResponse listingListAPIResponse) {
                Listing firstListing = listingListAPIResponse.getListings().get(0);
                textView.setText(firstListing.getTitle());
                System.out.println("LISTING ID === "+firstListing.getListingID());
            }

        });

        System.out.println("WHOOO HOOO in account frag");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SingleListingFragment.OnFragmentInteractionListener) {
            mListener = (AccountViewFragment.OnFragmentInteractionListener) context;
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
        void onAccountViewFragmentInteraction(Uri uri);
    }
}
