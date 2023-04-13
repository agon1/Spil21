package hi.verkefni.spilVinnsla;

import hi.verkefni.vinnsla.LeikmadurInterface;
import hi.verkefni.vinnsla.SpilV;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/******************************************************************************
 *  Nafn    : Ebba Þóra Hvannberg
 *  T-póstur: ebba@hi.is
 *
 *  Lýsing  : Vinnsluklasi sem hefur gögn um hendi leikmanns í spili
 *            Gögnin eru nafn leikmanns, samtals á hendi, fjöldi spila á hendi og hámark
 *            fjölda spila á hendi
 *
 *****************************************************************************/
public class Leikmadur implements LeikmadurInterface {
    private String nafn;    // nafn leikmanns
    private int samtals;    // samtals á hendi
    private int fjoldi;     // fjöldi spila
    private final int max;  // hámark fjöldi spila
    

    public Leikmadur(String n, int max) {
        this.nafn = n;
        this.max = max;
    }

    public int getSamtals() {
        return samtals;
    }


    public String getNafn() {
        return nafn;
    }

    public void setNafn(String nafn) {
        this.nafn = nafn;
    }

    /**
     * Leikmaður má hafa mest max fjölda spila á hendi.
     * Gefur leikmanni spil s ef leikmaður ef max er ekki náð.
     * Uppfærir samtölu spila
     *
     * @param s spilið sem á að gefa leikmanni
     */
    public void gefaSpil(SpilV s) {
        if (fjoldi < max) {
            samtals += s.getVirdi();
            fjoldi++;
        }
    }

    /**
     * Athugar hvort d hefur unnið leikmanninn
     *
     * @param d dealer
     * @return satt ef d hefur unnið, annars false
     */
    public boolean vinnurDealer(LeikmadurInterface d) {
        return d.getSamtals() > this.getSamtals() && d.getSamtals() <= 21;
    }

    /**
     * Skila hvaða leikmaður vann d (dealerinn) eða þessi leikmaður
     *
     * @param d - andstæðingur leikmannsins
     * @return - skilar þeim leikmanni sem vann  - null ef hvorugur vann
     */
    public LeikmadurInterface hvorVann(LeikmadurInterface d) {
        if (d.getSamtals() > 21)   // dealer springur - leikmaður vinnur óháð samtölu
            return this;
        else if (getSamtals() > 21 && d.getSamtals() <= 21) // leikmaður springur
            return d;
        else if (getSamtals() == 21)    // leikmaður fær 21
            return this;
        else if (d.getSamtals() == 21) // dealer fær 21
            return d;
        return null;
    }

    @Override
    public String toString() {
        return "[" + nafn + ", " + samtals + "]";
    }

    /**
     * Leikmaður tekur þátt í nýjum leik, samtala og fjöldi spila á hendi er 0
     */
    public void nyrLeikur() {
        samtals = 0;
        fjoldi = 0;
    }

    /**
     * Prófunarfall fyrir Leikmann - aðallega hvor vinnur
     *
     * @param args ónotað
     */
    public static void main(String[] args) {
        Leikmadur leikmadur = new Leikmadur("leikmaður", 5);
        Leikmadur dealer = new Leikmadur("dealer", 5);

        Scanner s = new Scanner(System.in, StandardCharsets.UTF_8);
        while (s.hasNext()) {
            int l = s.nextInt();
            int d = s.nextInt();
            leikmadur.samtals = l;
            dealer.samtals = d;
            System.out.println("leikmaður " + l + " dealer " + d);
            System.out.println(leikmadur.hvorVann(dealer));
            System.out.println("vinnur dealer ");
            System.out.println(leikmadur.vinnurDealer(dealer));
        }
    }
}
