package org.example;

import org.example.controller.EmissionController;
import org.example.model.TransportationMethod;
import org.example.service.EmissionService;
import org.example.utils.ArgumentParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import sun.misc.Unsafe;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SystemStubsExtension.class)
class ApplicationTest {

        @Mock
        private EmissionService mockEmissionService;

        @Mock
        private EmissionController mockEmissionController;

        @SystemStub
        private EnvironmentVariables environmentVariables;

        @BeforeEach
        void setUp() throws Exception {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        void testRun_Success() throws Exception {
                // Arrange
                String[] args = {"--start Berlin", "--end Paris", "--transportation-method diesel-car-medium"};
                environmentVariables.set("ORS_TOKEN", "test-token");

                Map<String, String> arguments = ArgumentParser.parseArguments(args);
                when(mockEmissionController.calculateEmission("Berlin", "Paris", "diesel-car-medium"))
                        .thenReturn(41.038);

                // Mock the getEmissionService method
                try (var mockedApplication = mockStatic(Application.class)) {
                        mockedApplication.when(() -> Application.getEmissionService(anyString())).thenReturn(mockEmissionService);

                        Application app = new Application();
                        app.setEmmissionController(mockEmissionController);
                        when(mockEmissionController.calculateEmission("Berlin", "Paris", "diesel-car-medium"))
                                .thenReturn(41.038);

                        // Act
                        app.run(args);

                        // Assert
                        verify(mockEmissionController).calculateEmission("Berlin", "Paris", "diesel-car-medium");
                }
        }

        @Test
        void testRun_InvalidArguments_start() {
                // Arrange
                String[] args = {"--start ", "--end Berlin", "--transportation-method diesel-car-medium"};

                Application app = new Application();
                app.setEmmissionController(mockEmissionController);

                // Act & Assert
                assertThrows(IllegalArgumentException.class, () -> app.run(args));
        }

        @Test
        void testRun_InvalidArguments_end() {
                // Arrange
                String[] args = {"--start Berlin", "--end ", "--transportation-method diesel-car-medium"};

                Application app = new Application();
                app.setEmmissionController(mockEmissionController);

                // Act & Assert
                assertThrows(IllegalArgumentException.class, () -> app.run(args));
        }

        @Test
        void testRun_InvalidArguments_transportation_method() {
                // Arrange
                String[] args = {"--start Berlin", "--end Paris", "--transportation-method "};

                Application app = new Application();
                app.setEmmissionController(mockEmissionController);

                // Act & Assert
                assertThrows(IllegalArgumentException.class, () -> app.run(args));
        }


        @Test
        void testRun_MissingApiKey() {
                // Arrange
                String[] args = {"--start Berlin", "--end Paris", "--transportation-method=diesel-car-medium"};
                environmentVariables.set("ORS_TOKEN", "");

                Application app = new Application();
                app.setEmmissionController(mockEmissionController);

                // Act & Assert
                assertThrows(IllegalArgumentException.class, () -> app.run(args));
        }
}