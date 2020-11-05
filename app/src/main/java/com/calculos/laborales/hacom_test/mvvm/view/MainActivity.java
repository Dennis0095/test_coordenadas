package com.calculos.laborales.hacom_test.mvvm.view;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.calculos.laborales.hacom_test.R;
import com.calculos.laborales.hacom_test.util.Constants;
import com.calculos.laborales.hacom_test.util.ServiceGuardar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private LatLng latLongActual;
    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fnStartLocalization();

        ServiceGuardar service = new ServiceGuardar();
        serviceIntent = new Intent(this, service.getClass());
        if (!verificarServicio(service.getClass())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        setSytleMaps(googleMap);

        LatLng tienda = new LatLng(-12.066289, -77.036838);// -12.066289, -77.036838 LIMA CENTRO
        if(latLongActual == null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(tienda));
        else
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLongActual));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

    }

    private void setSytleMaps(GoogleMap googleMap){
        try {
             googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));
        } catch (Resources.NotFoundException e) { }
    }

    public void fnStartLocalization(){
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        UbicationLocalization objLoca = new UbicationLocalization();
        final boolean gpsEnable = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnable){
            Toast.makeText(this, getString(R.string.activarG), Toast.LENGTH_LONG).show();
        }

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.MIN_TIEMPO_ENTRE_UPDATES, Constants.MIN_CAMBIO_DISTANCIA_PARA_UPDATES,objLoca);
        }

    }

    private boolean verificarServicio(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public class UbicationLocalization implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            latLongActual = new LatLng(location.getLatitude(), location.getLongitude());
            mapFragment.getMapAsync(MainActivity.this::onMapReady);
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

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        super.onDestroy();

    }
}
