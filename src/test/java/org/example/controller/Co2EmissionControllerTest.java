package org.example.controller;

import org.example.Application;
import org.example.exception.CalculationException;
import org.example.model.TransportationMethod;
import org.example.service.EmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class Co2EmissionControllerTest {

    @Mock
    private EmissionService mockEmissionService;

    private EmissionController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new Co2EmissionController();
        controller.setEmissionService(mockEmissionService);
    }

    @Test
    public void testCalculateEmission_Success() throws CalculationException {
        // Arrange
        String startCity = "Berlin";
        String endCity = "Paris";
        String transportationMethod = "diesel-car-medium";
        double expectedEmission = 179.74;

        when(mockEmissionService.calculateEmission(startCity, endCity, TransportationMethod.DIESEL_CAR_MEDIUM.getCo2EmissionPerKm()))
                .thenReturn(expectedEmission);
        // Act
        double actualEmission = controller.calculateEmission(startCity, endCity, transportationMethod);

        // Assert
        assertEquals(expectedEmission, actualEmission, 0.001);
    }

    @Test
    void testRun_InvalidTransportationMethod() {
        // Arrange
        String startCity = "Berlin";
        String endCity = "Paris";
        String method = "invalid-method";


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.calculateEmission(startCity, endCity, method));
    }

    @Test
    void testRun_MissingStartCity() {
        // Arrange
        String startCity = "";
        String endCity = "Paris";
        String method = "invalid-car-medium";


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.calculateEmission(startCity, endCity, method));
    }

    @Test
    void testRun_MissingEndCity() {
        // Arrange
        String startCity = "Berlin";
        String endCity = "";
        String method = "invalid-car-medium";


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.calculateEmission(startCity, endCity, method));
    }

    @Test
    void testRun_MissingTransportationMethod() {
        // Arrange
        String startCity = "Berlin";
        String endCity = "Paris";
        String method = "";


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> controller.calculateEmission(startCity, endCity, method));
    }


    @Test
    public void testCalculateEmission_ThrowsCalculationException() throws CalculationException {
        // Arrange
        String startCity = "Berlin";
        String endCity = "Paris";
        String method = "diesel-car-medium";

        when(mockEmissionService.calculateEmission(startCity, endCity, TransportationMethod.DIESEL_CAR_MEDIUM.getCo2EmissionPerKm()))
                .thenThrow(new CalculationException("Error occurred"));

        // Act & Assert
        assertThrows(CalculationException.class, () -> {
            controller.calculateEmission(startCity, endCity, method);
        });
    }
}