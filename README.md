# CO2 Emission Calculator

Calculates the CO2 emissions for a trip between two cities using different transportation methods.

## Prerequisites

- Java 17
- Maven
- ORS (OpenRouteService) API Key(NOTE:  Please create a free account to get an API
  Token from [here](https://openrouteservice.org/))

## Set Environment variable ORS_TOKEN
### Windows
1. Open Command Prompt and run the following command:
    ```bat
        setx ORS_TOKEN "your_api_key_here"
    ```
### macOS/Linux
1. Open Terminal and add the following line to your shell configuration file (e.g., .bashrc, .zshrc, or .profile):  
    ```sh
        export ORS_TOKEN="your_api_key_here"
    ```
2. Apply the changes by running:  
    ```shell
        source ~/.bashrc  # or source ~/.zshrc or source ~/.profile
    ```

**NOTE**: Replace **"your_api_key_here"** with your actual ORS API key.

## Building the Project

1. Build the project:
    ```sh
    mvn clean package
    ```

## Running the Application
**Note**: 
- Use `=` to separate keys and values in the parameters. Using spaces will lead to incorrect results, as the script will interpret them as separate parameters.
- In case the parameter value contains spaces, use quotes as shown in the examples below.
- For '--transportation-method' parameter, please use the following Transportation methods:
  - Small cars:
     - `diesel-car-small`
     - `petrol-car-small`
     - `plugin-hybrid-car-small`
     - `electric-car-small`
  - Medium cars:
     - `diesel-car-medium`
     - `petrol-car-medium`
     - `plugin-hybrid-car-medium`
     - `electric-car-medium`
  - Large cars:
     - `diesel-car-large`
     - `petrol-car-large`
     - `plugin-hybrid-car-large`
     - `electric-car-large`
  - `bus-default`
  - `train-default`

### Windows

1. Run the script:
    ```bat
    .\co2-calculator.bat --start="Bad Homburg" --end=Berlin --transportation-method=diesel-car-medium
    ```

### macOS/Linux

1. Make the `co2-calculator.sh` script executable:
    ```sh
    chmod +x co2-calculator.sh
    ```

2. Run the script:
    ```sh
    ./co2-calculator.sh --start="Bad Homburg" --end=Berlin --transportation-method=diesel-car-medium
    ```


## License

This project is licensed under the MIT License.