package com.calculos.laborales.hacom_test.mvvm.viewmodel;

import com.calculos.laborales.hacom_test.mvvm.model.CoordenadasRepository;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class genViewModel extends ViewModel {
    private CoordenadasRepository repository;
    private MutableLiveData<ArrayList> respLogin;


    public genViewModel(){
        repository = new CoordenadasRepository();
    }

    public void login(String user, String pass){
        repository.login(user, pass);
        respLogin = repository.getLogin();
    }

    public MutableLiveData<ArrayList> getLogin(){
        return respLogin;
    }

}
