package org.example.service;

import org.example.client.ServiceClient;
import org.example.core.Calculator;
import org.example.exception.CalculationException;
import org.example.exception.ServiceClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Co2EmissionServiceV1 implements EmissionService{
    private static final Logger logger = LoggerFactory.getLogger(Co2EmissionServiceV1.class);
    private final ServiceClient serviceClient;
    private final Calculator calculator;

    public Co2EmissionServiceV1(ServiceClient serviceClient, Calculator calculator) {
        this.serviceClient = serviceClient;
        this.calculator = calculator;
    }
    @Override
    public double calculateEmission(String startCity, String endCity, double emissionInGramsPerKm) throws CalculationException{
        double[] startCoordinates = null;
        try {
            startCoordinates = serviceClient.getCoordinates(startCity);
            logger.info("Start coordinates: {}, {}", startCoordinates[0],startCoordinates[1]);
            double[] endCoordinates = serviceClient.getCoordinates(endCity);
            logger.info("End coordinates: {}, {}", endCoordinates[0] , endCoordinates[1]);
            double distance = serviceClient.getDistance(startCoordinates, endCoordinates);
            logger.info("Distance: {}", distance);
            return calculator.calculate(distance, emissionInGramsPerKm);
        } catch (ServiceClientException e) {
            throw new CalculationException(e.getMessage());
        }
    }
}
