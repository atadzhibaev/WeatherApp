import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {


    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/WeatherView.fxml")));

        Scene scene = new Scene(loader);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
