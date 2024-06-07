package com.sparta.areadevelopment.entity;

public enum StatusEnum {
    DELETED("DELETED"),
    ACTIVE("ACTIVATIVE");

    private final String stat;

    StatusEnum(String stat) {
        this.stat = stat;
    }
}
