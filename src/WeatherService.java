import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WeatherService {

    private static double latitude;
    private static double longitude;

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
    public static WeatherData getWeatherData(String zipCode) throws Exception {
        String zipApiUrl = "https://api.zippopotam.us/us/" + zipCode;
        String zipResponse = fetchData(zipApiUrl);

        JSONObject zipJson = new JSONObject(zipResponse);
        JSONArray places = zipJson.getJSONArray("places");
        JSONObject place = places.getJSONObject(0);
        latitude = Double.parseDouble(place.getString("latitude"));
        longitude = Double.parseDouble(place.getString("longitude"));
//        System.out.printf(
////                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
//                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&daily=weather_code,temperature_2m_max,temperature_2m_min&hourly=temperature_2m,weather_code&current=temperature_2m,weather_code,wind_speed_10m,wind_direction_10m,precipitation,relative_humidity_2m,apparent_temperature&timezone=America%%2FChicago",
//                latitude, longitude
//        );
        String weatherApiUrl = String.format(
//                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
//                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&daily=weather_code,temperature_2m_max,temperature_2m_min&hourly=temperature_2m,weather_code&current=temperature_2m,weather_code,wind_speed_10m,wind_direction_10m,precipitation,relative_humidity_2m,apparent_temperature&timezone=America%2FChicago",
                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&daily=weather_code,temperature_2m_max,temperature_2m_min&hourly=temperature_2m,weather_code&current=temperature_2m,weather_code,wind_speed_10m,wind_direction_10m,precipitation,relative_humidity_2m,apparent_temperature&timezone=America%%2FChicago",
                latitude, longitude
        );

        String weatherResponse = fetchData(weatherApiUrl);
        JSONObject weatherJson = new JSONObject(weatherResponse);
        JSONObject currentWeather = weatherJson.getJSONObject("current");

        double temperature = currentWeather.getDouble("temperature_2m");
        double windSpeed = currentWeather.getDouble("wind_speed_10m");
        int windDirection = currentWeather.getInt("wind_direction_10m");
        double humidity = currentWeather.getDouble("relative_humidity_2m");
        double feels_like = currentWeather.getDouble("apparent_temperature");

        //Taking out hourly information from the json file
        JSONObject hourlyForecastData = weatherJson.getJSONObject("hourly");
        JSONArray times = hourlyForecastData.getJSONArray("time");
        JSONArray temps = hourlyForecastData.getJSONArray("temperature_2m");
        JSONArray weather_code = hourlyForecastData.getJSONArray("weather_code");

        //Map/dictionary to hold all the hourly information
        Map<String, HourlyWeatherData> hourlyWeatherMap = new TreeMap<>();

        //creating hourly weather data based on the data in the arrays
        for (int i = 0; i < times.length(); i++) {
            String time = times.getString(i);
            double temp = temps.getDouble(i);
            int code = weather_code.getInt(i);

            hourlyWeatherMap.put(time, new HourlyWeatherData(code, temp));
        }

        //limit hourly forecast to 7 hours
        int counter = 0;
        for (Map.Entry<String, HourlyWeatherData> entry : hourlyWeatherMap.entrySet()) {
            if (!LocalDateTime.parse(entry.getKey()).isBefore(LocalDateTime.now()) && counter < 7) {
                System.out.println(entry.getKey() + " => " + entry.getValue().getWeatherCode() + " => " + entry.getValue().getTemperature());
                counter++;
            }
        }

        //Taking daily information from json file
        JSONObject dailyForecastData = weatherJson.getJSONObject("daily");
        JSONArray dates = dailyForecastData.getJSONArray("time");
        JSONArray daily_codes = dailyForecastData.getJSONArray("weather_code");
        JSONArray daily_temps_min = dailyForecastData.getJSONArray(("temperature_2m_min"));
        JSONArray daily_temps_max = dailyForecastData.getJSONArray(("temperature_2m_max"));

        //Map to hold daily weather information
        Map<String, DailyWeatherData> DailyWeatherMap = new TreeMap<>();

        //creating daily weather data and associating them with a date
        for (int i = 0; i < 7; i++) {
            String date = dates.getString(i);
            double temp_min = daily_temps_min.getDouble(i);
            double temp_max = daily_temps_max.getDouble(i);
            int daily_code = daily_codes.getInt(i);

            DailyWeatherMap.put(date, new DailyWeatherData(temp_min, temp_max, daily_code));
        }

        return new WeatherData(temperature, windSpeed, windDirection, humidity, feels_like, hourlyWeatherMap, DailyWeatherMap);
    }
}
