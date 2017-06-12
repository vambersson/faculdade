package br.com.vambersson.portalparatodos.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.List;

import br.com.vambersson.portalparatodos.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,
        Rotas.OnRotaCriadaListner{

    private GoogleMap mMap;
    private LatLng localAtual;
    private LatLng prefeitura;
    private Marker marcador;
    private Localizacao localizacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onLocationChanged(Location location) {

        localAtual = new LatLng(location.getLatitude(),location.getLongitude());

        if(marcador != null){
            marcador.setPosition(localAtual);
        }else{
            marcador = mMap.addMarker(new MarkerOptions().position(localAtual).title("Local Atual"));
        }

        new Rotas(this, prefeitura, localAtual).execute();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        localizacao = new Localizacao(this, this);

        prefeitura = new LatLng(-8.0555904,-34.8723802);
        mMap.addMarker(new MarkerOptions().position(prefeitura).title("Prefeitura"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(prefeitura, 20.0f));

    }

    @Override
    public void rotaCriada(List<LatLng> rotas) {

        if(rotas != null && rotas.size() > 0){
            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(rotas)
                    .width(5)
                    .color(Color.RED)
                    .visible(true);

            mMap.addPolyline(polylineOptions);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(localAtual));
    }
}
