package org.example.utils;

import org.example.model.TransportationMethod;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentParserTest {

    @Test
    void testParseArguments_Success_with_equals() {
        // Arrange
        String[] args = {"--start=Berlin", "--end=Paris", "--transportation-method=diesel-car-medium"};

        // Act
        Map<String, String> result = ArgumentParser.parseArguments(args);

        // Assert
        assertEquals(3, result.size());
        assertEquals("Berlin", result.get("start"));
        assertEquals("Paris", result.get("end"));
        assertEquals("diesel-car-medium", result.get("transportation-method"));
    }

    @Test
    void testParseArguments_Success_with_spaces() {
        // Arrange
        String[] args = {"--start Berlin", "--end Paris", "--transportation-method diesel-car-medium"};

        // Act
        Map<String, String> result = ArgumentParser.parseArguments(args);

        // Assert
        assertEquals(3, result.size());
        assertEquals("Berlin", result.get("start"));
        assertEquals("Paris", result.get("end"));
        assertEquals("diesel-car-medium", result.get("transportation-method"));
    }

    @Test
    void testParseArguments_Success_with_equals_spaces() {
        // Arrange
        String[] args = {"--start=Berlin", "--end Paris", "--transportation-method diesel-car-medium"};

        // Act
        Map<String, String> result = ArgumentParser.parseArguments(args);

        // Assert
        assertEquals(3, result.size());
        assertEquals("Berlin", result.get("start"));
        assertEquals("Paris", result.get("end"));
        assertEquals("diesel-car-medium", result.get("transportation-method"));
    }

    @Test
    void testParseArguments_Success_with_spaces_in_values() {
        // Arrange
        String[] args = {"--start=Los Angeles", "--end New York", "--transportation-method diesel-car-medium"};

        // Act
        Map<String, String> result = ArgumentParser.parseArguments(args);

        // Assert
        assertEquals(3, result.size());
        assertEquals("Los Angeles", result.get("start"));
        assertEquals("New York", result.get("end"));
        assertEquals("diesel-car-medium", result.get("transportation-method"));
    }

    @Test
    void testParseArguments_InvalidArgument_with_equals_spaces() {
        // Arrange
        String[] args = {"--start =", "--end Paris", "--transportation-method diesel-car-medium"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentParser.parseArguments(args);
        });
        assertEquals("Invalid argument: --start =", exception.getMessage());
    }

    @Test
    void testParseArguments_InvalidArgument_transportation_method() {
        // Arrange
        String[] args = {"--start Berlin", "--end Paris", "--transportation-method"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentParser.parseArguments(args);
        });
        assertEquals("Invalid argument: --transportation-method", exception.getMessage());
    }

    @Test
    void testParseArguments_InvalidArgument_start() {
        // Arrange
        String[] args = {"--start", "--end Paris", "--transportation-method diesel-car-medium"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentParser.parseArguments(args);
        });
        assertEquals("Invalid argument: --start", exception.getMessage());
    }

    @Test
    void testParseArguments_InvalidArgument_end() {
        // Arrange
        String[] args = {"--start Berlin", "--end", "--transportation-method diesel-car-medium"};

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentParser.parseArguments(args);
        });
        assertEquals("Invalid argument: --end", exception.getMessage());
    }

    @Test
    void testParseArguments_InvalidArgument() {
        String[] args = {"--invalidArgument"};
        assertThrows(IllegalArgumentException.class, () -> ArgumentParser.parseArguments(args));
    }

    @Test
    void testGetTransportationMethod_Success() {
        // Act
        TransportationMethod result = ArgumentParser.getTransportationMethod("diesel-car-medium");

        // Assert
        assertEquals(TransportationMethod.DIESEL_CAR_MEDIUM, result);
    }

    @Test
    void testGetTransportationMethod_InvalidMethod() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ArgumentParser.getTransportationMethod("invalid-method");
        });
    }
}