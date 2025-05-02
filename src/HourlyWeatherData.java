public class HourlyWeatherData {
    private int weatherCode;
    private double temperature;

    public HourlyWeatherData(int weatherCode, double temperature) {
        this.weatherCode = weatherCode;
        this.temperature = temperature;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public double getTemperature() {
        return temperature;
    }
}
