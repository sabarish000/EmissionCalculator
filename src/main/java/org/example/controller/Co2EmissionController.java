package org.example.controller;

import org.example.exception.CalculationException;
import org.example.model.TransportationMethod;
import org.example.service.EmissionService;
import org.example.utils.ArgumentParser;

public class Co2EmissionController implements EmissionController {
    private EmissionService emissionService;

    public void setEmissionService(EmissionService emissionService) {
        this.emissionService = emissionService;
    };

    @Override
    public double calculateEmission(String startCity, String endCity, String method) throws CalculationException {
        // throws IllegalArgumentException if any of the inputs are missing
        validateInputs(startCity, endCity, method);
        // throws IllegalArgumentException if the transportation method is invalid
        TransportationMethod transportationMethod = null;
        try {
            transportationMethod = ArgumentParser.getTransportationMethod(method);;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Provided invalid transportation method: "+ method);
        }
        return emissionService.calculateEmission(startCity, endCity, transportationMethod.getCo2EmissionPerKm());
    }

    private void validateInputs(String startCity, String endCity, String method) throws IllegalArgumentException{
        if (startCity == null || startCity.isEmpty()) {
            throw new IllegalArgumentException("Start city is missing");
        }

        if (endCity == null || endCity.isEmpty()) {
            throw new IllegalArgumentException("End city is missing");
        }

        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("Transportation method is missing");
        }
    }
}
