package com.example.batchDemo.business;

import com.example.batchDemo.model.Employee;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class EmployeeReader implements ItemReader<Employee> {

    private final JdbcTemplate jdbcTemplate;
    private int nextEmployeeIndex;
    private List<Employee> employeeData;

    @Autowired
    public EmployeeReader(@Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.nextEmployeeIndex = 0;
    }

    @Override
    public Employee read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (employeeDataIsNotInitialized()) {
            employeeData = fetchEmployeeDataFromDatabase();
        }

        Employee nextEmployee = null;

        if (nextEmployeeIndex < employeeData.size()) {
            nextEmployee = employeeData.get(nextEmployeeIndex);
            nextEmployeeIndex++;
        }

        return nextEmployee;
    }

    private boolean employeeDataIsNotInitialized() {
        return this.employeeData == null;
    }

    private List<Employee> fetchEmployeeDataFromDatabase() {
        return jdbcTemplate.query("SELECT emp_code, emp_name, exp_in_years FROM employees", new BeanPropertyRowMapper<>(Employee.class));
    }
}