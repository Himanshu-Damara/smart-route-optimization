package com.sro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Spring Boot Application for Smart Route Optimization System
 */
@SpringBootApplication
public class SmartRouteOptimizationApplication {

    private static final Logger logger = LoggerFactory.getLogger(SmartRouteOptimizationApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SmartRouteOptimizationApplication.class, args);
        logger.info("Smart Route Optimization System started successfully!");
        logger.info("API Documentation: http://localhost:8080/api");
        logger.info("H2 Console: http://localhost:8080/h2-console");
    }

}
