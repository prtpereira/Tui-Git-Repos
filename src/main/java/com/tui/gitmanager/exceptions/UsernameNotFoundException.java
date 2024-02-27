package com.bvgroup.exchangerates.exceptions;

public class CustomRateException extends Exception{
    private String type;

    public CustomRateException(String type, String message) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
