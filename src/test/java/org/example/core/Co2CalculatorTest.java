package org.example.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Co2CalculatorTest {

    private final Co2Calculator calculator = new Co2Calculator();

    @Test
    void testCalculate_Success() {
        // Arrange
        double distanceInKm = 289.0;
        double emissionInGramsPerKm = 142.0;

        // Act
        double result = calculator.calculate(distanceInKm, emissionInGramsPerKm);

        // Assert
        assertEquals(41.038, result, 0.001); // 289 km * 142 g/km = 41.038 kg
    }

    @Test
    void testCalculate_ZeroDistance() {
        // Arrange
        double distanceInKm = 0.0;
        double emissionInGramsPerKm = 142.0;

        // Act
        double result = calculator.calculate(distanceInKm, emissionInGramsPerKm);

        // Assert
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testCalculate_ZeroEmission() {
        // Arrange
        double distanceInKm = 289.0;
        double emissionInGramsPerKm = 0.0;

        // Act
        double result = calculator.calculate(distanceInKm, emissionInGramsPerKm);

        // Assert
        assertEquals(0.0, result, 0.001);
    }

    @Test
    void testCalculate_NegativeDistance() {
        // Arrange
        double distanceInKm = -289.0;
        double emissionInGramsPerKm = 142.0;

        // Act
        double result = calculator.calculate(distanceInKm, emissionInGramsPerKm);

        // Assert
        assertEquals(-41.038, result, 0.001); // -289 km * 142 g/km = -41.038 kg
    }

    @Test
    void testCalculate_NegativeEmission() {
        // Arrange
        double distanceInKm = 289.0;
        double emissionInGramsPerKm = -142.0;

        // Act
        double result = calculator.calculate(distanceInKm, emissionInGramsPerKm);

        // Assert
        assertEquals(-41.038, result, 0.001); // 289 km * -142 g/km = -41.038 kg
    }
}