package hi.verkefni.vidmot;

import hi.verkefni.spilVinnsla.Leikmadur;
import hi.verkefni.vinnsla.LeikmadurInterface;
import hi.verkefni.vinnsla.SpilV;
import hi.verkefni.vinnsla.Stokkur;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *
 *  Lýsing  : Aðalstýriklasi fyrir spilið 21. Hefur handlera fyrir nýr leikur, nýtt spil og komið nóg.
 *  Hefur tilviksbreytur fyrir helstu viðmótshluti. Inniheldur stöðu klasa.
 *
 *
 *****************************************************************************/
public class SpilController  {

    // fastar
    public static final String DEALER = "Dealer"; // nafnið á dealer
    private static final int MAX = 5; // Mesti fjöldi spila
    public static final int HAMARK = 17; // Hæsta samtala dealers ef komið er nóg

    // viðmótshlutir - hér til að hægt sé að gera þá virka/óvirka
    @FXML
    private Button fxNyrLeikur;
    @FXML
    private Button fxKomidNog;
    @FXML
    private Button fxNyttSpil;

    @FXML
    private Text fxDealerNafn;  // inniheldur nafn og samtölu - en hefði mátt skipta í tvær breytur
    @FXML
    private Text fxLeikmadurNafn;
    @FXML
    private HBox fxDealer;      // spilin á hendi í notendaviðmótinu
    @FXML
    private HBox fxLeikmadur;

    private final Stada stada = new Stada();   // stöður notendaviðmóts

    // vinnsla
    private Leikmadur leikmadur;    // leikmaður
    private Leikmadur dealer;       // dealer
    private Stokkur stokkur = new Stokkur();    // spilastokkur


    /**
     * Frumstilla vinnslu. Hefjaj nýjan leik. Biðja um nafn leikmanns
     *
     */

    public void initialize() {
        dealer = new Leikmadur(DEALER, MAX);
        String nafn = hvadHeitirLeikmadur();
        leikmadur = new Leikmadur(nafn, MAX);
        nyrLeikurHandler(null);
    }

    /**
     * Opna dialog og spyrja um nafn leikmanns
     *
     * @return nafni leikmanns
     */
    private String hvadHeitirLeikmadur() {
        LeikmadurDialogPane l = new LeikmadurDialogPane();
        String nafn = l.hvadHeitirLeikmadur();
        if (nafn == null) { // ef ekkert nafn þá hætta í forriti
            Platform.exit();
        }
        return nafn;
    }

    /**
     * Handler fyrir að setja út nýtt spil - athuga hvor vann
     *
     * @param actionEvent ónotað
     */
    @FXML
    public void nyttSpilHandler(ActionEvent actionEvent) {
        nyttSpil(leikmadur, fxLeikmadur);
        uppfaeraSamtals(fxLeikmadurNafn, leikmadur);
        hvorVann();
    }

    /**
     * Hjálparfall fyrir að draga og gefa nýtt spil
     *
     * @param l leikmaður
     * @param h notendaviðmótið
     */
    private void nyttSpil(Leikmadur l, HBox h) {
        SpilV sV = stokkur.dragaSpil(); // draga spil
        l.gefaSpil(sV);                 // gefa leikmanni/dealer spil
        Spil s = new Spil();            // nýtt spil í viðmóti
        s.setSpil(sV);                  // tengja viðmótið við vinnsluna
        h.getChildren().add(s);         // birta spilið
    }


    /**
     * Nýr leikur hefst. Spil sett út fyrir leikmann og dealer og athugað hvor vann
     * Notendaviðmót uppfært miðað við stöðu
     *
     * @param actionEvent ónotað
     */
    @FXML
    private void nyrLeikurHandler(ActionEvent actionEvent) {
        nyrLeikur();
        hvorVann();
    }

