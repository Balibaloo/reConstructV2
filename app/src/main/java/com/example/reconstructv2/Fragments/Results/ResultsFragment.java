package com.example.reconstructv2.Fragments.Results;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.CreateListingFragmentDirections;
import com.example.reconstructv2.R;


public class ResultsFragment extends Fragment {


    private Button toHomeButton;
    private Button retryButton;
    private TextView textView;

    private OnFragmentInteractionListener mListener;

    public ResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view = getView();


        initViews(view);

        textView.setText(ResultsFragmentArgs.fromBundle(getArguments()).getMessage());

        // if the previous operation was successful, hide the retry button
        if (ResultsFragmentArgs.fromBundle(getArguments()).getIsSuccess()){
            retryButton.setVisibility(View.INVISIBLE);


        } else {
            // show the retry button
            retryButton.setVisibility(View.VISIBLE);

            retryButton.setOnClickListener(v -> {

                // get bundle arguments
                ResultsFragmentArgs arguments = ResultsFragmentArgs.fromBundle(getArguments());

                Integer retryDestination = arguments.getRetryDestination();


                switch (retryDestination) {
                    case (R.id.createListingFragment):{

                        // if the retry destination is the create listing fragment
                        // add the listing argument from the temporary listing field
                        // this is so that if the request failed, the user can continue editing their listing

                        ResultsFragmentDirections.ActionResultsFragmentToCreateListingFragment action = ResultsFragmentDirections.actionResultsFragmentToCreateListingFragment();
                        action.setListingArgument(arguments.getTemporaryListing());
                        Navigation.findNavController(view).navigate(action);

                        break;

                    } default:{
                        Navigation.findNavController(view).navigate(retryDestination);
                    }

                }

            });}

        // navigate home
        toHomeButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.homeFragment2));
    }

    private void initViews(View view) {
        toHomeButton = view.findViewById(R.id.toHomeButton);
        retryButton = view.findViewById(R.id.retryButton);
        textView = view.findViewById(R.id.resultTextView);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        void onFragmentInteraction(Uri uri);
    }
}
