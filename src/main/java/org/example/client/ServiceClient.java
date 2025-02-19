package org.example.client;

import org.example.exception.ServiceClientException;

public interface ServiceClient {

    double[] getCoordinates(String city) throws ServiceClientException;
    double getDistance(double[] startCoordinates, double[] endCoordinates) throws ServiceClientException;
}
