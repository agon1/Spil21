package hi.verkefni.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *
 *  Lýsing  : Aðalklasi fyrir forritið Spil21. Les inn aðalnotendaviðmótið
 *
 *
 *****************************************************************************/
public class SpilApplication extends Application {

    // fastar
    private static final String ADALVIDMOT = "tuttuguOgEinn.fxml";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SpilApplication.class.getResource(ADALVIDMOT));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Tuttugu og einn");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
