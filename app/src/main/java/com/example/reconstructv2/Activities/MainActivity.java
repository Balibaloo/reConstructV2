package com.example.reconstructv2.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.reconstructv2.Fragments.AccountManagement.AccountMenu.AccountMenuFragment;
import com.example.reconstructv2.Fragments.AccountManagement.RecentlyViewedListings.recentlyViewedListings;
import com.example.reconstructv2.Fragments.AccountManagement.UserListings.userListingsFragment;
import com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.CreateListingFragment;
import com.example.reconstructv2.Fragments.CreateListing.CreateListingMain.ItemListEdit.ItemListEditFragment;
import com.example.reconstructv2.Fragments.CreateListing.createListingLocationFragment;
import com.example.reconstructv2.Fragments.CreateUser.CreateUserFragment;
import com.example.reconstructv2.Fragments.CreateUser.FinishCreateUserFragment;
import com.example.reconstructv2.Fragments.LogIn.LogInFragment;
import com.example.reconstructv2.Fragments.Results.ResultsFragment;
import com.example.reconstructv2.Fragments.SingleItem.SingleItemViewFragment;
import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.R;
import com.example.reconstructv2.Fragments.AccountManagement.AccountView.AccountViewFragment;
import com.example.reconstructv2.Fragments.Home.HomeFragment;
import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        HomeFragment.OnFragmentInteractionListener,
        SingleListingFragment.OnFragmentInteractionListener,
        SingleItemViewFragment.OnFragmentInteractionListener,
        AccountMenuFragment.OnFragmentInteractionListener,
        AccountViewFragment.OnFragmentInteractionListener,
        CreateListingFragment.OnFragmentInteractionListener,
        createListingLocationFragment.OnFragmentInteractionListener,
        ItemListEditFragment.OnFragmentInteractionListener,
        LogInFragment.OnFragmentInteractionListener,
        CreateUserFragment.OnFragmentInteractionListener,
        FinishCreateUserFragment.OnFragmentInteractionListener,
        ResultsFragment.OnFragmentInteractionListener,
        recentlyViewedListings.OnFragmentInteractionListener,
        userListingsFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    // a list of fragments that require a popup dialouge
    // to confirm that the user wants to leave
    // because data may be lost
    private List<Integer> fragmentsToConfirmLeave = 
        Arrays.asList(R.id.createListingFragment,R.id.createUserFragment);
    
    // a list of top level fragments
    // when the back button is pressed
    // the app should navigate to the home fragment
    // as opposed to popping the backstack
    private List<Integer> topLevelFragments = Arrays.asList(R.id.createListingFragment,R.id.createUserFragment,R.id.singleListingFragment, R.id.accountMenuFragment);

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // display the main activity layout

        init();
    }

    private void init(){
        drawerLayout = findViewById(R.id.drawer_layout); // find the reference to the drawer layout

        // find the reference to the navigation menu in the drawer layout
        navigationView = findViewById(R.id.nav_view); 


        // initialises navController and drawerLayour with navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout); 
        NavigationUI.setupWithNavController(navigationView,navController);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        final NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        // check if the user is leaving an unsafe fragment
        if (userIsLeaving(navController,fragmentsToConfirmLeave)) {

            // create an alert to check if the user wants to leave
            createMenuNavigationAlert(menuItem);
            return true;
        }

        // if the user is not leaving an unsafe fragment then handle the navigation
        handleMenuItemPress(menuItem);
        return true;
    }

    // handles drawer menu items being selected
    private void handleMenuItemPress(MenuItem menuItem){
        
        // check that the user is not trying to navigate to a fragment they are already in
        if (isValidDestination(menuItem.getItemId())){

            switch (menuItem.getItemId()) {
                case (R.id.nav_to_home):{
                    // if the user tries to navigate to the home fragment
                    // clear the backstack and navigate to the home fragment
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.main_nav_graph,true).build();

                    Navigation.findNavController(this,R.id.nav_host_fragment)
                            .navigate(R.id.homeFragment2,null, navOptions);
                    break;
                }

                case (R.id.nav_to_account):{
                    // navigate to the account fragment
                    Navigation.findNavController(this,R.id.nav_host_fragment)
                        .navigate(R.id.accountMenuFragment);
                    break;
                }

                case R.id.nav_to_create_listing:{

                    // users must be logged in to create a listing
                    if (UserInfo.getIsLoggedIn(getApplication())){
                        // if the user is logged in
                        // navigate to the fragment
                        Navigation.findNavController(this,R.id.nav_host_fragment)
                            .navigate(R.id.createListingFragment);

                    } else {
                        // if the user is not logged in display a toast
                        Toast.makeText(this, "You need to be logged in to create a listing", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }

                case R.id.nav_to_log_in:{
                    // navigate to the log in fragment
                    Navigation.findNavController(this,R.id.nav_host_fragment)
                        .navigate(R.id.logInFragment);
                }

            }}

        // menuItem.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START); // hide the drawer layout
    }

    
    // check that the user is not trying to navigate to a fragment that theyre already in
    private boolean isValidDestination(int Destination){
        return Destination != Navigation.findNavController(this,R.id.nav_host_fragment)
              .getCurrentDestination().getId();
    }

    @Override
    public boolean onSupportNavigateUp() {
        // when the user presses the back button

        // find the navigation controller
        final NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        // checks if the user is trying to leave an unsafe fragment
        if (userIsLeaving(navController,fragmentsToConfirmLeave)
                &&
                userIsLeaving(navController,topLevelFragments)) {
            // if the user is leaving an unsafe top level fragment
            createHomeNavigateAlert(navController);
            return true;

        } else if (userIsLeaving(navController,topLevelFragments)){
            // if the user is leaving a top level fragment
            // navigate to the home fragment
            navController.navigate(R.id.homeFragment2);
            return true;

        } else if (userIsLeaving(navController,fragmentsToConfirmLeave)){
            // if the user is leaving an unsafe fragment
            createBackNavigateAlert(navController);
            return true;
        } else {
            // pop the backstack
            return NavigationUI.navigateUp(navController,drawerLayout);
        }
    }

    // pop the backstack and navigate back
    private void navigateUp(NavController navController){
        NavigationUI.navigateUp(navController,drawerLayout);
    }

    // create an alert to check if the user wants to navigate back
    private void createBackNavigateAlert(final NavController navcontroller){

        // create new alert
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        // set alert the message
        alert.setMessage(R.string.listing_creation_exit_dialouge)
                .setCancelable(true);

        // if the user wishes to leave, navigate back
        alert.setNegativeButton("Discard", (dialog, which) -> navigateUp(navcontroller)).setPositiveButtonIcon(getResources()
                .getDrawable(R.drawable.ic_check_white_24dp));

        // if the user cancels, hide the allert
        alert.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel()).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

        // show the alert
        alert.show();
    }

    // create an alert to check if the user wants to navigate to the home fragment
    private void createHomeNavigateAlert(final NavController navcontroller){

        // create a new allert
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        // set the alert message
        alert.setMessage(R.string.listing_creation_exit_dialouge)
                .setCancelable(true);

        // if the user wants to leave, navigate to the home fragment
        alert.setNegativeButton("Discard", (dialog, which) -> navcontroller.navigate(R.id.homeFragment2)).setPositiveButtonIcon(getResources()
                .getDrawable(R.drawable.ic_check_white_24dp));

        // if the user calcels, hide the dialouge
        alert.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel()).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

        // hide the allert
        alert.show();
    }

    // create an alert to check if the user wants to navigate to a fragment    
    private void createMenuNavigationAlert(final MenuItem menuItem){

        // create a new alert
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        // set the alert message
        alert.setMessage(R.string.listing_creation_exit_dialouge)
                .setCancelable(false);

        // if the user wants to leave, handle the menu item press
        alert.setNegativeButton("Discard", (dialog, which) -> {
            handleMenuItemPress(menuItem);
        }).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

        // if the user calcels, hide the dialouge
        alert.setPositiveButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        }).setPositiveButtonIcon(getResources().getDrawable(R.drawable.ic_check_white_24dp));

        // show the alert
        alert.show();

    }

    // check if user is leaving a fragment in a list of fragments
    private boolean userIsLeaving(final NavController navController, List fragmentList){

        // get the fragment the user is currently leaving
        Integer currFragmentId = navController.getCurrentDestination().getId();

        // return weather the fragment is in the list
        return fragmentList.contains(currFragmentId);
    }

    // callback method when starting activities for results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
