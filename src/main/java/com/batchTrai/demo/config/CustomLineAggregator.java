package com.batchTrai.demo.config;

import com.batchTrai.demo.Entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.transform.LineAggregator;

import java.util.ArrayList;
import java.util.List;

//@Slf4j
public class CustomLineAggregator implements LineAggregator<Employee> {
    private static final Logger log = LoggerFactory.getLogger(CustomLineAggregator.class);
    private int recordCount = 0;
    private int batchCount = 0;
    private List<Integer> batchAges = new ArrayList<>();
    private List<String> batchRecords = new ArrayList<>();
    private boolean headerWritten = false;

    @Override
    public String aggregate(Employee item) {
        StringBuilder output = new StringBuilder();

        // Write file header only once
        if (!headerWritten) {
            output.append("Header of file");
            headerWritten = true;
        }

        // Validate item
        if (item == null) {
            log.warn("Skipping null Employee record");
            return "";
        }

        // Add employee age to current batch
        batchAges.add(item.getAge());
        recordCount++;

        // Format employee record without extra newline
        String employeeRecord = String.format(
                "%s;%s;%s;%s;%s;%s;%s;%d",
                item.getEmployeeId(),
                item.getFullName(),
                item.getJobTitle(),
                item.getDepartment(),
                item.getBusinessUnit(),
                item.getGender(),
                item.getEthnicity(),
                item.getAge()
        );
        batchRecords.add(employeeRecord);

        // Write batch header and records after every 5 records
        if (recordCount % 5 == 0) {
            batchCount++;
            int sumOfAges = batchAges.stream().mapToInt(Integer::intValue).sum();
            output.append(String.format("Batch-%02d;Total-5;%d\n", batchCount, sumOfAges));
            for (String record : batchRecords) {
                output.append(record).append("\n");
            }
            batchAges.clear();
            batchRecords.clear();
        }

        return output.toString();
    }

    // Handle remaining records in last batch
    public String getFooter() {
        if (!batchRecords.isEmpty()) {
            batchCount++;
            int sumOfAges = batchAges.stream().mapToInt(Integer::intValue).sum();
            StringBuilder footer = new StringBuilder();
            footer.append(String.format("Batch-%02d;Total-%d;%d\n", batchCount, batchRecords.size(), sumOfAges));
            for (String record : batchRecords) {
                footer.append(record).append("\n");
            }
            batchAges.clear();
            batchRecords.clear();
            return footer.toString();
        }
        return "";
    }
}
