package org.example.service;

import org.example.exception.CalculationException;
import org.example.exception.ServiceClientException;

public interface EmissionService {
    double calculateEmission(String startCity, String endCity, double emissionInGrams) throws CalculationException;
}
