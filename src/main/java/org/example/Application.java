package org.example;

import okhttp3.OkHttpClient;
import org.example.client.OpenRouteServiceClient;
import org.example.controller.EmissionController;
import org.example.core.Calculator;
import org.example.core.Co2Calculator;
import org.example.exception.CalculationException;
import org.example.model.TransportationMethod;
import org.example.service.Co2EmissionService;
import org.example.service.EmissionService;
import org.example.utils.ArgumentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String ARGS_START = "start";
    private static final String ARGS_END = "end";
    private static final String ARGS_TRANSPORTATION_METHOD = "transportation-method";

    private EmissionController emissionController;

    public void setEmmissionController(EmissionController emissionController) {
        this.emissionController = emissionController;
    }

    public void run(String[] args) throws IllegalArgumentException{
        logger.info("args: {}", Arrays.toString(args));
        Map<String, String> arguments = null;
        arguments = ArgumentParser.parseArguments(args);
        logger.info("args: {}", arguments);
        String startCity = arguments.get(ARGS_START);
        String endCity = arguments.get(ARGS_END);
        String method = arguments.get(ARGS_TRANSPORTATION_METHOD);
        String apiKey = System.getenv("ORS_TOKEN");

        // throws IllegalArgumentException if apiKey is not provided
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("ORS_TOKEN environment variable is missing");
        }



        emissionController.setEmissionService(getEmissionService(apiKey));

        try {
            double co2Emission = emissionController.calculateEmission(startCity, endCity, method);
            logger.info("Your trip caused {}kg of CO2-equivalent.", String.format("%.01f", co2Emission));
        } catch (CalculationException e) {
            logger.error("Error occurred while calculating emission: {}", e.getMessage());
        }
    }


    // Factory method to create the default EmissionService
    protected static EmissionService getEmissionService(String apiKey) {
        OpenRouteServiceClient apiClient = new OpenRouteServiceClient(apiKey, new OkHttpClient());
        Calculator calculator = new Co2Calculator();
        return new Co2EmissionService(apiClient, calculator);
    }
}
