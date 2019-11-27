package com.example.reconstructv2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.reconstructv2.Fragments.CreateListing.CreateListingFragment;
import com.example.reconstructv2.Fragments.LogIn.LogInFragment;
import com.example.reconstructv2.R;
import com.example.reconstructv2.Fragments.AccountView.AccountViewFragment;
import com.example.reconstructv2.Fragments.Home.HomeFragment;
import com.example.reconstructv2.Fragments.SingleListing.SingleListingFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements
        HomeFragment.OnFragmentInteractionListener,
        SingleListingFragment.OnFragmentInteractionListener,
        AccountViewFragment.OnFragmentInteractionListener,
        CreateListingFragment.OnFragmentInteractionListener,
        LogInFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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
        return true;
    }

    private void init(){
        // initialises navController and drawerLayour with navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);
        NavigationUI.setupWithNavController(navigationView,navController);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean isValidDestination(int Destination){
      return Destination != Navigation.findNavController(this,R.id.nav_host_fragment)
              .getCurrentDestination().getId();
    };

    @Override
    public boolean onSupportNavigateUp() {
        //
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.nav_host_fragment),drawerLayout);
    }


    @Override
    public void onHomeFragmentInteraction(Uri uri) {
        // Do stuff
    }

    @Override
    public void onSingleListingFragmentInteraction(Uri uri) {
        // Do different stuff
    }

    @Override
    public void onAccountViewFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCreateListingFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
