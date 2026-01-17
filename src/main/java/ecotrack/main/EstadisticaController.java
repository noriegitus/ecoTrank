package ecotrack.main;

import ecotrack.services.SistemaEcoTrack;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.util.Map;

public class EstadisticaController implements SistemaInyectable {

    private SistemaEcoTrack sistema;

    @FXML
    private TableView<FilaEstadistica> tablaEstadisticas;

    @FXML
    private TableColumn<FilaEstadistica, String> colTipo;

    @FXML
    private TableColumn<FilaEstadistica, Double> colPeso;

    @FXML
    private Pane chartContainer;

    @FXML
    private Button btnToggleChart;

    private boolean mostrandoBarras = false;

    @FXML
    private void initialize() {
        System.out.println("EstadisticaController: initialize() llamado");
    }

    @Override
    public void setSistema(SistemaEcoTrack sistema) {
        this.sistema = sistema;
        // Configurar las tablas si las columnas ya están inicializadas
        if (tablaEstadisticas != null && colTipo != null) {
            configurarTabla();
            cargarDatos();
            //Configurar gráfica por defecto si hay datos
            construirGrafica(true);
        }
    }

    private void configurarTabla() {
        if (colTipo == null || colPeso == null) {
            System.err.println("Error: Alguna columna no está inicializada");
            return;
        }
        
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
            System.err.println("Error: tablaEstadisticas  no existe");
            return;
        }
        if (sistema == null) {
            System.err.println("Error: sistema es vacio");
            tablaEstadisticas.setItems(FXCollections.observableArrayList());
            return;
        }
        
        if (sistema.getEstadisticas() == null) {
            System.err.println("Error: módulo de estadísticas es vacio");
            tablaEstadisticas.setItems(FXCollections.observableArrayList());
            return;
        }

        var datos = sistema.getEstadisticas().obtenerDatos();
        if (datos == null) {
            System.err.println("Error: datos de estadísticas esta vacío");
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

    private void construirGrafica(boolean barras){
        if(chartContainer == null) return;
        if(sistema == null || sistema.getEstadisticas() == null){
            chartContainer.getChildren().clear();
            return;
        }

        var datos = sistema.getEstadisticas().obtenerDatos();
        if (datos == null || datos.isEmpty()) {
            chartContainer.getChildren().clear();
            return;
        }

        Node grafica;
        if(barras){
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Peso por Tipo de Residuo");
            xAxis.setLabel("Tipo");
            yAxis.setLabel("Peso (kg)");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Peso");
            for(Map.Entry<String, Double> e : datos.entrySet()){
                series.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
            }
            barChart.getData().add(series);
            // Hacer que el chart ocupe todo el contenedor
            barChart.setMinSize(0, 0);
            barChart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            barChart.prefWidthProperty().bind(chartContainer.widthProperty());
            barChart.prefHeightProperty().bind(chartContainer.heightProperty());

            grafica = barChart;
            mostrandoBarras = true;
            btnToggleChart.setText("Cambiar a Pastel");
        } else {
            PieChart pie = new PieChart();
            pie.setTitle("Distribución por Tipo");
            for(Map.Entry<String, Double> e : datos.entrySet()){
                pie.getData().add(new PieChart.Data(e.getKey(), e.getValue()));
            }
            // Hacer que el pie ocupe todo el contenedor
            pie.setMinSize(0, 0);
            pie.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            pie.prefWidthProperty().bind(chartContainer.widthProperty());
            pie.prefHeightProperty().bind(chartContainer.heightProperty());

            grafica = pie;
            mostrandoBarras = false;
            btnToggleChart.setText("Cambiar a Barras");
        }

        chartContainer.getChildren().setAll(grafica);
    }

    @FXML
    public void handleToggleChart(ActionEvent event){
        construirGrafica(!mostrandoBarras);
    }
}
