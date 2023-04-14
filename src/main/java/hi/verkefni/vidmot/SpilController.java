package hi.verkefni.vidmot;

import hi.verkefni.spilVinnsla.Leikmadur;
import hi.verkefni.vinnsla.LeikmadurInterface;
import hi.verkefni.vinnsla.SpilV;
import hi.verkefni.vinnsla.Stokkur;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
public class SpilController {

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
    private Button fxBetButton;
    @FXML
    private Button fxDouble;

    @FXML
    private Text fxDealerNafn;  // inniheldur nafn og samtölu - en hefði mátt skipta í tvær breytur
    @FXML
    private Text fxLeikmadurNafn;
    @FXML
    private Label fxBalance;
    @FXML
    private HBox fxDealer;      // spilin á hendi í notendaviðmótinu
    @FXML
    private HBox fxLeikmadur;
    @FXML
    private TextField fxBet;

    private final Stada stada = new Stada();   // stöður notendaviðmóts

    // vinnsla
    private Leikmadur leikmadur;    // leikmaður
    private Leikmadur dealer;       // dealer
    private Stokkur stokkur = new Stokkur();    // spilastokkur

    private int balance;
    private int bet;


    /**
     * Frumstilla vinnslu. Hefjaj nýjan leik. Biðja um nafn leikmanns
     */

    public void initialize() {
        dealer = new Leikmadur(DEALER, MAX);
        String nafn = hvadHeitirLeikmadur();
        leikmadur = new Leikmadur(nafn, MAX);
        int balance = hvadBalance();
        reset();
        fxNyttSpil.setDisable(true);
        fxKomidNog.setDisable(true);
        fxDouble.setDisable(true);
        fxBalance.setText("Balance: " + balance);
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
     * Opna dialog og spyrja upphæð sem hann vill byrja með, ef meira en 1000 er það sett auto í 1000
     *
     * @return upphæð
     */
    private int hvadBalance() {
        BalanceDialogPane b = new BalanceDialogPane();

        int balance = Integer.parseInt(b.hvadBalance());
        if (balance > 1000) {
            balance = 1000;
        }
        if (balance < 1) { // ef minna en 1 hætta
            Platform.exit();
        }

        //setur notandi sem valdi balance í breytuna
        this.balance = balance;
        return balance;
    }


    private void setBet(int bet) {
        this.bet = bet;
    }

    private boolean getBet() {
        if (fxBet.getText().isEmpty()) {
            showAlert(10);
        }
        this.bet = Integer.parseInt(fxBet.getText());

        if (bet > balance) {
            showAlert(1);
        } else {
            System.out.println("bet tókst, " + bet + " balance " + balance);
            return true;
        }
        return false;
    }

    /**
     * Alert, fer eftir parameter
     */
    private void showAlert(int hvad) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");

        // Header Text: null
        alert.setHeaderText(null);
        if (hvad == 1) {
            alert.setContentText("Not enough balance for this bet");
        }
        if (hvad == 10) {
            alert.setContentText("No bets placed");
        }
        alert.showAndWait();
    }

    public void doubleHandler(ActionEvent actionEvent) {

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
        if (getBet()) {
            nyrLeikur();
            hvorVann();
        }
    }

    /**
     * Hjálparaðferð fyrir nýjan leik
     */
    private void nyrLeikur() {
        if (balance < 1) {
            System.out.println(balance);
            Platform.exit();
        }
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

    //hverfur alla spilana á borðinu
    private void reset() {
        // eyða gömlu spilunum hjá dealer og leikmanni
        fxDealer.getChildren().removeAll(fxDealer.getChildren());
        fxLeikmadur.getChildren().removeAll(fxLeikmadur.getChildren());

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
        if (l == leikmadur) { // Gefa eitt spil til dealer og 2 til leikmann
            nyttSpil(l, h);
        }
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
            balance -= bet;
            // Update balance ef leikmaður vinnur
            fxBalance.setText("Balance: " + balance);
        } else {
            vann(fxLeikmadurNafn, leikmadur);
            balance += bet;
            // Update balance ef leikmaður vinnur
            fxBalance.setText("Balance: " + balance);
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
        t.setText(l.getNafn() + " " + l.getSamtals() + " won");
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
            fxDouble.setDisable(true);

            fxBetButton.setDisable(false);
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
            fxDouble.setDisable(false);

            fxBetButton.setDisable(true);
        }

    }
}
