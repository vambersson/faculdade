package br.com.vambersson.portalparatodos.map;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class Localizacao implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


        private static long LOCATION_INTERVAL = 2000;

        private GoogleApiClient googleApiClient;
        private LocationRequest locationRequest;
        private FusedLocationProviderApi fusedLocationProviderApi;
        private LocationListener locationListener;

    public Localizacao(Context context, LocationListener locationListener) {
            this.locationListener = locationListener;

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(LOCATION_INTERVAL);
            locationRequest.setFastestInterval(LOCATION_INTERVAL);

            fusedLocationProviderApi = LocationServices.FusedLocationApi;

            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            onStart();
        }

    public void onStart(){
        googleApiClient.connect();
    }

    public void onStop(){
        if(googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try{
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
        }catch (SecurityException ex){ }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
