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

    private List<Integer> fragmentsToConfirmLeave = Arrays.asList(R.id.createListingFragment,R.id.createUserFragment);
    private List<Integer> topLevelFragments = Arrays.asList(R.id.createListingFragment,R.id.createUserFragment,R.id.singleListingFragment, R.id.accountMenuFragment);
    private Boolean userConfirmedLeave;

    private Integer currFragmentId;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);



        init();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        final NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        // check if user has confirmed leaving the create listing fragment
        if (userIsLeaving(navController,fragmentsToConfirmLeave)) {
            if (!userConfirmedLeave){
                createMenuNavigationAlert(menuItem);
                return true;
            }
        }

        handleMenuItemPress(menuItem);
        return true;
    }

    private void handleMenuItemPress(MenuItem menuItem){
        // handles drawer menu items being selected
        if (isValidDestination(menuItem.getItemId())){
            switch (menuItem.getItemId()) {

                case (R.id.nav_to_home):{
                    // clear backstack on navigation to home
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.main_nav_graph,true).build();

                    Navigation.findNavController(this,R.id.nav_host_fragment)
                            .navigate(R.id.homeFragment2,null, navOptions);
                    break;
                }

                case (R.id.nav_to_account):{
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.accountMenuFragment);
                    break;
                }
                case R.id.nav_to_create_listing:{
                    if (UserInfo.getIsLoggedIn(getApplication())){
                        Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.createListingFragment);
                    } else {
                        Toast.makeText(this, "You need to be logged in to create a listing", Toast.LENGTH_SHORT).show();
                    }

                    break;
                }

                case R.id.nav_to_log_in:{
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.logInFragment);
                }

            }}

        menuItem.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void init(){
        // initialises navController and drawerLayour with navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);
        NavigationUI.setupWithNavController(navigationView,navController);
        navigationView.setNavigationItemSelectedListener(this);

        userConfirmedLeave = false;
    }

    private boolean isValidDestination(int Destination){
      return Destination != Navigation.findNavController(this,R.id.nav_host_fragment)
              .getCurrentDestination().getId();
    }

    @Override
    public boolean onSupportNavigateUp() {

        final NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        // checks if the user is trying to leave an unsafe fragment
        if (userIsLeaving(navController,fragmentsToConfirmLeave)
                &&
                userIsLeaving(navController,topLevelFragments)) {
            createHomeNavigateAlert(navController);
            return true;
        } else if (userIsLeaving(navController,topLevelFragments)){
            navController.navigate(R.id.homeFragment2);
            return true;
        } else if (userIsLeaving(navController,fragmentsToConfirmLeave)){
            createBackNavigateAlert(navController);
            return true;
        } else {
            return NavigationUI.navigateUp(navController,drawerLayout);
        }
    }

    private void navigateUp(NavController navController){
        NavigationUI.navigateUp(navController,drawerLayout);
    }

    private void createBackNavigateAlert(final NavController navcontroller){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setMessage(R.string.listing_creation_exit_dialouge)
                .setCancelable(false);

        alert.setNegativeButton("Discard", (dialog, which) -> navigateUp(navcontroller)).setPositiveButtonIcon(getResources()
                .getDrawable(R.drawable.ic_check_white_24dp));

        alert.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel()).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

        alert.show();
    }

    private void createHomeNavigateAlert(final NavController navcontroller){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setMessage(R.string.listing_creation_exit_dialouge)
                .setCancelable(false);

        alert.setNegativeButton("Discard", (dialog, which) -> navcontroller.navigate(R.id.homeFragment2)).setPositiveButtonIcon(getResources()
                .getDrawable(R.drawable.ic_check_white_24dp));

        alert.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel()).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

        alert.show();
    }

    private void createMenuNavigationAlert(final MenuItem menuItem){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setMessage(R.string.listing_creation_exit_dialouge)
                .setCancelable(false);

        alert.setNegativeButton("Discard", (dialog, which) -> {
            userConfirmedLeave = true;
            onNavigationItemSelected(menuItem);

        }).setPositiveButtonIcon(getResources()
                .getDrawable(R.drawable.ic_check_white_24dp));

        alert.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel()).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

        alert.show();

    }

    private boolean userIsLeaving(final NavController navController, List list){
        currFragmentId = navController.getCurrentDestination().getId();
        System.out.println("user is leaving " + currFragmentId);
        System.out.println("listing frag id  = " + R.id.createListingFragment);
        return list.contains(currFragmentId);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
