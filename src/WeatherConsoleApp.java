//-----NOTE  - THIS CODE WILL NOT COMPILE AND IS JUST
//MEANT AS AN EXAMPLE FOR YOU TO USE TO GET STARTED.
//PLEASE DO NOT SUBMIT THIS CODE IN YOUR FINAL PROJECT,
//HOWEVER USE IT TO GENERATE IDEAS. YOU MAY USE THE METHOD(S) BELOW


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;//Norfolk NE Lat & Long (CHANGE AS YOU WISH)
import java.nio.charset.StandardCharsets;

static final double latitude = 42.0285;
static final double longitude = -97.4170;

// String use for API Request
static String urlString = String.format("https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true", latitude, longitude);


public static <JSONObject> void main(String[] args) {
    try {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

// Parse the JSON respons
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject currentWeather = jsonResponse.getJSONObject("current_weather");

// Calls to API
        double temperatureCelsius = currentWeather.getDouble("temperature");
        int isDayOrNight = currentWeather.getInt("is_day");
        double windDirection = currentWeather.getDouble("winddirection");
        double windSpeed = currentWeather.getDouble("windspeed");

// Sout json data
        System.out.println(currentWeather.toString());

// Convert Celsius to Fahrenheit
        //double temperatureFahrenheit = getTemperatureFahrenheit(temperatureCelsius);

// Display Output
        System.out.println("Current Temperature in Norfolk, NE: " + temperatureCelsius + "\u00B0C"); //MAY NOT LOOK RIGHT IN YOUR CONSOLE (UTF-8 ENCODINGS)
        //System.out.println("Current Temperature in Norfolk, NE: " + temperatureFahrenheit + "\u00B0F"); //MAY NOT
        // LOOK RIGHT IN YOUR CONSOLE (UTF-8 ENCODINGS)

//--JUST A SIMPLE EXAMPLE, PLEASE ENHANCE THIS
// Check if it's day or night out 0 = night, 1 = day
        if (isDayOrNight == 0) {
            System.out.println("It's Night time");
        }
        else {
            System.out.println("It's Day time");
        }

        //---CHECK OUT THE METHOD BELOW FOR CALC WIND DIRECTION. YOU MAY COPY VERBATIM!
        // Check wind direction
        System.out.println("Wind Direction is: " + getCompassDirection(windDirection));
        //System.out.println("Current Wind Speed in Norfolk, NE: " + convertKilometersPerHour_MilesPerHour(windSpeed));


    } catch (Exception e) {
        e.printStackTrace();
    }
}

//                    else {
//                            System.out.println("Sorry, the latitude or longitude is not in the correct format or is an invalid number.");
//        }
//
//                }

// Method to convert temp from celsius to fahrenheit
//BUILD THIS METHOD IF YOU WOULD LIKE TO USE IT

// Method to convert degrees (angles) to wind direction
private static String getCompassDirection(double angle){
    String[] directions = {"North","Northeast","East","Southeast","South","Southeast","West","Northwest","North"};
    return directions[(int)Math.round(((angle % 360) / 45))];
}

// Convert from km/h to MPH
//fill out below

// Validate latitude and longitude ranges using boolean method returns

//BUILD MANY MORE METHODS AS YOU SEE SUITED. NOTE, YOU SHOULD CREATE A SEPERATE CLASS FOR THESE
//STATIC METHODS!