    /**
     * Hjálparaðferð fyrir nýjan leik
     */
    private void nyrLeikur() {
        // nýir vinnsluhlutir eða upphafsstilltir
        stokkur = new Stokkur();
        leikmadur.nyrLeikur();
        dealer.nyrLeikur();
        // eyða gömlu spilunum hjá dealer og leikmanni
        fxDealer.getChildren().removeAll(fxDealer.getChildren());
        fxLeikmadur.getChildren().removeAll(fxLeikmadur.getChildren());
        // nýtt spil fyrir dealer
        upphafsstillaLeikmann(dealer, fxDealer, fxDealerNafn);
        upphafsstillaLeikmann(leikmadur, fxLeikmadur, fxLeikmadurNafn);
        // breyta stöðunni á hnöppunum
        stada.nyrLeikurStada();
    }

    /**
     * Athuga hvor vann og uppfæra notendaviðmót. Staða leiks breytist
     */
    private void hvorVann() {
        LeikmadurInterface l = leikmadur.hvorVann(dealer);
        if (l != null) {
            vann(l == leikmadur ? fxLeikmadurNafn : fxDealerNafn, l);
            stada.leikLokidStada();     // staðan er leik lokið
        }
    }

    /**
     * Upphafsstilla leikmann með tveimur spilum og stöðu leiksins
     *
     * @param l    leikmaðurinn
     * @param h    hbox sem notað er fyrir hendi
     * @param nafn nafn leikmanns
     */
    private void upphafsstillaLeikmann(Leikmadur l, HBox h, Text nafn) {
        nyttSpil(l, h);
        nyttSpil(l, h);
        uppfaeraSamtals(nafn, l);
    }

    /**
     * Leikmaður hefur hætt leik og dealer fær nýtt spil þar til samtalan er 17 eða stærri
     * Ef dealer hefur hærri samtölu en ekki hærri en 21 þá vinnur dealer annars leikmaður
     *
     * @param actionEvent ónotað
     */
    @FXML
    private void komidNogHandler(ActionEvent actionEvent) {
        while (dealer.getSamtals() < HAMARK) {  // draga nýtt spil á meðan samtalan er minni en hámark fyrir dealer
            nyttSpil(dealer, fxDealer);
        }
        uppfaeraSamtals(fxDealerNafn, dealer);
        if (leikmadur.vinnurDealer(dealer)) {
            vann(fxDealerNafn, dealer);
        } else {
            vann(fxLeikmadurNafn, leikmadur);
        }
        stada.leikLokidStada(); // leik er lokið
    }

    /**
     * Uppfærir samtölu í notendaviðmóti hjá leikmanni eða dealer
     *
     * @param nafn textasvið fyrir leikmann/dealer
     * @param l    vinnsluhluti fyrir leimann/dealer
     */
    private void uppfaeraSamtals(Text nafn, LeikmadurInterface l) {
        nafn.setText(l.getNafn() + " " + l.getSamtals());
    }

    /**
     * Birtir í {@code t} að {@code l} hafi unnið og birtir samtölu
     *
     * @param t textasvið fyrir leikmann/dealer
     * @param l vinnsluhluti fyrir leikmann/dealer
     */
    private void vann(Text t, LeikmadurInterface l) {
        t.setText(l.getNafn() + " " + l.getSamtals() + " vann");
    }


    /**
     * Innri klasi sem heldur utan um mismunandi stöður í notendaviðmótinu. Sjá myndband um stöðurit verkefnisins
     * Innri klasi sér breytur og aðferðir í ytri klasanum
     */
    class Stada {

        /**
         * Staða 1 á mynd
         * Staðan breytist ef leik er lokið.
         * Ekki hægt að velja nýtt spil eða komið nóg. Hægt að ræsa nýjan leik
         */
        private void leikLokidStada() {
            fxNyrLeikur.setDisable(false);
            fxNyttSpil.setDisable(true);
            fxKomidNog.setDisable(true);
        }


        /**
         * Staða 2 á mynd
         * Staðan breytist ef leikur er hafin
         * Hægt að velja nýtt spil eða komið nóg. Ekki hægt að ræsa nýjan leik
         */
        private void nyrLeikurStada() {
            fxNyrLeikur.setDisable(true);
            fxNyttSpil.setDisable(false);
            fxKomidNog.setDisable(false);
        }

    }
}
