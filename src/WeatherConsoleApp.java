import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.json.JSONArray;

public class WeatherConsoleApp {

    // Regex for US Zip Code
    private static final String ZIP_CODE_REGEX = "^[0-9]{5}(?:-[0-9]{4})?$";

    public static void main(String[] args) throws Exception {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
//            System.out.println("IP Address of your device: " + localhost.getHostAddress());
//            String ip = "8.8.8.8";
            URL url = new URL("http://ip-api.com/json/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            System.out.println("Location Info: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a U.S. Zip Code: ");
        String zipCode = scanner.nextLine();

        if (!Pattern.matches(ZIP_CODE_REGEX, zipCode)) {
            System.out.println("Invalid Zip Code Format!");
            return;
        }

        WeatherData response = WeatherService.getWeatherData(zipCode);
        System.out.println(response);

        try {

//            // Step 1: Get Coordinates
//            String zipApiUrl = "https://api.zippopotam.us/us/" + zipCode;
//            String zipResponse = fetchData(zipApiUrl);
//
//            JSONObject zipJson = new JSONObject(zipResponse);
//            JSONArray places = zipJson.getJSONArray("places");
//            JSONObject place = places.getJSONObject(0);
//            double latitude = Double.parseDouble(place.getString("latitude"));
//            double longitude = Double.parseDouble(place.getString("longitude"));
//
//            System.out.printf("Coordinates for %s: Lat=%.4f, Lon=%.4f%n", zipCode, latitude, longitude);
//
//            // Step 2: Get Weather Data
//            String weatherApiUrl = String.format(
//                    "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
//                    latitude, longitude
//            );
//            String weatherResponse = fetchData(weatherApiUrl);
//            JSONObject weatherJson = new JSONObject(weatherResponse);
//            JSONObject currentWeather = weatherJson.getJSONObject("current_weather");
//
//            double temperature = currentWeather.getDouble("temperature");
//            double windSpeed = currentWeather.getDouble("windspeed");
//            String conditionCode = currentWeather.get("weathercode").toString(); // Could be mapped to a description
//
//            // Step 3: Display Results
//            System.out.println("Weather Information:");
//            System.out.printf("Temperature: %.1f°C%n", temperature);
//            System.out.printf("Wind Speed: %.1f km/h%n", windSpeed);
//            System.out.println("Weather Code: " + conditionCode);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String fetchData(String apiUrl) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder fullResponse = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            fullResponse.append(inputLine);
        }
        in.close();
        return fullResponse.toString();
    }
}

