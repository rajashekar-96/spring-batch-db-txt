//package com.batchTrai.demo.config;
//
//import com.batchTrai.demo.Entity.Employee;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.file.FlatFileItemWriter;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class CustomBatchWriter implements ItemWriter<Employee>, InitializingBean, DisposableBean {
//
//    private BufferedWriter writer;
//    private final String filePath = "employee.txt";
//    private int recordCount = 0;
//    private int batchNumber = 1;
//    private int batchAgeSum = 0;
//    private static final int BATCH_SIZE = 5;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        writer = new BufferedWriter(new FileWriter(filePath, true)); // append mode
//        writer.write("Header of file");
//        writer.newLine();
//    }
//
//    @Override
//    public void write(List<? extends Employee> items) throws Exception {
//        for (Employee item : items) {
//            if (recordCount % BATCH_SIZE == 0) {
//                if (recordCount != 0) {
//                    writeBatchHeader();
//                }
//                batchAgeSum = 0;
//            }
//
//            batchAgeSum += Integer.parseInt(item.getAge());
//            recordCount++;
//
//            writer.write(String.join(";",
//                    item.getEmployeeId(), item.getFullName(), item.getJobTitle(), item.getDepartment(),
//                    item.getBusinessUnit(), item.getGender(), item.getEthnicity(), item.getAge()));
//            writer.newLine();
//        }
//
//        // Flush to ensure data is written
//        writer.flush();
//    }
//
//    private void writeBatchHeader() throws IOException {
//        writer.write(String.format("<Batch-%02d><Total-%02d><%d>", batchNumber, recordCount, batchAgeSum));
//        writer.newLine();
//        batchNumber++;
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        // Write final batch header if remaining
//        if ((recordCount % BATCH_SIZE) != 0) {
//            writeBatchHeader();
//        }
//
//        if (writer != null) {
//            writer.flush();
//            writer.close();
//        }
//    }
//}
//
//
