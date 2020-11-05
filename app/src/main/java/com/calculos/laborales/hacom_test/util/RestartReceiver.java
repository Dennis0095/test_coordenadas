package com.calculos.laborales.hacom_test.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class RestartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("DMA_LECTOR", "------------------ RestartReceiver");

        context.startService(new Intent(context, ServiceGuardar.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, ServiceGuardar.class));
        } else {
            context.startService(new Intent(context, ServiceGuardar.class));
        }

    }
}
