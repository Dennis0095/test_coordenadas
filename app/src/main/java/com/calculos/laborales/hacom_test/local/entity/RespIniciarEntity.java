package com.calculos.laborales.hacom_test.local.entity;

public class RespIniciarEntity {
    public String estado;
    public String mensaje;

    public RespIniciarEntity(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }
}
