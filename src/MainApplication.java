import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
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
        primaryStage.setTitle("Aziz's Weather Application");
        primaryStage.getIcons().add(new Image("images/cloudy.png"));
        primaryStage.show();
    }
}
