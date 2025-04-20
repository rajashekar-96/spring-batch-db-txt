package com.batchTrai.demo.processor;

import com.batchTrai.demo.Entity.Employee;
import com.batchTrai.demo.dto.EmployeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class EmployeeProcessor implements ItemProcessor<EmployeeDto, Employee> {


    @Override
    public Employee process(EmployeeDto employeeDto) throws Exception {
        return Employee.builder()
                .employeeId(employeeDto.employeeId())
                .fullName(employeeDto.fullName())
                .businessUnit(employeeDto.businessUnit())
                .department(employeeDto.department())
                .gender(employeeDto.gender())
                .age(employeeDto.age())
                .ethnicity(employeeDto.ethnicity())
                .jobTitle(employeeDto.jobTitle())
                .build();
    }
}
