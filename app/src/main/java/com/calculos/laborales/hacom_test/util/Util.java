package com.calculos.laborales.hacom_test.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ProgressBar;

import com.calculos.laborales.hacom_test.local.entity.ErrorEntity;
import com.calculos.laborales.hacom_test.mvvm.model.Services;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.constraintlayout.widget.ConstraintLayout;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
    private Context ctx;
    public static Retrofit retrofit;
    public static Services services;
    public static SharedPreferences sharedPref;


    public Util(Context ctx) {
        this.ctx = ctx;
    }
    public Util(){
    }

    public void colorProgressbar(ProgressBar progressBar, int color){
        progressBar.getIndeterminateDrawable().setColorFilter(ctx.getResources().getColor(color), android.graphics.PorterDuff.Mode.SRC_IN);

    }


    public static void instanciarRetrofit(){
        if(retrofit == null && services ==null){
            retrofit = fnRetrofit();
            services = retrofit.create(Services.class);
        }

    }

    public static Retrofit fnRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Constants.URI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(Util.timeoutInternet())
                .build();
    }


    public static void instanciaShardPreference(SharedPreferences sharedPreferences){
        if(sharedPref == null){
            sharedPref = sharedPreferences;
        }
    }

    public static OkHttpClient timeoutInternet(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("Content-Type","application/x-www-form-urlencoded")
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
        return okHttpClient;
    }

    public ErrorEntity construirErrorFailure(){
        return new ErrorEntity(Constants.CODE_ERROR_HTTP, Constants.MESSAGE_RESPOSE_ERROR, Constants.CODE_ERROR_HTTP);
    }

    public JsonObject statusResponse(retrofit2.Response<Object> response){
        LinkedTreeMap tree = (LinkedTreeMap) response.body();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(tree).getAsJsonObject();
        //return jsonObject.get("code").getAsString();
        return jsonObject;
    }

    public JsonArray formatearRespuesta(retrofit2.Response<Object> response){
        LinkedTreeMap tree = (LinkedTreeMap) response.body();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(tree).getAsJsonObject();
        JsonArray array = jsonObject.getAsJsonArray("data");
        return array;
    }

    public ErrorEntity construirError(retrofit2.Response<Object> response){
        LinkedTreeMap tree = (LinkedTreeMap) response.body();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(tree).getAsJsonObject();
        //JsonArray array = jsonObject.getAsJsonArray("data");
        return new ErrorEntity(jsonObject.get("code").getAsString(),
                Constants.ERROR_PROCESAR_INFORMA,
                String.valueOf(response.code()));
    }

    public ErrorEntity errorInternal(retrofit2.Response<Object> response){
        return new ErrorEntity(String.valueOf(response.code()), Constants.ERROR_INTERNAL_SERVER, String.valueOf(response.code()));
    }

    public static Boolean HayConexionaInternet(Context a ) {
        ConnectivityManager cm;
        NetworkInfo ni;
        cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        boolean tipoConexion1 = false;
        boolean tipoConexion2 = false;
        boolean result = false;

        if (ni != null) {
            ConnectivityManager connManager1 = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager1.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            ConnectivityManager connManager2 = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobile = connManager2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(mWifi != null){
                if (mWifi.isConnected()) {
                    tipoConexion1 = true;
                    ////Log.d("DMA","wifi: "+tipoConexion2);
                }
            }

            if(mMobile != null){
                if (mMobile.isConnected()) {
                    tipoConexion2 = true;
                    ////Log.d("DMA","datos: "+tipoConexion2);
                }
            }


            if (tipoConexion1 || tipoConexion2) {
                result=true;
            }
        }
        ////Log.d("DMA","ESTADO CONEXION : "+result);
        return result;
    }


    public String getFecha(){
        Calendar c1 = Calendar.getInstance();
        String dia = String.valueOf(c1.get(Calendar.DATE));
        String mes = String.valueOf(c1.get(Calendar.MONTH)+1);
        String annio = String.valueOf(c1.get(Calendar.YEAR));

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);

        return dia+"-"+mes+"-"+annio + " " + String.valueOf(hour)+":"+String.valueOf(min);
    }

}
