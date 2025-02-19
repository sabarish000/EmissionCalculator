package org.example.controller;

import org.example.exception.CalculationException;
import org.example.model.TransportationMethod;
import org.example.service.EmissionService;

public interface EmissionController {
    double calculateEmission(String startCity, String endCity, String transportationMethod) throws CalculationException;

    void setEmissionService(EmissionService emissionService);
}
