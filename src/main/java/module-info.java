module hi.verkefni.vidmot {
    requires javafx.controls;
    requires javafx.fxml;
    requires Spil21Library;

    opens hi.verkefni.vidmot to javafx.fxml;
    exports hi.verkefni.vidmot;
    exports hi.verkefni.spilVinnsla;
}

