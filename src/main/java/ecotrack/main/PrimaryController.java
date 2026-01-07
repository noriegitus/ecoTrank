package ecotrack.main;

import ecotrack.services.SistemaEcoTrack; 
import ecotrack.logica.Residuo;           
import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    private SistemaEcoTrack sistema;

    @FXML
    public void initialize() {
        System.out.println("Inicializando vista primaria...");
        
        // Cargamos los datos guardados anteriormente
        sistema = SistemaEcoTrack.cargarDatos();
        
        // para verificar que cargó
        if (sistema.getEstadisticas().obtenerDatos().isEmpty()) {
            System.out.println("Sistema iniciado sin datos previos.");
        } else {
            System.out.println("Datos cargados exitosamente.");
            sistema.getEstadisticas().obtenerDatos().forEach((tipo, peso) -> 
                System.out.println("-> " + tipo + ": " + peso + "kg")
            );
        }
    }

    @FXML
    private void switchToSecondary() throws IOException {
        // Guardar los datos antes de cambiar de ventana para no perder nada
        System.out.println("Guardando datos antes de cambiar de ventana...");
        sistema.guardarDatos();
        
        App.setRoot("secondary");
    }

    // para probar que tu lógica funciona.
    // (Para usarlo, deberías crear un botón en tu 'primary.fxml' que diga onAction="#probarLogica")
    @FXML
    private void probarLogica() {
        Residuo r = new Residuo("TEST-01", "Botella Fanta", "Plastico", 0.5, "Zona Norte", 1);
        
        sistema.agregarResiduo(r);
        
        System.out.println("¡Prueba exitosa! Residuo agregado.");
        sistema.guardarDatos();
    }
}