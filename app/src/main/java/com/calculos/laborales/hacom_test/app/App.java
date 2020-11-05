package com.calculos.laborales.hacom_test.app;

import android.app.Application;
import android.content.Context;

import com.calculos.laborales.hacom_test.local.db.InstanceDB;
import com.calculos.laborales.hacom_test.util.Util;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        InstanceDB.setInstance(getApplicationContext());
        Util.instanciarRetrofit();
        Util.instanciaShardPreference(getApplicationContext().getSharedPreferences("stored", Context.MODE_PRIVATE));
    }
}
