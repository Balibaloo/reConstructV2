package com.example.reconstructv2.Fragments.CreateListing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class SingleShotLocationProvider {

    public static interface LocationCallback {
        public void onNewLocationAvailable(Location location);
    }

    // calls back to calling thread, note this is for low grain: if you want higher precision, swap the
    // contents of the else and if. Also be sure to check gps permission/settings are allowed.
    // call usually takes <10ms

    public static void requestSingleUpdate(final Context context, final LocationCallback callback) {
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnabled) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager.requestSingleUpdate(criteria, new android.location.LocationListener() {

                @Override
                public void onLocationChanged(Location location) {
                    Toast.makeText(context, "should callback", Toast.LENGTH_SHORT).show();
                    callback.onNewLocationAvailable(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Toast.makeText(context, provider, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Toast.makeText(context, "enabled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Toast.makeText(context, "disabled", Toast.LENGTH_SHORT).show();
                }
        }, null);
        } else {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationManager.requestSingleUpdate(criteria, new android.location.LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        Toast.makeText(context, "should callback", Toast.LENGTH_SHORT).show();
                        callback.onNewLocationAvailable(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Toast.makeText(context, provider, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Toast.makeText(context, "enabled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Toast.makeText(context, "disabled", Toast.LENGTH_SHORT).show();
                    }
                }, null);
            }
        }
    }

}