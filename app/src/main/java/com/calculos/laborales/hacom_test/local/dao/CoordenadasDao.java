package com.calculos.laborales.hacom_test.local.dao;

import com.calculos.laborales.hacom_test.local.tables.CoordenadasTbl;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CoordenadasDao {

    @Insert
    void addCoordenada(CoordenadasTbl obj);

    @Delete
    void delCoordenada(CoordenadasTbl obj);

    @Query("select * from coordenadas")
    List<CoordenadasTbl> listarCoordenadas();
}
