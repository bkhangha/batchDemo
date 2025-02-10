package com.example.batchDemo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    @Bean("dataSource")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setJdbcUrl("jdbc:oracle:thin:@//localhost:1521/oraclelinux");
        config.setUsername("oraclelocal");
        config.setPassword("123456");

        return new HikariDataSource(config);
    }

    @Bean(name = "salaryDataSource",defaultCandidate = false)
    public DataSource salaryDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setJdbcUrl("jdbc:oracle:thin:@//localhost:1521/oraclelinux");
        config.setUsername("other");
        config.setPassword("123456");
        return new HikariDataSource(config);
    }
}
