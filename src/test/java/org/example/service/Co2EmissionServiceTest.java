package org.example.service;

import org.example.client.ServiceClient;
import org.example.core.Calculator;
import org.example.exception.CalculationException;
import org.example.exception.ServiceClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Co2EmissionServiceTest {

    @Mock
    private ServiceClient serviceClient;

    @Mock
    private Calculator calculator;

    @InjectMocks
    private Co2EmissionService co2EmissionService;

    private final double[] startCoordinates = {52.5200, 13.4050}; // Berlin
    private final double[] endCoordinates = {53.5511, 9.9937}; // Hamburg
    private final double distance = 289.0; // Distance in km
    private final double emissionInGramsPerKm = 142.0; // Emission for diesel-car-small

    @BeforeEach
    void setUp() throws ServiceClientException {
        // Mock successful coordinate fetching
        lenient().when(serviceClient.getCoordinates("Berlin")).thenReturn(startCoordinates);
        lenient().when(serviceClient.getCoordinates("Hamburg")).thenReturn(endCoordinates);

        // Mock successful distance calculation
        lenient().when(serviceClient.getDistance(startCoordinates, endCoordinates)).thenReturn(distance);

        // Mock successful emission calculation
        lenient().when(calculator.calculate(distance, emissionInGramsPerKm)).thenReturn(41.038); // 289 km * 142 g/km = 41.038 kg
    }

    @Test
    void testCalculateEmission_Success() throws CalculationException, ServiceClientException {
        // Act
        double result = co2EmissionService.calculateEmission("Berlin", "Hamburg", emissionInGramsPerKm);

        // Assert
        assertEquals(41.038, result, 0.001); // Verify the result with a delta for floating-point precision
        verify(serviceClient, times(1)).getCoordinates("Berlin");
        verify(serviceClient, times(1)).getCoordinates("Hamburg");
        verify(serviceClient, times(1)).getDistance(startCoordinates, endCoordinates);
        verify(calculator, times(1)).calculate(distance, emissionInGramsPerKm);
    }

    @Test
    void testCalculateEmission_CalculationException_FetchingStartCoordinates() throws ServiceClientException {
        // Arrange
        when(serviceClient.getCoordinates("Berlin")).thenThrow(new ServiceClientException("API Error"));

        // Act & Assert
        CalculationException exception = assertThrows(CalculationException.class,
                () -> co2EmissionService.calculateEmission("Berlin", "Hamburg", emissionInGramsPerKm));

        assertEquals("Error occurred during emission calculation: org.example.exception.CalculationException: API Error", exception.getMessage());
        assertTrue(exception instanceof CalculationException);
    }

    @Test
    void testCalculateEmission_CalculationException_FetchingEndCoordinates() throws ServiceClientException {
        // Arrange
        when(serviceClient.getCoordinates("Hamburg")).thenThrow(new ServiceClientException("API Error"));

        // Act & Assert
        CalculationException exception = assertThrows(CalculationException.class,
                () -> co2EmissionService.calculateEmission("Berlin", "Hamburg", emissionInGramsPerKm));

        assertEquals("Error occurred during emission calculation: org.example.exception.CalculationException: API Error", exception.getMessage());
        assertTrue(exception instanceof CalculationException);
    }

    @Test
    void testCalculateEmission_CalculationException_CalculatingDistance() throws ServiceClientException {
        // Arrange
        when(serviceClient.getDistance(startCoordinates, endCoordinates)).thenThrow(new ServiceClientException("API Error"));

        // Act & Assert
        CalculationException exception = assertThrows(CalculationException.class,
                () -> co2EmissionService.calculateEmission("Berlin", "Hamburg", emissionInGramsPerKm));

        assertEquals("Error occurred during emission calculation: org.example.exception.CalculationException: API Error", exception.getMessage());
        assertTrue(exception instanceof CalculationException);
    }

    @Test
    void testCalculateEmission_InterruptedException() throws ServiceClientException, ExecutionException, InterruptedException {
        // Arrange
        when(serviceClient.getCoordinates(anyString())).thenAnswer(invocation -> {
            throw new InterruptedException("Thread interrupted");
        });

        // Act & Assert
        CalculationException exception = assertThrows(CalculationException.class,
                () -> co2EmissionService.calculateEmission("Berlin", "Hamburg", emissionInGramsPerKm));

        assertEquals("Error occurred during emission calculation: java.lang.InterruptedException: Thread interrupted", exception.getMessage());
        assertTrue(exception instanceof CalculationException);
    }

    @Test
    void testCalculateEmission_ExecutionException() throws ServiceClientException, ExecutionException, InterruptedException {
        // Arrange
        when(serviceClient.getCoordinates(anyString())).thenAnswer(invocation -> {
            throw new ExecutionException("Execution failed", new RuntimeException("Error"));
        });

        // Act & Assert
        CalculationException exception = assertThrows(CalculationException.class,
                () -> co2EmissionService.calculateEmission("Berlin", "Hamburg", emissionInGramsPerKm));

        assertEquals("Error occurred during emission calculation: java.util.concurrent.ExecutionException: Execution failed", exception.getMessage());
        assertTrue(exception instanceof CalculationException);
    }

    @Test
    void testCalculateEmission_ZeroDistance() throws ServiceClientException, CalculationException {
        // Arrange
        when(serviceClient.getDistance(startCoordinates, endCoordinates)).thenReturn(0.0);
        when(calculator.calculate(0.0, emissionInGramsPerKm)).thenReturn(0.0);

        // Act
        double result = co2EmissionService.calculateEmission("Berlin", "Hamburg", emissionInGramsPerKm);

        // Assert
        assertEquals(0.0, result, 0.001); // Verify zero emission for zero distance
    }

    @Test
    void testCalculateEmission_NegativeEmissionValue() throws ServiceClientException, CalculationException {
        // Arrange
        double negativeEmission = -142.0;
        when(calculator.calculate(distance, negativeEmission)).thenReturn(-41.038); // 289 km * -142 g/km = -41.038 kg

        // Act
        double result = co2EmissionService.calculateEmission("Berlin", "Hamburg", negativeEmission);

        // Assert
        assertEquals(-41.038, result, 0.001); // Verify the result with a delta for floating-point precision
        verify(serviceClient, times(1)).getCoordinates("Berlin");
        verify(serviceClient, times(1)).getCoordinates("Hamburg");
        verify(serviceClient, times(1)).getDistance(startCoordinates, endCoordinates);
        verify(calculator, times(1)).calculate(distance, negativeEmission);
    }
}