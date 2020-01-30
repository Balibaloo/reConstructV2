package com.example.reconstructv2.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.reconstructv2.Fragments.CreateListing.CreateListingFragment;
import com.example.reconstructv2.Fragments.CreateListing.ItemListEditFragment;
import com.example.reconstructv2.Fragments.CreateUser.CreateUserFragment;
import com.example.reconstructv2.Fragments.CreateUser.FinishCreateUserFragment;
import com.example.reconstructv2.Fragments.LogIn.LogInFragment;
import com.example.reconstructv2.Fragments.Results.ResultsFragment;
import com.example.reconstructv2.Fragments.SingleItem.SingleItemViewFragment;
import com.example.reconstructv2.R;
import com.example.reconstructv2.Fragments.AccountView.AccountViewFragment;
import com.example.reconstructv2.Fragments.Home.HomeFragment;
import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragment;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements
        HomeFragment.OnFragmentInteractionListener,
        SingleListingFragment.OnFragmentInteractionListener,
        SingleItemViewFragment.OnFragmentInteractionListener,
        AccountViewFragment.OnFragmentInteractionListener,
        CreateListingFragment.OnFragmentInteractionListener,
        ItemListEditFragment.OnFragmentInteractionListener,
        LogInFragment.OnFragmentInteractionListener,
        CreateUserFragment.OnFragmentInteractionListener,
        FinishCreateUserFragment.OnFragmentInteractionListener,
        ResultsFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private List<Integer> fragmentsToConfirmLeave = Arrays.asList(R.id.createListingFragment,R.id.createUserFragment);
    private Boolean userConfirmedLeave;

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
        if (userIsLeavingUnsafeFragment(navController)) {
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
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.accountViewFragment);
                    break;
                }
                case R.id.nav_to_create_listing:{
                    Navigation.findNavController(this,R.id.nav_host_fragment).navigate(R.id.createListingFragment);
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
        if (userIsLeavingUnsafeFragment(navController)) {
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

        alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                navigateUp(navcontroller);

            }}).setPositiveButtonIcon(getResources()
                .getDrawable(R.drawable.ic_check_white_24dp));

        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }}).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

        alert.show();
    }

    private void createMenuNavigationAlert(final MenuItem menuItem){
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setMessage(R.string.listing_creation_exit_dialouge)
                .setCancelable(false);

        alert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userConfirmedLeave = true;
                onNavigationItemSelected(menuItem);

            }}).setPositiveButtonIcon(getResources()
                .getDrawable(R.drawable.ic_check_white_24dp));

        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }}).setNegativeButtonIcon(getResources().getDrawable(R.drawable.ic_cross_red_24dp));

        alert.show();

    }

    private boolean userIsLeavingUnsafeFragment(final NavController navController){
        Integer currFragmentId = navController.getCurrentDestination().getId();
        return fragmentsToConfirmLeave.contains(currFragmentId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
