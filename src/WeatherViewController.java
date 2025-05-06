import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.json.JSONObject;
import javafx.scene.media.AudioClip;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class WeatherViewController {
    public Label wind_speed_lbl;
    public Label humidity_lbl;
    public Label temp_lbl;
    public ToggleButton Conv_toggle_btn;
    public Label location_lbl;
    public ImageView main_weather_icon;
    @FXML
    private ImageView wind_direction_img;
    @FXML
    private Label feels_like_lbl;
    @FXML
    private ScrollPane HourlyScroll;
    private WeatherData weather;
    @FXML
    private VBox WeeklyForecast;
    @FXML
    private HBox HourlyForecast;

    double windspeed;
    double feels_like;
    double current_temp;

    File soundfile = new File("src/sounds/error_sound.wav");

    List<Label> allTemps = new ArrayList<>();

    @FXML
    private void initialize() throws Exception {
        JSONObject iplocation = new JSONObject(WeatherService.fetchData("http://ip-api.com/json/"));
        String zipCode = iplocation.getString("zip");
        load_data(zipCode);
    }


    public void load_data(String zipCode) throws Exception {
        //clears all the contents weekly and hourly forecast
        WeeklyForecast.getChildren().clear();
        HourlyForecast.getChildren().clear();

        //pulls information from the api
        weather = WeatherService.getWeatherData(zipCode);

        //main weather icon
        String currenttime = String.valueOf(LocalDateTime.now());
        main_weather_icon.setImage(new Image(getIconPathForCode(weather.getWeather_code(),
                Integer.parseInt(currenttime.substring(11, 13)))));

        //Weather Forecast for a week
        int counter = 0;
        for (Map.Entry<String, DailyWeatherData> entry : weather.getDailyWeatherMap().entrySet()) {
            if (counter < 7) {
                LocalDate date = LocalDate.parse(entry.getKey());
                int code = entry.getValue().getWeather_code();
                double temp_min = entry.getValue().getTemp_min();
                double temp_max = entry.getValue().getTemp_max();

                //day of week conversion of the date
                Label dayOfWeek = new Label(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
                dayOfWeek.setTextFill(Paint.valueOf("#ffffff"));
                dayOfWeek.setFont(Font.font(14));
                VBox dayBox = new VBox(dayOfWeek);
                dayBox.setAlignment(Pos.CENTER_RIGHT);

                //icons based on weather type
                ImageView icon = new ImageView(new Image(getIconPathForCode(entry.getValue().getWeather_code(), 12)));
                icon.setFitWidth(50);
                icon.setFitHeight(50);


                // TEMPERATURES
                Label maxTemp = new Label(String.valueOf(temp_max));
                maxTemp.setFont(Font.font(16));
                maxTemp.setTextFill(Paint.valueOf("#ffffff"));
                Label minTemp = new Label(String.valueOf(temp_min));
                minTemp.setFont(Font.font(12));
                minTemp.setTextFill(Paint.valueOf("#ffffff"));
                //adding labels to the label list for conversion
                allTemps.add(maxTemp);
                allTemps.add(minTemp);

                VBox tempBox = new VBox(5, maxTemp, minTemp);
                tempBox.setAlignment(Pos.CENTER_LEFT);

                //creating new row in the weeekly forecast
                HBox daily_data = new HBox(20, icon, tempBox, dayBox);
                daily_data.setAlignment(Pos.CENTER);
                daily_data.setPadding(new Insets(10));
                daily_data.setStyle("""
                            -fx-background-color: rgba(100, 100, 100, 0.40);  /* Very light gray */
                            -fx-background-radius: 10;
                            -fx-border-radius: 10;
                            -fx-border-color: #cccccc;
                            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0.5, 0, 1);
                        """);

                WeeklyForecast.getChildren().add(daily_data);
                counter++;
            }
            else
                break;
        }

       //weather forecast for 16 hours
        HourlyForecast.setPadding(new Insets(10));
        HourlyForecast.setAlignment(Pos.CENTER_LEFT);

        counter = 0;
        for (Map.Entry<String, HourlyWeatherData> entry : weather.getHourlyWeatherMap().entrySet()) {
            if (LocalDateTime.parse(entry.getKey()).isAfter(LocalDateTime.now())) {
                if (counter < 12) {
                    String hour =  entry.getKey().substring(11);
                    Label hour_lbl = new Label(hour);
                    hour_lbl.setFont(Font.font(12));
                    hour_lbl.setTextFill(Paint.valueOf("#ffffff"));
                    int time = Integer.parseInt(hour.substring(0,2));
                    ImageView icon = new ImageView(new Image(getIconPathForCode(entry.getValue().getWeatherCode(), time)));
                    icon.setFitWidth(50);
                    icon.setFitHeight(50);

                    Label temp = new Label (String.valueOf(entry.getValue().getTemperature()));
                    temp.setTextFill(Paint.valueOf("#ffffff"));
                    allTemps.add(temp);

                    VBox hourly_temp = new VBox(5, hour_lbl, icon, temp);
                    hourly_temp.setAlignment(Pos.CENTER);
                    hourly_temp.setPadding(new Insets(8));
                    hourly_temp.setStyle("""
                                            -fx-background-color: rgba(100, 100, 100, 0.30);
                                            -fx-background-radius: 10;
                                            -fx-border-radius: 10;
                                        """);
                HourlyForecast.getChildren().add(hourly_temp);
                counter++;
                }
            }
        }


        //code to make scroll bar invisible
        HourlyScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        HourlyScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //scrolling with mouse wheel
        HourlyScroll.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() != 0) {
                double delta = event.getDeltaY();
                double currentHvalue = HourlyScroll.getHvalue();
                double newHvalue = currentHvalue - delta / 500; // scroll sensitivity
                newHvalue = Math.max(0, Math.min(1, newHvalue)); // clamp between 0 and 1
                HourlyScroll.setHvalue(newHvalue);
                event.consume(); // prevent vertical scrolling
            }
        });


        //code to turn arrow
        wind_direction_img.setRotate(weather.getWind_direction() + 70);

        //setting default values of weather data
        windspeed = weather.getWindspeed();
        feels_like = weather.getFeels_like_temp();
        current_temp = weather.getTemperature();

        //display feels like value
        feels_like_lbl.setText(String.valueOf(feels_like) + "°C");

        //display temperature
        temp_lbl.setText(String.valueOf(current_temp) + "°C");

        //display windspeed
        wind_speed_lbl.setText(String.valueOf(windspeed) + "Km\\h");

        //display humidity
        humidity_lbl.setText(String.valueOf(weather.getHumidity() + "%"));

        //display location
        location_lbl.setText(weather.getCity() + ", " + weather.getState());

    }

    public void ConvertMetrics() {
        boolean toMiles = Conv_toggle_btn.isSelected(); //checks if the button is in the clicked position or not
        //this loop converts temp values of weekly and hourly forecast
        for (Label label : allTemps) {
            double tempOnLabel = Double.parseDouble(label.getText());  // Get °C since the default data pulled from
            // the api is in Celsius
            if (toMiles) {
                label.setText(String.format("%.1f", tempOnLabel * 9.0 / 5 + 32));
            } else {
                label.setText(String.format("%.1f", (tempOnLabel - 32) * 5.0 / 9));
            }
        }
        // this condition converts values of individual huge labels of the interface
        if (toMiles) {
            wind_speed_lbl.setText(String.format("%.1f M\\h", windspeed / 1.609));
            feels_like_lbl.setText(String.format("%.1f°F", feels_like * 9.0 / 5 + 32));
            temp_lbl.setText(String.format("%.1f°F", current_temp * 9.0 / 5 + 32));

        } else {
            wind_speed_lbl.setText(String.format("%.1f Km/h", windspeed));
            feels_like_lbl.setText(String.format("%.1f°C", feels_like));
            temp_lbl.setText(String.format("%.1f°C", current_temp));
        }
    }

    public void ChangeZipCode() throws Exception {
        //preping a file for a funny sound
        AudioInputStream sound = AudioSystem.getAudioInputStream(soundfile);
        Clip track = AudioSystem.getClip();
        track.open(sound);

        //setting up a dialog box
        TextInputDialog zipcodechange = new TextInputDialog();
        zipcodechange.setTitle("Change ZIP Code");
        zipcodechange.setGraphic(null);
        zipcodechange.setHeaderText(null);
        zipcodechange.setContentText("ZIP:");


        while (true) {
            zipcodechange.getEditor().clear(); //clears text field after a wrong input
            Optional<String> result = zipcodechange.showAndWait(); //shows dialog box for the zip code input
            if (result.isPresent()) {
                String zip = result.get();
                if (zip.matches("^[0-9]{5}(?:-[0-9]{4})?$")) {
                    load_data(zip);
                    break;
                } else {
                    track.setFramePosition(0); //this enables audio file to play each time user input is wrong
                    track.start();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid ZIP Code. Please enter a 5-digit number.");
                    alert.setGraphic(null);
                    alert.setHeaderText((null));
                    alert.showAndWait();
                }
            } else
                break;
        }
    }




    public static String getIconPathForCode(int code, int time) {
        if (time >= 20 || time <= 6) {
            if (code == 0) return "images/moon.png";
            else if (code == 1 || code == 2 || code == 3) return "images/moon_clouds.png";
            else if (code >= 45 && code <= 48) return "images/foggy.png";
            else if (code >= 51 && code <= 67) return "images/rain2.png";
            else if (code >= 71 && code <= 77) return "images/snow.png";
            else if (code >= 80 && code <= 99) return "images/storm.png";
        }
        else {
            if (code == 0) return "images/sunny.png";
            else if (code == 1 || code == 2 || code == 3) return "images/cloudy.png";
            else if (code >= 45 && code <= 48) return "images/foggy.png";
            else if (code >= 51 && code <= 67) return "images/rain2.png";
            else if (code >= 71 && code <= 77) return "images/snow.png";
            else if (code >= 80 && code <= 99) return "images/storm.png";
        }
        return "images/sunny.png";
    }

}
