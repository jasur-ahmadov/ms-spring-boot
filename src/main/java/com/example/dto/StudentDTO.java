package com.example.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO implements Serializable {

    private String temp;

    @NotEmpty(message = "{error.studentNameNotDefined}")
    private String name;

    @NotEmpty(message = "{error.studentSurnameNotDefined}")
    private String surname;
}