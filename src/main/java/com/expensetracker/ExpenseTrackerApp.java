package com.expensetracker;

import com.expensetracker.config.DataSourceConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

public class ExpenseTrackerApp {
    public static void main(String[] args) {
        DataSource dataSource = new DataSourceConfig().getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    }
}