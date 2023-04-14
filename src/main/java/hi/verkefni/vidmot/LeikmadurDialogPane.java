package hi.verkefni.vidmot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *
 *  Lýsing  : Dialog pane sem biður um nafn leikmanns
 *
 *
 *****************************************************************************/
public class LeikmadurDialogPane extends DialogPane {

    // fastar
    private static final String LEIKMADUR_DIALOG = "leikmadurDialog-view.fxml";

    // viðmótshlutir
    @FXML
    private TextField fxLeikmadur;


    public LeikmadurDialogPane() {
        lesaPane();
    }

    /**
     * Opnar dialog sem leyfir notanda að setja inn nafn leikmanns
     *
     * @return skilar nafni leikmanns en null ef notandi hætti við
     */
    public String hvadHeitirLeikmadur() {
        Dialog<ButtonType> d = new Dialog<>();
        d.setDialogPane(this);
        //  setConverter(d);    // ef þú vilt nota resultConverter í staðinn fyrir if-setninguna

        Optional<ButtonType> utkoma = d.showAndWait();
        if (utkoma.isPresent() && (utkoma.get()
                .getButtonData() == ButtonBar.ButtonData.OK_DONE)) {
            return getNafn();
        }
        return null;
    }

    /**
     * Sýnir result converter fyrir Dialog<String>
     *
     * @param d dialogur
     */
    private void setConverter(Dialog<String> d) {
        d.setResultConverter(b -> {       // b er af taginu ButtonType
            if (b.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return getNafn();
            } else
                return "";
        });      // endir á d.setResultConverter
    }

    /**
     * Les inn notendaviðmót fyrir dialog-inn
     */
    private void lesaPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(LEIKMADUR_DIALOG));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // getterar
    public String getNafn() {
        return fxLeikmadur.getText();
    }
}
