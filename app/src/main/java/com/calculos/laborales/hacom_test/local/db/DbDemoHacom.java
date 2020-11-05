package com.calculos.laborales.hacom_test.local.db;

import com.calculos.laborales.hacom_test.local.dao.CoordenadasDao;
import com.calculos.laborales.hacom_test.local.tables.CoordenadasTbl;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {
        CoordenadasTbl.class},
        version = 1,
        exportSchema = false)
abstract public class DbDemoHacom extends RoomDatabase {

    public abstract CoordenadasDao getCoordenadasDao();
}
