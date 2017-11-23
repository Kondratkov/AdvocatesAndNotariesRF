package kondratkov.advocatesandnotariesrf.maps;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import kondratkov.advocatesandnotariesrf.IN;
import kondratkov.advocatesandnotariesrf.R;

/**
 * Created by xaratyan on 27.02.2017.
 */

public class LocationListenerGPServices implements LocationListener {

    public int i = -1;
    LocationManager lm;
    Context context;
    public IN in;
    private static final String TAG = LocationListenerGPServices.class.getName();

    public void start(LocationManager lm, Context context) {
        //LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.lm = lm;
        this.context = context;
        in = new IN();
        Criteria criteria = new Criteria();
        String bestProvider = lm.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //lm.requestLocationUpdates(bestProvider, 0, 0, this);
        // lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
        // lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        android.location.Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            Log.d(TAG, "Широта=" + location.getLatitude());
            Log.d(TAG, "Долгота=" + location.getLongitude());

            in.set_latitude(location.getLatitude());
            in.set_longitude(location.getLongitude());

        } catch (NullPointerException e) {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                Log.d(TAG, "Широта=" + location.getLatitude());
                Log.d(TAG, "Долгота=" + location.getLongitude());

                in.set_latitude(location.getLatitude());
                in.set_longitude(location.getLongitude());

            } catch (NullPointerException e1) {
                location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                try {
                    Log.d(TAG, "Широта=" + location.getLatitude());
                    Log.d(TAG, "Долгота=" + location.getLongitude());

                    in.set_latitude(location.getLatitude());
                    in.set_longitude(location.getLongitude());

                } catch (NullPointerException e2) {
                    lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }
            }
        }
    }

    public void start_my(LocationManager lm, Context context) {
        //LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.lm = lm;
        this.context = context;
        in = new IN();
        Criteria criteria = new Criteria();
        String bestProvider = lm.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //lm.requestLocationUpdates(bestProvider, 0, 0, this);
        // lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
        // lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        android.location.Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        try {
            Log.d(TAG, "Широта=" + location.getLatitude());
            Log.d(TAG, "Долгота=" + location.getLongitude());

            in.set_latitude_my(location.getLatitude());
            in.set_longitude_my(location.getLongitude());

        } catch (NullPointerException e) {
            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                Log.d(TAG, "Широта=" + location.getLatitude());
                Log.d(TAG, "Долгота=" + location.getLongitude());

                in.set_latitude_my(location.getLatitude());
                in.set_longitude_my(location.getLongitude());

            } catch (NullPointerException e1) {
                location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                try {
                    Log.d(TAG, "Широта=" + location.getLatitude());
                    Log.d(TAG, "Долгота=" + location.getLongitude());

                    in.set_latitude_my(location.getLatitude());
                    in.set_longitude_my(location.getLongitude());

                } catch (NullPointerException e2) {
                    lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d(TAG, "Широта=" + location.getLatitude());
            in.set_latitude(location.getLatitude());
            in.set_latitude_my(location.getLatitude());

            Log.d(TAG, "Долгота=" + location.getLongitude());
            in.set_longitude(location.getLongitude());
            in.set_longitude_my(location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}


