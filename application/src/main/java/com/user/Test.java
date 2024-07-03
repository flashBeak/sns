package com.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class Test {

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @Bean
    public CommandLineRunner checkDatabaseConnection(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                if (connection != null && !connection.isClosed()) {
                    System.out.println("Connection to the database is successful!");
                } else {
                    System.out.println("Failed to connect to the database.");
                }
            } catch (SQLException e) {
                System.out.println("Database connection failed!");
                e.printStackTrace();
            }
        };
    }
}
