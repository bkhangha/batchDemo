package com.example.batchDemo;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.UUID;

public class UniqueJobIncrementer implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters parameters) {
        return new JobParametersBuilder(parameters)
                .addString("run.id", UUID.randomUUID().toString() + System.nanoTime())
                .addLong("timestamp", System.currentTimeMillis()) // Sử dụng timestamp để đảm bảo không trùng
                .toJobParameters();
    }
}