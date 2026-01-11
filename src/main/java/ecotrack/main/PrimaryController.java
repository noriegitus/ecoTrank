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
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
