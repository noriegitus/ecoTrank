package ecotrack.main;

import ecotrack.services.SistemaEcoTrack; // Importamos el cerebro del sistema
import ecotrack.logica.Residuo;           // Importamos el modelo
import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    // 1. Declaramos la instancia del sistema
    private SistemaEcoTrack sistema;

    // 2. Este método 'initialize' es especial de JavaFX. 
    // Se ejecuta AUTOMÁTICAMENTE justo después de cargar la vista.
    @FXML
    public void initialize() {
        System.out.println("Inicializando vista primaria...");
        
        // Cargamos los datos guardados anteriormente
        sistema = SistemaEcoTrack.cargarDatos();
        
        // (Opcional) Imprimimos en consola para verificar que cargó
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
        // 3. Guardamos los datos antes de cambiar de ventana para no perder nada
        System.out.println("Guardando datos antes de cambiar de ventana...");
        sistema.guardarDatos();
        
        App.setRoot("secondary");
    }

    // 4. Agrega este método nuevo para probar que tu lógica funciona.
    // (Para usarlo, deberías crear un botón en tu 'primary.fxml' que diga onAction="#probarLogica")
    @FXML
    private void probarLogica() {
        // CORRECCIÓN: Se agregó "Zona Norte" como 5to parámetro para coincidir con tu constructor
        Residuo r = new Residuo("TEST-01", "Botella Fanta", "Plastico", 0.5, "Zona Norte", 1);
        
        sistema.agregarResiduo(r);
        
        System.out.println("¡Prueba exitosa! Residuo agregado.");
        sistema.guardarDatos();
    }
}