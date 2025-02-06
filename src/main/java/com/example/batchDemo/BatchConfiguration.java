package com.example.batchDemo;

import com.example.batchDemo.model.Employee;
import com.example.batchDemo.model.Profile;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    // Read from DB
    @Bean
    public JdbcCursorItemReader<Employee> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Employee>()
                .name("employeeItemReader")
                .dataSource(dataSource)
                .sql("SELECT emp_code, emp_name, exp_in_years FROM employees")
                .rowMapper(new BeanPropertyRowMapper<>(Employee.class))
                .build();
    }

    // Read from CSV
//    @Bean
//    public FlatFileItemReader<Employee> reader() {
//        return new FlatFileItemReaderBuilder<Employee>()
//                .name("employeeItemReader")
//                .resource(new ClassPathResource("employees.csv"))
//                .delimited()
//                .names(new String[]{ "empCode", "empName", "expInYears" })
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
//                    setTargetType(Employee.class);
//                }})
//                .linesToSkip(1)
//                .build();
//    }

    @Bean
    public JdbcBatchItemWriter<Profile> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Profile>()
                .sql("INSERT INTO profile (emp_code, emp_name, profile_name) VALUES (:empCode, :empName, :profileName)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
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

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:testdb;MODE=STRICT");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//        return dataSource;
//    }

//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
//        dataSource.setUrl("jdbc:oracle:thin:@//localhost:1521/oraclelinux");
//        dataSource.setUsername("oraclelocal");
//        dataSource.setPassword("123456");
//        return dataSource;
//    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setJdbcUrl("jdbc:oracle:thin:@//localhost:1521/oraclelinux");
        config.setUsername("oraclelocal");
        config.setPassword("123456");

        return new HikariDataSource(config);
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
