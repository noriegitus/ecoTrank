package ecotrack.main;

import ecotrack.services.SistemaEcoTrack;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class EstadisticaController implements SistemaInyectable {

    private SistemaEcoTrack sistema;

    @FXML
    private TableView<FilaEstadistica> tablaEstadisticas;

    @FXML
    private TableColumn<FilaEstadistica, String> colTipo;

    @FXML
    private TableColumn<FilaEstadistica, Double> colPeso;

    @FXML
    private void initialize() {
        System.out.println("EstadisticaController: initialize() llamado");
        // Solo configurar las columnas aquí, los datos se cargarán en setSistema()
        // No configuramos nada aquí porque sistema aún es null
        // Todo se configurará cuando se llame setSistema()
    }

    @Override
    public void setSistema(SistemaEcoTrack sistema) {
        this.sistema = sistema;
        // Configurar las tablas si las columnas ya están inicializadas
        if (tablaEstadisticas != null && colTipo != null) {
            configurarTabla();
            cargarDatos();
        }
    }

    private void configurarTabla() {
        if (colTipo == null || colPeso == null) {
            System.err.println("Error: Alguna columna no está inicializada");
            return;
        }
        
        // Usar callbacks directos para mayor compatibilidad con módulos
        colTipo.setCellValueFactory(cellData -> {
            FilaEstadistica f = cellData.getValue();
            return f != null ? new SimpleStringProperty(f.getTipo()) : new SimpleStringProperty("");
        });
        
        colPeso.setCellValueFactory(cellData -> {
            FilaEstadistica f = cellData.getValue();
            return f != null ? new SimpleDoubleProperty(f.getPesoTotal()).asObject() : new SimpleDoubleProperty(0.0).asObject();
        });
    }

    private void cargarDatos() {
        if (tablaEstadisticas == null) {
            System.err.println("Error: tablaEstadisticas es null");
            return;
        }
        if (sistema == null) {
            System.err.println("Error: sistema es null");
            tablaEstadisticas.setItems(FXCollections.observableArrayList());
            return;
        }
        
        if (sistema.getEstadisticas() == null) {
            System.err.println("Error: módulo de estadísticas es null");
            tablaEstadisticas.setItems(FXCollections.observableArrayList());
            return;
        }

        var datos = sistema.getEstadisticas().obtenerDatos();
        if (datos == null) {
            System.err.println("Error: datos de estadísticas es null");
            tablaEstadisticas.setItems(FXCollections.observableArrayList());
            return;
        }

        var filas = FXCollections.<FilaEstadistica>observableArrayList();

        for (var entry : datos.entrySet()) {
            filas.add(
                new FilaEstadistica(
                    entry.getKey(),
                    entry.getValue()
                )
            );
        }

        tablaEstadisticas.setItems(filas);
        System.out.println("Estadísticas cargadas: " + filas.size());
    }
}
