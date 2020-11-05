package com.calculos.laborales.hacom_test.local.entity;

public class ErrorEntity {
    private String code;
    private String message;
    private String codeHttp;

    public ErrorEntity(String code, String message, String codeHttp) {
        this.code = code;
        this.message = message;
        this.codeHttp = codeHttp;
    }

    public ErrorEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getCodeHttp() {
        return codeHttp;
    }
}
