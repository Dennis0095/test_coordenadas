package com.calculos.laborales.hacom_test.mvvm.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.calculos.laborales.hacom_test.R;
import com.calculos.laborales.hacom_test.local.entity.ErrorEntity;
import com.calculos.laborales.hacom_test.local.entity.RespIniciarEntity;
import com.calculos.laborales.hacom_test.mvvm.viewmodel.genViewModel;
import com.calculos.laborales.hacom_test.util.Constants;
import com.calculos.laborales.hacom_test.util.ServiceGuardar;
import com.calculos.laborales.hacom_test.util.Util;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar pbCargando;
    private TextInputEditText tiEdUsua, tiEdPass;
    private boolean request = true;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        solicitarPermisos();
        ctx = this;
        Util util = new Util(this);
        pbCargando = findViewById(R.id.pbCargando);
        tiEdPass = findViewById(R.id.tiEdPass);
        tiEdUsua = findViewById(R.id.tiEdUsua);
        findViewById(R.id.cliniciar).setOnClickListener(this);
        util.colorProgressbar(pbCargando, R.color.white);

    }

    private void solicitarPermisos()
    {
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.Q){
            if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){

                boolean permissionAccessCoarseLocationApproved =
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED;

                if (permissionAccessCoarseLocationApproved) {
                    boolean backgroundLocationPermissionApproved =
                            ActivityCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED;

                    if (!backgroundLocationPermissionApproved){
                        ActivityCompat.requestPermissions(this, new String[] {
                                        Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                2000);
                    }
                } else {
                    ActivityCompat.requestPermissions(this, new String[] {
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            },
                            3000);
                }
            }
        }else{
            if(ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1000);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cliniciar:
                if(request){
                    if(validateCampos()){
                        request = false;
                        pbCargando.setVisibility(View.VISIBLE);
                        genViewModel viewModelGen = ViewModelProviders.of(this).get(genViewModel.class);
                        viewModelGen.login(tiEdUsua.getText().toString().trim(), tiEdPass.getText().toString().trim());
                        viewModelGen.getLogin().observe(this, new Observer<ArrayList>() {
                            @Override
                            public void onChanged(ArrayList arrayList) {
                                request = true;
                                if(arrayList.size()>1){
                                    pbCargando.setVisibility(View.INVISIBLE);
                                    RespIniciarEntity res = (RespIniciarEntity) arrayList.get(1);
                                    if(res.estado.equals("1")){
                                        String aResp[] = res.getMensaje().split("&");

                                        SharedPreferences.Editor edit = Util.sharedPref.edit();
                                        edit.putString("idUser", aResp[1]);
                                        edit.apply();
                                        iniciar(aResp[0] +" " + aResp[2]);
                                    }else{
                                        Toast.makeText(ctx, Constants.MSG_VALIDATE_USER, Toast.LENGTH_LONG).show();
                                    }

                                }else{
                                    ErrorEntity error = (ErrorEntity) arrayList.get(0);
                                    Toast.makeText(ctx, error.getMessage().split("&")[0], Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                    }
                }

                break;
        }

    }

    private void iniciar(String msj){
        Toast.makeText(this, msj, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private boolean validateCampos(){
        boolean b;
        if(tiEdUsua.getText().toString().trim().length()==0 || tiEdPass.getText().toString().trim().length()==0){
            Toast.makeText(this, getString(R.string.verificarCampos), Toast.LENGTH_LONG).show();
            tiEdUsua.requestFocus();
            b=false;
        }else
            b = true;
        return b;
    }
}
