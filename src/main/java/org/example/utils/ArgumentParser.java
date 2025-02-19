package org.example.utils;

import org.example.model.TransportationMethod;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    private static final String SPACE_DELIMITER = " ";
    private static final String EQUAL_DELIMITER = "=";
    public static final String ARGS_START = "--start";
    public static final String ARGS_END = "--end";
    public static final String ARGS_TRANSPORTATION_METHOD = "--transportation-method";


    public static Map<String, String> parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (String arg : args) {
            String refinedArg = arg
                    .replaceFirst(ARGS_START+ SPACE_DELIMITER, ARGS_START+ EQUAL_DELIMITER)
                    .replaceFirst(ARGS_END+ SPACE_DELIMITER, ARGS_END+ EQUAL_DELIMITER)
                    .replaceFirst(ARGS_TRANSPORTATION_METHOD+ SPACE_DELIMITER, ARGS_TRANSPORTATION_METHOD+ EQUAL_DELIMITER);
            String[] parts = refinedArg.split("=");
            if (parts.length == 2) {
                arguments.put(parts[0].replace("--", ""), parts[1]);
            } else {
                throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }
        return arguments;
    }

    public static TransportationMethod getTransportationMethod(String method) {
        return TransportationMethod.valueOf(method.toUpperCase().replace("-", "_"));
    }
}
