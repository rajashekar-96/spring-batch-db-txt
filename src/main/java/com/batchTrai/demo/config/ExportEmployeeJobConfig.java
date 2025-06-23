package com.batchTrai.demo.config;

import com.batchTrai.demo.Entity.Employee;
import com.batchTrai.demo.dto.EmployeeDto;
import com.batchTrai.demo.processor.EmployeeProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@RequiredArgsConstructor
//@EnableBatchProcessing
@Configuration
public class ExportEmployeeJobConfig {

    private final DataSource dataSource;
    private final EmployeeProcessor employeeProcessor;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JdbcCursorItemReader<EmployeeDto> employeeDtoJdbcCursorItemReader() {
        String sql = "SELECT * FROM EMPLOYEE";
        return new JdbcCursorItemReaderBuilder<EmployeeDto>()
                .name("Employee Details")
                .dataSource(dataSource)
                .sql(sql)
                .fetchSize(100)
                .rowMapper(new DataClassRowMapper<>(EmployeeDto.class))
                .build();
    }

    @Bean
    public FlatFileItemWriter<Employee> flatFileItemWriter() {
        CustomLineAggregator aggregator = new CustomLineAggregator();

        return new FlatFileItemWriterBuilder<Employee>()
                .name("Employee File Writer")
                .resource(new FileSystemResource("D:/Code/java/spring-batch-db-txt/src/main/resources/employee.txt"))
                .lineAggregator(aggregator)
                .footerCallback(writer -> {
                    String footer = aggregator.getFooter();
                    if (!footer.isEmpty()) {
                        writer.write(footer);
                    }
                })
                .append(false)
                .build();
    }

    @Bean
    public Step fromEmployeeToFile() {
        return new StepBuilder("from DB to File",jobRepository)
                .<EmployeeDto, Employee>chunk(5, platformTransactionManager)
                .reader(employeeDtoJdbcCursorItemReader())
                .processor(employeeProcessor)
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public Job dbToFileJob(Step fromEmployeeToFile) {
        return new JobBuilder("dbToFile job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fromEmployeeToFile)
                .build();
    }

}
