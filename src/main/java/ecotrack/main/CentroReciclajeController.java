package ecotrack.main;

import ecotrack.logica.Residuo;
import ecotrack.services.SistemaEcoTrack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CentroReciclajeController implements SistemaInyectable {

    private SistemaEcoTrack sistema;

    @FXML private Label lblCantidad;
    @FXML private Label lblNombre;
    @FXML private Label lblTipo;
    @FXML private Label lblPeso;
    @FXML private Label lblZona;

    @Override
    public void setSistema(SistemaEcoTrack sistema) {
        this.sistema = sistema;
        actualizarVista();
    }

    private void actualizarVista() {
        int cantidad = sistema.getCentroReciclaje().cantidadPendiente();
        lblCantidad.setText("Residuos pendientes: " + cantidad);

        Residuo r = sistema.getCentroReciclaje().verSiguiente();

        if (r != null) {
            lblNombre.setText("Nombre: " + r.getNombre());
            lblTipo.setText("Tipo: " + r.getTipo());
            lblPeso.setText("Peso: " + r.getPeso());
            lblZona.setText("Zona: " + r.getZona());
        } else {
            lblNombre.setText("Nombre: -");
            lblTipo.setText("Tipo: -");
            lblPeso.setText("Peso: -");
            lblZona.setText("Zona: -");
        }
    }

    @FXML
    private void procesarResiduo() {
        Residuo r = sistema.getCentroReciclaje().procesarResiduo();
        sistema.guardarDatos();
        actualizarVista();
    }
}
