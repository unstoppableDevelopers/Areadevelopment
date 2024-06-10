package com.sparta.areadevelopment.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusEnum {
    DELETED("DELETED"),
    ACTIVE("ACTIVATE");

    private final String stat;

    StatusEnum(String stat) {
        this.stat = stat;
    }

    @JsonValue
    public String getStat() {
        return stat;
    }
}
