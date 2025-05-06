import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WeatherData {
    private String city;
    private String state;
    private double temperature;
    private int weather_code;
    private double windspeed;
    private double humidity;
    private double feels_like_temp;
    private int wind_direction;
    private Map<String, HourlyWeatherData> hourlyWeatherMap = new TreeMap<>();
    private Map<String, DailyWeatherData> DailyWeatherMap = new TreeMap<>();

    public WeatherData(String city, String state, double temperature, int weather_code, double windspeed,
                       int wind_direction, double humidity, double feels_like_temp, Map<String, HourlyWeatherData> hourlyWeatherMap, Map<String, DailyWeatherData> DailyWeatherMap) {
        this.city = city;
        this.state = state;
        this.temperature = temperature;
        this.weather_code = weather_code;
        this.windspeed = windspeed;
        this.wind_direction = wind_direction;
        this.humidity = humidity;
        this.feels_like_temp = feels_like_temp;
        this.hourlyWeatherMap = hourlyWeatherMap;
        this.DailyWeatherMap = DailyWeatherMap;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getWeather_code() {return weather_code;}

    public double getWindspeed() {
        return windspeed;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getFeels_like_temp() {
        return feels_like_temp;
    }

    public int getWind_direction() {
        return wind_direction;
    }

    public Map<String, HourlyWeatherData> getHourlyWeatherMap() {
        return hourlyWeatherMap;
    }

    public Map<String, DailyWeatherData> getDailyWeatherMap() {
        return DailyWeatherMap;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temperature=" + temperature +
                ", windspeed=" + windspeed +
                ", wind direction=" + wind_direction +
                '}';
    }
}
