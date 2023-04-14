package hi.verkefni.vidmot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;

/******************************************************************************
 *  Nafn    :
 *  T-póstur:
 *
 *  Lýsing  : Dialog pane sem biður um upphæð til að byrja með
 *
 *
 *****************************************************************************/
public class BalanceDialogPane extends DialogPane {

    // fastar
    private static final String LEIKMADUR_DIALOG = "balanceDialog-view.fxml";

    @FXML
    private TextField fxBalance;


    public BalanceDialogPane() {
        lesaPane();
    }

    /**
     * Opnar dialog sem leyfir notanda að inn hversu mikill pening hann vill byrja með
     *
     * @return skilar upphæð en null ef notandi hætti við
     */
    public String hvadBalance() {
        Dialog<ButtonType> d = new Dialog<>();
        d.setDialogPane(this);
        //  setConverter(d);    // ef þú vilt nota resultConverter í staðinn fyrir if-setninguna

        Optional<ButtonType> utkoma = d.showAndWait();
        if (utkoma.isPresent() && (utkoma.get()
                .getButtonData() == ButtonBar.ButtonData.OK_DONE)) {
            return getBalance();
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
                return getBalance();
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
    public String getBalance() {
        return fxBalance.getText();
    }
}
