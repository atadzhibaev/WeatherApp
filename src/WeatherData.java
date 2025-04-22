public class WeatherData {
    private double temperature;
    private double windspeed;
    boolean isInMiles;

    public WeatherData(double temperature, double windspeed) {
        isInMiles = false;
        this.temperature = temperature;
        this.windspeed = windspeed;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temperature=" + temperature +
                ", windspeed=" + windspeed +
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
