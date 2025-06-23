package com.batchTrai.demo.dto;

import jakarta.persistence.Id;

public record EmployeeDto(
        String employeeId,
        String fullName,
        String jobTitle,
        String department,
        String businessUnit,
        String gender,
        String ethnicity,
        int age
) {

}
