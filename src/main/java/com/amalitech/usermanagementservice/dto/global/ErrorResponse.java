package com.amalitech.usermanagementservice.dto.global;

import com.amalitech.usermanagementservice.enums.ErrorCode;

public record ErrorResponse<T>(ErrorCode code, String message, String timestamp,String path, T error) {
}
