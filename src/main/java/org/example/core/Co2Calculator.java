package org.example.core;

public class Co2Calculator implements Calculator{
    @Override
    public double calculate(double distanceInKm, double emissionInGramsPerKm) {
        return (distanceInKm * emissionInGramsPerKm) / 1000; // Convert grams to kilograms
    }
}
