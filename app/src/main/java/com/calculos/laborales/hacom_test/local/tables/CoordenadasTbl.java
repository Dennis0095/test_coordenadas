package com.calculos.laborales.hacom_test.local.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "coordenadas")
public class CoordenadasTbl {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "latitude")
    public String latitude;

    @ColumnInfo(name = "longitude")
    public String longitude;

    @ColumnInfo(name = "fecha")
    public String fecha;

    public int getId() {
        return id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getFecha() {
        return fecha;
    }

    public CoordenadasTbl(String latitude, String longitude, String fecha) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.fecha = fecha;
    }

    public CoordenadasTbl() {
    }
}
