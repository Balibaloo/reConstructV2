package com.example.reconstructv2.Fragments.AccountView;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragment;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Models.User;
import com.example.reconstructv2.R;


public class AccountViewFragment extends Fragment {

    private AccountViewFragment.OnFragmentInteractionListener mListener;
    private AccountViewModel accountViewModel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayout;

    private TextView textView;

    private EditText editTextUsername;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;

    private Button buttonRefreshText;

    public AccountViewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_view,container,false);
    }


    @Override
    public  void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
        final View view = getView();

        textView = view.findViewById(R.id.textViewAccViewUserToken);

        swipeRefreshLayout = view.findViewById(R.id.accountViewRefreshLayout);
        constraintLayout = view.findViewById(R.id.accountViewContainer);
        editTextUsername = view.findViewById(R.id.editTextAccViewUsername);
        editTextFirstName = view.findViewById(R.id.editTextAccViewFirstName);
        editTextLastName = view.findViewById(R.id.editTextAccViewLastName);
        editTextEmail = view.findViewById(R.id.editTextAccViewEmail);
        editTextPhoneNumber = view.findViewById(R.id.editTextAccViewPhoneNumber);

        buttonRefreshText = view.findViewById(R.id.textRefreshButton);

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);



        System.out.println("UserID = "+UserInfo.getSelfUserID(getContext()));
        System.out.println("UserToken = "+UserInfo.getToken(getContext()));

        refreshData();

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getActivity());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        buttonRefreshText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("Please log In");
            }
        });

    }

    private void refreshData(){
        if (UserInfo.getSelfUserID(getContext()) != null){
            accountViewModel.getUserRequest(UserInfo.getSelfUserID(getContext()));
            accountViewModel.getUserAPIResponse().observe(this, new Observer<UserAPIResponse>() {
                @Override
                public void onChanged(UserAPIResponse userAPIResponse) {
                    User userObject = userAPIResponse.getUserProfile();
                    System.out.println(userObject.getUsername());
                    swipeRefreshLayout.setRefreshing(false);
                    displayUser(userObject);

                }
            });
        } else {
            textView.setText("Please log In");
        }
    }

    private void displayUser(User user){
        editTextUsername.setText(user.getUsername());
        editTextFirstName.setText(user.getFirst_name());
        editTextLastName.setText(user.getLast_name());
        editTextEmail.setText(user.getEmail());
        editTextPhoneNumber.setText(user.getPhone().toString());
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
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
        void onFragmentInteraction(Uri uri);
    }
}
