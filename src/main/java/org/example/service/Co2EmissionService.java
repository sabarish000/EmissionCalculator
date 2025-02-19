package org.example.service;

import org.example.client.ServiceClient;
import org.example.core.Calculator;
import org.example.exception.CalculationException;
import org.example.exception.ServiceClientException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Co2EmissionService implements EmissionService{
    private static final Logger logger = LoggerFactory.getLogger(Co2EmissionService.class);
    private final ServiceClient serviceClient;
    private final Calculator calculator;

    public Co2EmissionService(ServiceClient serviceClient, Calculator calculator) {
        this.serviceClient = serviceClient;
        this.calculator = calculator;
    }
    @Override
    public double calculateEmission(String startCity, String endCity, double emissionInGramsPerKm) throws CalculationException {
        CompletableFuture<double[]> startCoordinatesFuture = getCoordinatesFuture(startCity);
        CompletableFuture<double[]> endCoordinatesFuture = getCoordinatesFuture(endCity);

        CompletableFuture<Double> distanceFuture = startCoordinatesFuture
                .thenCombine(endCoordinatesFuture,this::fetchDistance);

        try {
            double distanceInKm = distanceFuture.get();
            return calculator.calculate(distanceInKm, emissionInGramsPerKm);
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error occurred during emission calculation: {}", e.getMessage());
            throw new CalculationException("Error occurred during emission calculation: " + e.getMessage());
        }
    }

    @NotNull
    private CompletableFuture<double[]> getCoordinatesFuture(String city) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return serviceClient.getCoordinates(city);
            } catch (ServiceClientException e) {
                logger.error("Error occurred while fetching coordinates for city: {}", city);
                throw new CalculationException(e.getMessage());
            }
        });
    }

    private double fetchDistance(double[] startCoordinates, double[] endCoordinates) {
        try {
            return serviceClient.getDistance(startCoordinates, endCoordinates);
        } catch (ServiceClientException e) {
            logger.error("Error occurred while fetching distance between cities: {}", e.getMessage());
            throw new CalculationException(e.getMessage());
        }
    }
}
