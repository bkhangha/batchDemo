package com.example.batchDemo;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class UniqueJobIncrementer implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters parameters) {
        return new JobParametersBuilder(parameters)
                .addLong("timestamp", System.currentTimeMillis()) // Sử dụng timestamp để đảm bảo không trùng
                .toJobParameters();
    }
}