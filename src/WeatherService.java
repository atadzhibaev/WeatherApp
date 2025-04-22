import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {

    private double latitude;
    private double longitude;

    private static String fetchData(String weatherApiUrl) throws Exception {

        HttpURLConnection connection = (HttpURLConnection) new URL(weatherApiUrl).openConnection();
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
    public WeatherData getWeatherData(int zipCode) throws Exception {
        String zipApiUrl = "https://api.zippopotam.us/us/" + zipCode;
        String zipResponse = fetchData(zipApiUrl);

        JSONObject zipJson = new JSONObject(zipResponse);
        JSONArray places = zipJson.getJSONArray("places");
        JSONObject place = places.getJSONObject(0);
        latitude = Double.parseDouble(place.getString("latitude"));
        longitude = Double.parseDouble(place.getString("longitude"));
        String weatherApiUrl = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
                latitude, longitude
        );

        String weatherResponse = fetchData(weatherApiUrl);
        JSONObject weatherJson = new JSONObject(weatherResponse);
        JSONObject currentWeather = weatherJson.getJSONObject("current_weather");

        double temperature = currentWeather.getDouble("temperature");
        double windSpeed = currentWeather.getDouble("windspeed");

        return new WeatherData(temperature, windSpeed);
    }
}
