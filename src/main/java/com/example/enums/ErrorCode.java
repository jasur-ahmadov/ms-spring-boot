package com.example.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    BAD_REQUEST(400, "Invalid Arguments"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    int httpStatus;
    String description;
}