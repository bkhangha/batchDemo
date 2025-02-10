package com.example.batchDemo;

import com.example.batchDemo.business.EmployeeReader;
import com.example.batchDemo.business.ProfileWriter;
import com.example.batchDemo.business.EmployeeItemProcessor;
import com.example.batchDemo.model.Employee;
import com.example.batchDemo.model.Profile;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("salaryDataSource")
    private DataSource salaryDataSource;

    @Bean
    public EmployeeReader employeeReader() {
        return new EmployeeReader(dataSource);
    }

    @Bean
    public ProfileWriter writer() {
        return new ProfileWriter(salaryDataSource);
    }

    @Bean
    public EmployeeItemProcessor processor() {
        return new EmployeeItemProcessor();
    }

    @Bean
    public Job createEmployeeJob(JobRepository jobRepository, EmpJobExecutionListener listener, Step step1) {
        return new JobBuilder("createEmployeeJob", jobRepository)
                .incrementer(new UniqueJobIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager, ItemReader<Employee> reader, ItemWriter<Profile> writer,
                      ItemProcessor<Employee, Profile> processor) {
        return new StepBuilder("step1", jobRepository)
                .<Employee, Profile>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
