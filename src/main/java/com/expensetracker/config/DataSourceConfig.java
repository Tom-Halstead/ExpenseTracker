package com.expensetracker.config;
import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;

public class DataSourceConfig {

    public DataSource getDataSource() {
        // Create a new instance of BasicDataSource
        BasicDataSource dataSource = new BasicDataSource();

        // Set the JDBC URL for your PostgreSQL database
        dataSource.setUrl("jdbc:postgresql://localhost:5432/expensetracker");

        // Set the PostgreSQL database username and password
        dataSource.setUsername("postgres");
        dataSource.setPassword("billythekid");

        // Set the PostgreSQL JDBC driver class name
        dataSource.setDriverClassName("org.postgresql.Driver");

        // Optional: Configure connection pool settings
        dataSource.setInitialSize(5); // Initial number of connections
        dataSource.setMaxTotal(10);   // Maximum number of connections in the pool
        dataSource.setMinIdle(2);     // Minimum number of idle connections in the pool
        dataSource.setMaxIdle(5);     // Maximum number of idle connections in the pool
        dataSource.setMaxWaitMillis(10000); // Maximum wait time for a connection, in milliseconds

        return dataSource;
    }
}
