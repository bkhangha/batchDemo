package com.example.batchDemo.business;

import com.example.batchDemo.model.Profile;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Component
@Slf4j
public class ProfileWriter implements ItemWriter<Profile> {

    private final JdbcBatchItemWriter<Profile> jdbcBatchItemWriter;

    public ProfileWriter(@Qualifier("salaryDataSource")  DataSource dataSource) {
        System.out.println("DataSource: " + dataSource);

        this.jdbcBatchItemWriter = new JdbcBatchItemWriterBuilder<Profile>()
                .sql("INSERT INTO profile (emp_code, emp_name, profile_name) VALUES (:empCode, :empName, :profileName)")
                .dataSource(dataSource)
                .beanMapped()
                .build();

        this.jdbcBatchItemWriter.afterPropertiesSet();
    }

    @Override
    @Transactional
    public void write(Chunk<? extends Profile> chunk) throws Exception {
        List<? extends Profile> items = chunk.getItems();
        log.info("Writing {} profiles to database", items.size());
        jdbcBatchItemWriter.write(chunk);
    }
}