package ecotrack.main;

import ecotrack.services.SistemaEcoTrack;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PrimaryController {

    private SistemaEcoTrack sistema;

    public void setSistema(SistemaEcoTrack sistema) {
        this.sistema = sistema;
    }

    @FXML
    private void irVerResiduos() {
        abrirVentana("verResiduos.fxml", "Ver Residuos");
    }

    @FXML
    private void irCentroReciclaje() {
        abrirVentana("centroReciclaje.fxml", "Centro de Reciclaje");
    }
    
    @FXML
    private void irZonas() {
        abrirVentana("zonas.fxml", "Gestión de Zonas");
    }

    @FXML
    private void irEstadisticas() {
        abrirVentana("estadistica.fxml", "Estadísticas");
    }

    // MÉTODO AUXILIAR (reutilizable)
    private void abrirVentana(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mycompany/ecotrack/" + fxml)
            );
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof SistemaInyectable) {
                ((SistemaInyectable) controller).setSistema(sistema);
            }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            
            // Tamaños apropiados según la ventana
            int ancho = 700;
            int alto = 550;
            if (fxml.equals("verResiduos.fxml")) {
                ancho = 800;
                alto = 600;
            } else if (fxml.equals("zonas.fxml")) {
                ancho = 700;
                alto = 500;
            } else if (fxml.equals("estadistica.fxml")) {
                ancho = 750;
                alto = 550;
            } else if (fxml.equals("centroReciclaje.fxml")) {
                ancho = 500;
                alto = 400;
            }
            
            stage.setScene(new Scene(root, ancho, alto));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
