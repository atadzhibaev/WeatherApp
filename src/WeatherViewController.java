import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

public class WeatherViewController {
    private WeatherData weather;
    @FXML
    private VBox WeeklyForecast;
    @FXML
    private HBox HourlyForecast;

//    @FXML
//    private void initialize() {
//        Task<Void> task = new Task<>() {
//            @Override
//            protected Void call() throws Exception {
//                WeatherData weather = WeatherService.getWeatherData("68701");
//                Platform.runLater(() -> updateUI(weather));
//                return null;
//            }
//        };
//        new Thread(task).start();
//    }


    @FXML
    private void initialize() throws Exception {
        load_data();
    }

//    private void updateUI(WeatherData weather) {
//        for (Map.Entry<String, DailyWeatherData> entry : weather.getDailyWeatherMap().entrySet()) {
//            VBox daily_data = new VBox(5);
//            daily_data.setAlignment(Pos.CENTER);
//            daily_data.setPadding(new Insets(10));
//            daily_data.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 10; -fx-background-radius: 10;");
//
//            LocalDate date = LocalDate.parse(entry.getKey());
//            int code = entry.getValue().getWeather_code();
//            double temp_min = entry.getValue().getTemp_min();
//            double temp_max = entry.getValue().getTemp_max();
//
//            Label dayOfWeek = new Label(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
//            Label maxTemp = new Label("Max: " + temp_max + "°C");
//            Label minTemp = new Label("Min: " + temp_min + "°C");
//
//            daily_data.getChildren().addAll(dayOfWeek, maxTemp, minTemp);
//            WeeklyForecast.getChildren().add(daily_data);
//        }
//    }

    public void load_data() throws Exception {
        weather = WeatherService.getWeatherData("68701");

        //Weather Forecast for a week
        for (Map.Entry<String, DailyWeatherData> entry : weather.getDailyWeatherMap().entrySet()) {

            LocalDate date = LocalDate.parse(entry.getKey());
            int code = entry.getValue().getWeather_code();
            double temp_min = entry.getValue().getTemp_min();
            double temp_max = entry.getValue().getTemp_max();

            //day of week conversion of the date
            Label dayOfWeek = new Label(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
            dayOfWeek.setFont(Font.font(14));
            VBox dayBox = new VBox(dayOfWeek);
            dayBox.setAlignment(Pos.CENTER_RIGHT);

            //icons based on weather type
            ImageView icon = new ImageView(new Image("/images/38919.png"));
            icon.setFitWidth(50);
            icon.setFitHeight(50);


            // TEMPERATURES
            Label maxTemp = new Label(String.valueOf(temp_max));
            maxTemp.setFont(Font.font(16));

            Label minTemp = new Label(String.valueOf(temp_min));
            minTemp.setFont(Font.font(12));

            VBox tempBox = new VBox(5, maxTemp, minTemp);
            tempBox.setAlignment(Pos.CENTER_LEFT);

            //creating new row in the weeekly forecast
            HBox daily_data = new HBox(20, icon, tempBox, dayBox);
            daily_data.setAlignment(Pos.CENTER);
            daily_data.setPadding(new Insets(10));
//            daily_data.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 10; -fx-background-radius: 10;");
            daily_data.setStyle("""
    -fx-background-color: rgba(100, 100, 100, 0.30);  /* Very light gray */
    -fx-background-radius: 10;
    -fx-border-radius: 10;
    -fx-border-color: #cccccc;
    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 3, 0.5, 0, 1);
""");


//            daily_data.getChildren().addAll(dayOfWeek, maxTemp, minTemp);

            WeeklyForecast.getChildren().add(daily_data);
        }

       //weather forecast for seven hours
        HourlyForecast.setPadding(new Insets(10));
        HourlyForecast.setAlignment(Pos.CENTER_LEFT);

        ScrollPane scrollPane = new ScrollPane(HourlyForecast);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        int counter = 1;
        for (Map.Entry<String, HourlyWeatherData> entry : weather.getHourlyWeatherMap().entrySet()) {
            if (counter < 16) {
                Label hour = new Label(entry.getKey().substring(11));
                hour.setFont(Font.font(12));

                ImageView icon = new ImageView(new Image("/images/38919.png"));
                icon.setFitWidth(50);
                icon.setFitHeight(50);

                Label temp = new Label (String.valueOf(entry.getValue().getTemperature()));

                VBox hourly_temp = new VBox(5, hour, icon, temp);
                hourly_temp.setAlignment(Pos.CENTER);
                hourly_temp.setPadding(new Insets(8));
                hourly_temp.setStyle("""
        -fx-background-color: rgba(100, 100, 100, 0.30);
        -fx-background-radius: 10;
        -fx-border-radius: 10;
    """);

//            HBox HourlyForecast = new HBox(10)
                HourlyForecast.getChildren().add(hourly_temp);
                counter++;
            }
        }

    }

    public static String getIconPathForCode(int code) {
        if (code == 0) return "icons/sunny.png";
        else if (code == 1 || code == 2 || code == 3) return "icons/partly_cloudy.png";
        else if (code >= 45 && code <= 48) return "icons/fog.png";
        else if (code >= 51 && code <= 67) return "icons/rain.png";
        else if (code >= 80 && code <= 99) return "icons/storm.png";
        return "icons/default.png";
    }

}
