module com.mycompany.ecotrack {
    requires javafx.controls;
    requires javafx.fxml;

    opens ecotrack.main to javafx.fxml;
    exports ecotrack.main;
}
