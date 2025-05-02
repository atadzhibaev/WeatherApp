import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WeatherData {
    private double temperature;
    private double windspeed;
    private double humidity;
    private double feels_like_temp;
    private int wind_direction;
    private Map<String, HourlyWeatherData> hourlyWeatherMap = new TreeMap<>();
    private Map<String, DailyWeatherData> DailyWeatherMap = new TreeMap<>();
    boolean isInMiles;

    public WeatherData(double temperature, double windspeed, int wind_direction, double humidity, double feels_like_temp, Map<String, HourlyWeatherData> hourlyWeatherMap, Map<String, DailyWeatherData> DailyWeatherMap) {
        isInMiles = false;
        this.temperature = temperature;
        this.windspeed = windspeed;
        this.wind_direction = wind_direction;
        this.humidity = humidity;
        this.feels_like_temp = feels_like_temp;
        this.hourlyWeatherMap = hourlyWeatherMap;
        this.DailyWeatherMap = DailyWeatherMap;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

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

    @Override
    public String toString() {
        return "WeatherData{" +
                "temperature=" + temperature +
                ", windspeed=" + windspeed +
                ", wind direction=" + wind_direction +
                '}';
    }

    public void ConvertMetrics() {
        if (!isInMiles) {
            setWindspeed(windspeed/1.609);
            setTemperature(temperature*((double) 9 /5)+32);
            isInMiles = true;
        }
        else {
            setWindspeed(windspeed*1.609);
            setTemperature((temperature-32)*(double) 9 /5);
            isInMiles = false;
        }
    }
}
