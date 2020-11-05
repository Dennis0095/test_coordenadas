package com.calculos.laborales.hacom_test.local.db;

import android.content.Context;

import com.calculos.laborales.hacom_test.mvvm.model.CoordenadasRepository;

import androidx.room.Room;

public class InstanceDB {

    public static DbDemoHacom dbDemo;
    private static InstanceDB INSTANCE;

    public static void setInstance(Context ctx){
        if(INSTANCE == null)
            INSTANCE = Initializer.INSTANCE;
        INSTANCE.init(ctx);
    }

    private void init(Context ctx){
        if(dbDemo == null)
            dbDemo = Room.databaseBuilder(ctx, DbDemoHacom.class, "dbHacom").allowMainThreadQueries().fallbackToDestructiveMigration().build();

        CoordenadasRepository.setCoordenadasRepository(dbDemo.getCoordenadasDao());

    }

    public static InstanceDB getInstance(){
        return INSTANCE;
    }

    // INSTANCIANDO UNA VEZ LA CLASE
    private static class Initializer{
        private static final InstanceDB INSTANCE = new InstanceDB();
    }
}
