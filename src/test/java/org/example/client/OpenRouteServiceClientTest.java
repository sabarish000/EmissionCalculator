package org.example.client;

import okhttp3.*;
import org.example.exception.ServiceClientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

class OpenRouteServiceClientTest {

    @Mock
    private OkHttpClient mockClient;

    @Mock
    private Call mockCall;

    @Mock
    private Response mockResponse;

    @Mock
    private ResponseBody mockResponseBody;

    private OpenRouteServiceClient openRouteServiceClient;
    private final String apiKey = "test-api-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        openRouteServiceClient = new OpenRouteServiceClient(apiKey, mockClient);
    }

    @Test
    void testGetCoordinates_Success() throws IOException, ServiceClientException {
        // Arrange
        String jsonResponse = "{\"features\":[{\"geometry\":{\"coordinates\":[13.4050,52.5200]}}]}";
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.byteStream()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act
        double[] coordinates = openRouteServiceClient.getCoordinates("Berlin");

        // Assert
        assertArrayEquals(new double[]{52.5200, 13.4050}, coordinates, 0.001);
//        assertEquals(52.524932, coordinates[0], 0.001);
//        assertEquals(13.407032, coordinates[1], 0.001);
    }

    @Test
    void testGetCoordinates_Failure() throws IOException {
        // Arrange
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act & Assert
        ServiceClientException exception = assertThrows(ServiceClientException.class,
                () -> openRouteServiceClient.getCoordinates("Berlin"));
        assertTrue(exception.getMessage().contains("Unexpected code"));
    }

    @Test
    void testGetCoordinates_Exception() throws IOException {
        // Arrange
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(new IOException("Network error"));

        // Act & Assert
        ServiceClientException exception = assertThrows(ServiceClientException.class,
                () -> openRouteServiceClient.getCoordinates("Berlin"));
        assertTrue(exception.getMessage().contains("Network error"));
    }

    @Test
    void testGetCoordinates_NumberFormatException() throws IOException {
        // Arrange
        String jsonResponse = "{\"features\":[{\"geometry\":{\"coordinates\":[\"invalid\", \"invalid\"]}}]}";
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.byteStream()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act & Assert
        ServiceClientException exception = assertThrows(ServiceClientException.class,
                () -> openRouteServiceClient.getCoordinates("Berlin"));
        assertTrue(exception.getMessage().contains("Coordinates not found for city"));
    }

    @Test
    void testGetCoordinates_InvalidCoordinates() throws IOException {
        // Arrange
        String jsonResponse = "{\"features\":[{\"geometry\":{\"coordinates\":[0, 0]}}]}";
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.byteStream()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act & Assert
        ServiceClientException exception = assertThrows(ServiceClientException.class,
                () -> openRouteServiceClient.getCoordinates("Berlin"));
        assertTrue(exception.getMessage().contains("Coordinates not found for city"));
    }

    @Test
    void testGetDistance_Success() throws IOException, ServiceClientException {
        // Arrange
        String jsonResponse = "{\"distances\":[[0,289000]]}";
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.byteStream()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act
        double distance = openRouteServiceClient.getDistance(new double[]{52.5200, 13.4050}, new double[]{53.5511, 9.9937});

        // Assert
        assertEquals(289.0, distance, 0.0001);
    }

    @Test
    void testGetDistance_Failure() throws IOException {
        // Arrange
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act & Assert
        ServiceClientException exception = assertThrows(ServiceClientException.class,
                () -> openRouteServiceClient.getDistance(new double[]{52.5200, 13.4050}, new double[]{53.5511, 9.9937}));
        assertTrue(exception.getMessage().contains("Unexpected code"));
    }

    @Test
    void testGetDistance_Exception() throws IOException {
        // Arrange
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(new IOException("Network error"));

        // Act & Assert
        ServiceClientException exception = assertThrows(ServiceClientException.class,
                () -> openRouteServiceClient.getDistance(new double[]{52.5200, 13.4050}, new double[]{53.5511, 9.9937}));
        assertTrue(exception.getMessage().contains("Network error"));
    }

    @Test
    void testGetDistance_NumberFormatException() throws IOException {
        // Arrange
        String jsonResponse = "{\"distances\":[[0,\"invalid\"]]}";
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.byteStream()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act & Assert
        ServiceClientException exception = assertThrows(ServiceClientException.class,
                () -> openRouteServiceClient.getDistance(new double[]{52.5200, 13.4050}, new double[]{53.5511, 9.9937}));
        assertTrue(exception.getMessage().contains("Distance not found between the coordinates or Invalid start/end coordinates"));
    }

    @Test
    void testGetDistance_InvalidCoordinates() throws IOException, ServiceClientException {
        // Arrange
        String jsonResponse = "{\"distances\":[[0,0]]}";
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(mockResponseBody);
        when(mockResponseBody.byteStream()).thenReturn(new java.io.ByteArrayInputStream(jsonResponse.getBytes()));
        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Act & Assert
        ServiceClientException exception = assertThrows(ServiceClientException.class,
                () -> openRouteServiceClient.getDistance(new double[]{52.5200, 13.4050}, new double[]{53.5511, 9.9937}));
        assertTrue(exception.getMessage().contains("Distance not found between the coordinates or Invalid start/end coordinates"));
    }
}