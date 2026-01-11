package ecotrack.main;

import ecotrack.logica.Residuo;
import ecotrack.services.SistemaEcoTrack;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static SistemaEcoTrack sistema;

    @Override
    public void start(Stage stage) throws IOException {

        sistema = SistemaEcoTrack.cargarDatos();

        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("/com/mycompany/ecotrack/primary.fxml")
        );
        Parent root = loader.load();
        PrimaryController controller = loader.getController();
        controller.setSistema(sistema);

        scene = new Scene(root, 640, 480);
        stage.setTitle("EcoTrack");
        stage.setScene(scene);

        stage.setOnCloseRequest(e -> {
            sistema.guardarDatos();
        });
        
        // Agregar residuos de ejemplo SOLO si el sistema está vacío (primera ejecución)
        // Esto evita que se agreguen residuos duplicados cada vez que se ejecuta la app
        if (sistema.getListaResiduos().isEmpty() && sistema.getCentroReciclaje().estaVacio()) {
            // Crear zona de ejemplo si no existe
            if (!sistema.getMapaZonas().containsKey("Zona Norte")) {
                ecotrack.logica.Zona zonaNorte = new ecotrack.logica.Zona("Zona Norte");
                sistema.agregarZona(zonaNorte);
                System.out.println("Zona 'Zona Norte' creada para datos de ejemplo");
            }
            
            // Agregar residuos de ejemplo para pruebas iniciales
            // R1: Va directamente al centro de reciclaje (ya procesado, NO se registra en estadísticas)
            sistema.getCentroReciclaje().recibirResiduo(
                    new Residuo("R1", "Botella", "Plástico", 2.5, "Zona Norte", 1)
            );
            // B1: Va a la lista de residuos (la calle), SÍ se registra en estadísticas
            sistema.agregarResiduo(new Residuo("B1", "Botella", "Plástico", 1.5, "Zona Norte", 2));
            
            System.out.println("Sistema inicializado con residuos de ejemplo (primera ejecución)");
        } else {
            System.out.println("Sistema cargado desde archivo. Residuos existentes: " + 
                             sistema.obtenerResiduosComoLista().size() + 
                             " | Centro reciclaje: " + sistema.getCentroReciclaje().cantidadPendiente());
        }

        stage.show();
    }

    // Método de apoyo 
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/com/mycompany/ecotrack/" + fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
