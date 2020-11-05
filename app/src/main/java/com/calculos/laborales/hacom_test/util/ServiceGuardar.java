package com.calculos.laborales.hacom_test.util;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.calculos.laborales.hacom_test.R;
import com.calculos.laborales.hacom_test.local.tables.CoordenadasTbl;
import com.calculos.laborales.hacom_test.mvvm.model.CoordenadasRepository;
import com.calculos.laborales.hacom_test.mvvm.view.MainActivity;
import com.calculos.laborales.hacom_test.mvvm.viewmodel.genViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

public class ServiceGuardar extends Service {

    private CoordenadasRepository repository;
    private Util util;
    private NetworkChangeReceiver mNetworkReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        repository = new CoordenadasRepository();
        util = new Util();

        mNetworkReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        crearNotifi();
    }

    public void crearNotifi(){
        if (Build.VERSION.SDK_INT >= 26) {
            String NOTIFICATION_CHANNEL_ID = "cnn";
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID, "********", NotificationManager.IMPORTANCE_DEFAULT));
            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setContentTitle(getString(R.string.app_name))
                    .setPriority(NotificationManager.IMPORTANCE_NONE)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(4, notification);
        }else{
            startForeground(3, new Notification());
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("DMA_LECTOR", " servicio iniciado");
        fnStartLocalization();
        return START_STICKY; // PARA QUE EL SERVICIO NO SE ELIMINE

    }


    public void fnStartLocalization(){
        if(Util.sharedPref.getString("ubicacion", "").equals("")){
            SharedPreferences.Editor edit = Util.sharedPref.edit();
            edit.putString("ubicacion", "OK");
            edit.apply();

            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            UbicationLocalization objLoca = new UbicationLocalization();
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.MIN_TIEMPO, 0, objLoca);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent(this, RestartReceiver.class);
        unregisterReceiver(mNetworkReceiver);// DESACTIVAR
        sendBroadcast(broadcastIntent);
    }

    public class UbicationLocalization implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("DMA_LECTOR", " actualización de ubicación");
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            CoordenadasTbl obj = new CoordenadasTbl(latitude, longitude, util.getFecha());
            repository.guardarCorrdenadas(obj);
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
}
