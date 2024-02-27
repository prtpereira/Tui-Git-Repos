package com.tui.gitmanager.representers;

public class CustomExceptionRepresenter {
    public int status;
    public String message;

    public CustomExceptionRepresenter(int status, String message) {
        this.status = status;
        this.message = message;
    }
}