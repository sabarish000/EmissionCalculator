package org.example;

import org.example.controller.Co2EmissionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Application app = new Application();
        app.setEmmissionController(new Co2EmissionController());
        try {
            app.run(args);
        } catch (Exception e) {
            logger.error("Error occurred while running the application: {}", e.getMessage());
        }
    }
}