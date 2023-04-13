package hi.verkefni.vidmot;

import hi.verkefni.vinnsla.SpilV;
import hi.verkefni.vinnsla.Tegund;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *
 *  Lýsing  : Klasi sem inniheldur framsetningu mannspils í notendaviðmóti
 *
 *
 *****************************************************************************/
public class Spil extends AnchorPane {

    // Fastar
    private static final Image[] myndir = new Image[4];
    private static final Tegund[] tegundir = Tegund.values();

    // Hér eru engar tilviksbreytur því getChildren() er notað en gætu vel verið
    // fjórar tilviksbreytur fyrir 2xmyndir og 2xtexta

    public Spil() {
        lesaSpil();
        // lesa inn myndir af tegundum spila
        for (int i = 0; i < myndir.length; i++)
            myndir[i] = new Image(Objects.requireNonNull(Spil.class.getResourceAsStream("myndir/".concat(tegundir[i].name().toLowerCase()).concat(".png"))),
                    32, 32, true, true);

    }

    /**
     * Les inn view fyrir spilið úr .fxml skrá
     */
    private void lesaSpil() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("spil-view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Setur réttar myndir og texta á spil-ið út frá vinnsluhlut s
     *
     * @param s vinnsluhlutur fyrir mannspil
     */
    public void setSpil(SpilV s) {
        // hér hefði líka mátt nota tilviksbreytur í staðinn fyrir að ná í börnin
        ((ImageView) getChildren().get(1)).setImage(myndir[s.getTegund().ordinal()]);
        ((ImageView) getChildren().get(2)).setImage(myndir[s.getTegund().ordinal()]);
        ((Text) getChildren().get(3)).setText(s.getGildi().toString());
        ((Text) getChildren().get(4)).setText(s.getGildi().toString());
    }
}
