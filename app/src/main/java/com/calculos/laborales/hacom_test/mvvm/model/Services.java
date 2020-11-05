package com.calculos.laborales.hacom_test.mvvm.model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Services {

    @FormUrlEncoded
    @POST("login.php")
    Call<Object> login(@Field("usuario") String user,
                                      @Field("password") String pass);

    @FormUrlEncoded
    @POST("save_coordenadas.php")
    Call<Object> guardarCoordenadas(@Field("idUser") int user,
                                      @Field("latitude") String pass,
                                    @Field("longitude") String  longitude,
                                    @Field("fecha") String fecha);
}
