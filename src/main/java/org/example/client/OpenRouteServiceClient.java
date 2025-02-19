package org.example.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.exception.ServiceClientException;

import java.io.IOException;

public class OpenRouteServiceClient implements ServiceClient{
    private static final String GEOCODE_URL = "https://api.openrouteservice.org/geocode/search";
    private static final String DISTANCE_MATRIX_URL = "https://api.openrouteservice.org/v2/matrix/driving-car";
    private final String API_KEY;
    private final OkHttpClient client;

    public OpenRouteServiceClient(String apiKey, OkHttpClient client) {
        this.API_KEY = apiKey;
        this.client = client;
    }

    @Override
    public double[] getCoordinates(String city) throws ServiceClientException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GEOCODE_URL).newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("text", city)
                .addQueryParameter("layers", "locality");
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body().byteStream());
            try {
                double latitude = root.at("/features/0/geometry/coordinates/1").asDouble();
                double longitude = root.at("/features/0/geometry/coordinates/0").asDouble();
                if(latitude == 0 && longitude == 0) {
                    throw new ServiceClientException("Coordinates not found for city " + city);
                }
                return new double[]{latitude, longitude};
            } catch (NumberFormatException nfe) {
                throw new ServiceClientException("Coordinates not found for city " + city);
            }
        } catch (IOException e) {
            throw new ServiceClientException("Error occurred during fetching coordinates of city " + city + " : " + e.getMessage());
        }
    }

    @Override
    public double getDistance(double[] startCoordinates, double[] endCoordinates) throws ServiceClientException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = String.format("{\"locations\":[[%f,%f],[%f,%f]],\"metrics\":[\"distance\"]}",
                startCoordinates[1], startCoordinates[0], endCoordinates[1], endCoordinates[0]);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(DISTANCE_MATRIX_URL)
                .post(body)
                .addHeader("Authorization", API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body().byteStream());
            try {
                double distanceInMeters = root.at("/distances/0/1").asDouble(-1);
                if(distanceInMeters <= 0) {
                    throw new ServiceClientException("Distance not found between the coordinates or Invalid start/end coordinates");
                }
                return  distanceInMeters/ 1000; // Convert meters to kilometers
            } catch (NumberFormatException nfe) {
                throw new ServiceClientException("Error occurred during fetching distance" + nfe.getMessage());
            }
        } catch (IOException e) {
            throw new ServiceClientException("Error occurred during fetching distance" + e.getMessage());
        }
    }
}
