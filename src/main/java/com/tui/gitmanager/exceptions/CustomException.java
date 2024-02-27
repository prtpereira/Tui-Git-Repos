package com.tui.gitmanager.exceptions;

public class CustomException extends Exception{
    private String type;

    public CustomException(String type, String message) {
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
