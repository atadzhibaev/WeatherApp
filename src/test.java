import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

public class test {
    public static void main(String[] args) {
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
            String result = response.toString();

            JSONObject iplocation = new JSONObject(result);
//            JSONArray places = iplocation.getJSONArray("places");
//            JSONObject place = places.getJSONObject(0);
//            JSONObject locinfo = iplocation.getJSONObject("Location Info");
            int zipcode = Integer.parseInt(iplocation.getString("zip"));
            String city = iplocation.getString("city");
            String state = iplocation.getString("region");
            double latitude = iplocation.getDouble("lat");
            double longitude = iplocation.getDouble("lon");

            HttpURLConnection connection = (HttpURLConnection) new URL( String.format(
                    "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
                    latitude, longitude)).openConnection();
            connection.setRequestMethod("GET");

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String inputLine;
            StringBuilder fullResponse = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                fullResponse.append(inputLine);
            }
            in.close();

            JSONObject weatherresponse = new JSONObject(fullResponse.toString());
            JSONObject currentWeather = weatherresponse.getJSONObject("current_weather");

            double temperature = currentWeather.getDouble("temperature");
            double windSpeed = currentWeather.getDouble("windspeed");
            System.out.println(city + ", " + state + "  " + zipcode);
            System.out.printf("Temperature: %.1f°C%n", temperature);
            System.out.printf("Wind Speed: %.1f km/h%n", windSpeed);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
