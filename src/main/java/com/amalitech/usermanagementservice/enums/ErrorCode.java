package com.amalitech.usermanagementservice.enums;

public enum ErrorCode {
    ERR_UNKNOWN("1001"),
    ERR_CONFLICT("4101");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

