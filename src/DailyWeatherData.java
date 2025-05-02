public class DailyWeatherData {
    private double temp_min;
    private double temp_max;
    private int weather_code;

    public DailyWeatherData(double temp_min, double temp_max, int weather_code) {
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.weather_code = weather_code;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public int getWeather_code() {
        return weather_code;
    }
}
