package com.calculos.laborales.hacom_test.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.calculos.laborales.hacom_test.local.tables.CoordenadasTbl;
import com.calculos.laborales.hacom_test.mvvm.model.CoordenadasRepository;

public class NetworkChangeReceiver extends BroadcastReceiver {

    CoordenadasRepository repository;

    public NetworkChangeReceiver() {
        if(repository == null)
            repository = new CoordenadasRepository();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DMA_LECTOR", " actualización");
        if(Util.HayConexionaInternet(context)){
            for(CoordenadasTbl obj: repository.listarCoordenadas()){
                repository.enviarCorrdenadasOffline(obj);
            }
        }
    }
}
