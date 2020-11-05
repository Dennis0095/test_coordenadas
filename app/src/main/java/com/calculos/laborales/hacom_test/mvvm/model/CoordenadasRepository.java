package com.calculos.laborales.hacom_test.mvvm.model;

import com.calculos.laborales.hacom_test.local.dao.CoordenadasDao;
import com.calculos.laborales.hacom_test.local.entity.ErrorEntity;
import com.calculos.laborales.hacom_test.local.entity.RespIniciarEntity;
import com.calculos.laborales.hacom_test.local.tables.CoordenadasTbl;
import com.calculos.laborales.hacom_test.util.Constants;
import com.calculos.laborales.hacom_test.util.Util;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoordenadasRepository {

    private static CoordenadasDao coorDao;
    private MutableLiveData<ArrayList> login;
    private Util util;

    public static void setCoordenadasRepository(CoordenadasDao dao){
        if(coorDao == null)
            coorDao = dao;
    }


    public CoordenadasRepository() {
        this.util = new Util();
    }

    public void login(String user, String pass){
        login = new MutableLiveData<>();
        ArrayList aResp = new ArrayList();
        Services services = Util.retrofit.create(Services.class);
        Call<Object> call = services.login(user, pass);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JsonObject o = util.statusResponse(response);
                    String code = o.get("code").getAsString();
                    switch (code){
                        case "ok":
                            JsonArray aObj = util.formatearRespuesta(response);

                            JsonObject obj = (JsonObject) aObj.get(0);
                            if(obj.get("estado").getAsString().equals("1") || obj.get("estado").getAsString().equals("0")){
                                aResp.add("");
                                aResp.add(new RespIniciarEntity(obj.get("estado").getAsString(), obj.get("msj").getAsString()));
                            }else{
                                aResp.add("");
                                aResp.add(new ErrorEntity(code, Constants.ERROR_REQUEST));
                            }
                            break;
                        case "error":
                            aResp.add(util.construirError(response));
                            break;
                    }
                }else
                    aResp.add(util.errorInternal(response));

                login.postValue(aResp);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Util util = new Util();
                aResp.add(util.construirErrorFailure());
                login.postValue(aResp);
            }
        });
    }

    public MutableLiveData<ArrayList> getLogin(){
        return login;
    }

    public void guardarCorrdenadas(CoordenadasTbl objCoordenada){
        login = new MutableLiveData<>();
        ArrayList aResp = new ArrayList();
        Services services = Util.retrofit.create(Services.class);
        Call<Object> call = services.guardarCoordenadas(Integer.parseInt(Util.sharedPref.getString("idUser", "0")),
                objCoordenada.getLatitude(), objCoordenada.getLongitude(), objCoordenada.getFecha());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JsonObject o = util.statusResponse(response);
                    String code = o.get("code").getAsString();
                    switch (code){
                        case "ok":
                            break;
                        case "error":
                            // GUARDAR OFFLINE
                            coorDao.addCoordenada(objCoordenada);
                            break;
                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // GUARDAR OFFLINE
                coorDao.addCoordenada(objCoordenada);
            }
        });
    }

    public void enviarCorrdenadasOffline(CoordenadasTbl objCoordenada){
        login = new MutableLiveData<>();
        ArrayList aResp = new ArrayList();
        Services services = Util.retrofit.create(Services.class);
        Call<Object> call = services.guardarCoordenadas(Integer.parseInt(Util.sharedPref.getString("idUser", "0")),
                objCoordenada.getLatitude(), objCoordenada.getLongitude(), objCoordenada.getFecha());
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()){
                    JsonObject o = util.statusResponse(response);
                    String code = o.get("code").getAsString();
                    switch (code){
                        case "ok":
                            JsonArray aObj = util.formatearRespuesta(response);
                            JsonObject obj = (JsonObject) aObj.get(0);
                            if(obj.get("estado").getAsString().equals("1")){
                                coorDao.delCoordenada(objCoordenada); // ELIMINAR DE OFFLINE
                            }
                            break;
                        case "error":
                            break;
                    }
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
            }
        });
    }

    public List<CoordenadasTbl> listarCoordenadas(){
        return coorDao.listarCoordenadas();
    }

}
