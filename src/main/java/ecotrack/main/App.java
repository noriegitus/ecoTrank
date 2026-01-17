package ecotrack.main;

import ecotrack.logica.Residuo;
import ecotrack.services.SistemaEcoTrack;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static SistemaEcoTrack sistema;

    @Override
    public void start(Stage stage) throws IOException {
        // Mostrar pantalla de carga primero
        Stage splashStage = mostrarSplashScreen();
        
        // Cargar datos en un hilo separado
        Task<SistemaEcoTrack> loadTask = new Task<SistemaEcoTrack>() {
            @Override
            protected SistemaEcoTrack call() throws Exception {
                // Simular tiempo de carga mínimo para mostrar el splash
                Thread.sleep(1500);
                return SistemaEcoTrack.cargarDatos();
            }
        };
        
        loadTask.setOnSucceeded(e -> {
            sistema = loadTask.getValue();
            cerrarSplashYMostrarAplicacion(splashStage, stage);
        });
        
        loadTask.setOnFailed(e -> {
            sistema = new SistemaEcoTrack();
            cerrarSplashYMostrarAplicacion(splashStage, stage);
        });
        
        new Thread(loadTask).start();
    }
    
    private Stage mostrarSplashScreen() {
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #2e7d32, #43a047); -fx-padding: 40;");
        
        // Título
        Text title = new Text("EcoTrack");
        title.setStyle("-fx-fill: white; -fx-font-size: 48px; -fx-font-weight: bold;");
        
        Text subtitle = new Text("Sistema de Gestión de Residuos");
        subtitle.setStyle("-fx-fill: #c8e6c9; -fx-font-size: 16px;");
        
        // Barra de progreso animada
        ProgressIndicator progress = new ProgressIndicator();
        progress.setStyle("-fx-progress-color: white;");
        
        Text loadingText = new Text("Cargando sistema...");
        loadingText.setStyle("-fx-fill: white; -fx-font-size: 14px;");
        
        vbox.getChildren().addAll(title, subtitle, progress, loadingText);
        
        Scene splashScene = new Scene(vbox, 400, 300);
        splashStage.setScene(splashScene);
        splashStage.setAlwaysOnTop(true);
        splashStage.centerOnScreen();
        splashStage.show();
        
        return splashStage;
    }
    
    private void cerrarSplashYMostrarAplicacion(Stage splashStage, Stage mainStage) {
        Platform.runLater(() -> {
            try {
                splashStage.close();
                
                FXMLLoader loader = new FXMLLoader(
                        App.class.getResource("/com/mycompany/ecotrack/primary.fxml")
                );
                Parent root = loader.load();
                PrimaryController controller = loader.getController();
                controller.setSistema(sistema);

                scene = new Scene(root, 600, 500);
                mainStage.setTitle("EcoTrack");
                mainStage.setScene(scene);

                mainStage.setOnCloseRequest(e -> {
                    if (sistema != null) {
                        sistema.guardarDatos();
                    }
                });
                
                // Agregar datos de ejemplo si es primera ejecución
                if (sistema != null && sistema.getListaResiduos() != null && 
                    sistema.getCentroReciclaje() != null &&
                    sistema.getListaResiduos().isEmpty() && sistema.getCentroReciclaje().estaVacio()) {
                    inicializarDatosEjemplo();
                } else if (sistema != null) {
                    int numResiduos = sistema.getListaResiduos() != null ? sistema.obtenerResiduosComoLista().size() : 0;
                    int cantidadCentro = sistema.getCentroReciclaje() != null ? sistema.getCentroReciclaje().cantidadPendiente() : 0;
                    System.out.println("Sistema cargado desde archivo. Residuos: " + 
                                     numResiduos + 
                                     " | Centro reciclaje: " + cantidadCentro);
                }

                mainStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

    // Método para inicializar datos de ejemplo (solo primera vez)
    private void inicializarDatosEjemplo() {
    }
    public static void main(String[] args) {
        launch();
    }
}
