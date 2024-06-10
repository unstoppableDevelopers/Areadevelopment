package com.sparta.areadevelopment.enums;

public enum StatusEnum {
    DELETED("DELETED"),
    ACTIVE("ACTIVATE");

    private final String status;

    StatusEnum(String status) {
        this.status = status;
    }
}
