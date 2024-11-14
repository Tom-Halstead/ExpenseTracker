package com.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ExpenseTrackerApp {
        public static void main(String[] args) {
                SpringApplication.run(ExpenseTrackerApp.class, args);
        }
}