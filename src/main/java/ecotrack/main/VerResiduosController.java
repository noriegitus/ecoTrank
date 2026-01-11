package ecotrack.main;
import ecotrack.estructuras.Iterator;
import ecotrack.logica.Residuo;
import ecotrack.services.SistemaEcoTrack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ecotrack.services.ComparadorPorPeso;
import ecotrack.services.ComparadorPorPrioridad;
import ecotrack.services.ComparadorPorTipo;
import java.util.Collections;
import java.util.Comparator;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class VerResiduosController implements SistemaInyectable {

    private SistemaEcoTrack sistema;

    @FXML
    private TableView<Residuo> tablaResiduos;

    @FXML private TableColumn<Residuo, String> colId;
    @FXML private TableColumn<Residuo, String> colNombre;
    @FXML private TableColumn<Residuo, String> colTipo;
    @FXML private TableColumn<Residuo, String> colZona;
    @FXML private TableColumn<Residuo, Double> colPeso;
    @FXML private TableColumn<Residuo, Integer> colPrioridad;
    @FXML private Label lblDetalle;
    @FXML private ComboBox<String> comboOrdenamiento;
    private Iterator<Residuo> iteradorActual;

    @FXML
    private void initialize() {
        // Este método se llama automáticamente después de que se carguen los campos @FXML
        System.out.println("VerResiduosController: initialize() llamado");
        System.out.println("Estado en initialize(): tablaResiduos=" + tablaResiduos + 
                         ", colId=" + colId + ", sistema=" + sistema);
        
        // Si el sistema ya está configurado (raro pero posible), configurar ahora
        if (sistema != null && tablaResiduos != null && colId != null) {
            System.out.println("Sistema ya disponible en initialize(), configurando ahora...");
            inicializarTabla();
            inicializarComboOrdenamiento();
            cargarDatos();
            if (sistema.getListaResiduos() != null) {
                this.iteradorActual = sistema.getListaResiduos().iterator();
                mostrarResiduoActual();
            }
        } else {
            System.out.println("Esperando a que se llame setSistema()...");
        }
    }

    @Override
    public void setSistema(SistemaEcoTrack sistema) {
        System.out.println("VerResiduosController.setSistema() llamado");
        this.sistema = sistema;
        System.out.println("Estado: tablaResiduos=" + tablaResiduos + ", colId=" + colId);
        
        // Configurar las tablas si las columnas ya están inicializadas
        if (tablaResiduos == null) {
            System.err.println("ERROR: tablaResiduos es null cuando se llama setSistema()");
            return;
        }
        
        if (colId == null) {
            System.err.println("ERROR: colId es null cuando se llama setSistema()");
            System.err.println("Las columnas aún no han sido inyectadas por JavaFX. Esperando...");
            // Intentar de nuevo después de un pequeño delay usando Platform.runLater
            javafx.application.Platform.runLater(() -> {
                if (colId != null) {
                    inicializarTabla();
                    inicializarComboOrdenamiento();
                    cargarDatos();
                    if (sistema.getListaResiduos() != null) {
                        this.iteradorActual = sistema.getListaResiduos().iterator();
                        mostrarResiduoActual();
                    }
                } else {
                    System.err.println("ERROR CRÍTICO: Las columnas nunca se inicializaron");
                }
            });
            return;
        }
        
        inicializarTabla();
        inicializarComboOrdenamiento();
        cargarDatos();
        // Inicializamos el iterador con tu lista circular propia
        if (sistema.getListaResiduos() != null) {
            this.iteradorActual = sistema.getListaResiduos().iterator();
            mostrarResiduoActual();
        }
    }
    
    private void inicializarComboOrdenamiento() {
        if (comboOrdenamiento == null) {
            System.err.println("Error: comboOrdenamiento es null");
            return;
        }
        // Solo agregar items si el ComboBox está vacío para evitar duplicados
        if (comboOrdenamiento.getItems().isEmpty()) {
            comboOrdenamiento.getItems().addAll(
                "Sin ordenar",
                "Por Peso",
                "Por Tipo",
                "Por Prioridad"
            );
        }
        comboOrdenamiento.setValue("Sin ordenar");
        comboOrdenamiento.setOnAction(e -> ordenarTabla());
    }
    
    @FXML
    private void ordenarTabla() {
        if (comboOrdenamiento == null || sistema == null || tablaResiduos == null) {
            System.err.println("Error: Componentes no inicializados en ordenarTabla()");
            return;
        }
        
        String criterio = comboOrdenamiento.getValue();
        if (criterio == null || criterio.equals("Sin ordenar")) {
            cargarDatos();
            return;
        }
        
        ObservableList<Residuo> datos = FXCollections.observableArrayList(
            sistema.obtenerResiduosComoLista()
        );
        
        Comparator<Residuo> comparador = null;
        switch (criterio) {
            case "Por Peso":
                comparador = new ComparadorPorPeso();
                break;
            case "Por Tipo":
                comparador = new ComparadorPorTipo();
                break;
            case "Por Prioridad":
                comparador = new ComparadorPorPrioridad();
                break;
        }
        
        if (comparador != null) {
            Collections.sort(datos, comparador);
        }
        
        tablaResiduos.setItems(datos);
    }

    @FXML
    private void irSiguiente() {
        if (iteradorActual == null && sistema != null && sistema.getListaResiduos() != null) {
            iteradorActual = sistema.getListaResiduos().iterator();
        }
        
        if (iteradorActual != null && iteradorActual.hasNext()) {
            Residuo r = iteradorActual.next();
            actualizarInterfazDetalle(r);
        } else if (lblDetalle != null) {
            lblDetalle.setText("No hay más residuos disponibles.");
        }
    }

    @FXML
    private void irAnterior() {
        if (iteradorActual == null && sistema != null && sistema.getListaResiduos() != null) {
            iteradorActual = sistema.getListaResiduos().iterator();
        }
        
        if (iteradorActual != null && iteradorActual.hasPrevious()) {
            Residuo r = iteradorActual.previous();
            actualizarInterfazDetalle(r);
        } else if (lblDetalle != null) {
            lblDetalle.setText("No hay residuos anteriores disponibles.");
        }
    }

    private void mostrarResiduoActual() {
        if (lblDetalle == null) {
            System.err.println("Error: lblDetalle es null");
            return;
        }
        
        if (iteradorActual != null && iteradorActual.hasNext()) {
            Residuo r = iteradorActual.next();

            // Actualizamos los elementos de la GUI
            actualizarInterfazDetalle(r);

        } else {
            lblDetalle.setText("No hay residuos registrados.");
        }
    }

    private void actualizarInterfazDetalle(Residuo r) {
        if (r == null) {
            return;
        }
        
        if (lblDetalle != null) {
            lblDetalle.setText(String.format("ID: %s | Nombre: %s | Zona: %s | Peso: %.2f kg",
                    r.getId(), r.getNombre(), r.getZona(), r.getPeso()));
        }

        if (tablaResiduos != null) {
            tablaResiduos.getSelectionModel().select(r);
            tablaResiduos.scrollTo(r);
        }
    }

    private void inicializarTabla() {
        if (colId == null || colNombre == null || colTipo == null || 
            colZona == null || colPeso == null || colPrioridad == null) {
            System.err.println("Error: Alguna columna no está inicializada");
            System.err.println("colId=" + colId + ", colNombre=" + colNombre + ", colTipo=" + colTipo + 
                             ", colZona=" + colZona + ", colPeso=" + colPeso + ", colPrioridad=" + colPrioridad);
            return;
        }
        
        System.out.println("Configurando CellValueFactory para las columnas...");
        
        // Usar callbacks directos en lugar de PropertyValueFactory para mayor compatibilidad con módulos
        // Esto evita problemas con reflexión en módulos Java
        try {
            colId.setCellValueFactory(cellData -> {
                Residuo r = cellData.getValue();
                return r != null ? new SimpleStringProperty(r.getId()) : new SimpleStringProperty("");
            });
            
            colNombre.setCellValueFactory(cellData -> {
                Residuo r = cellData.getValue();
                return r != null ? new SimpleStringProperty(r.getNombre()) : new SimpleStringProperty("");
            });
            
            colTipo.setCellValueFactory(cellData -> {
                Residuo r = cellData.getValue();
                return r != null ? new SimpleStringProperty(r.getTipo()) : new SimpleStringProperty("");
            });
            
            colZona.setCellValueFactory(cellData -> {
                Residuo r = cellData.getValue();
                return r != null ? new SimpleStringProperty(r.getZona()) : new SimpleStringProperty("");
            });
            
            colPeso.setCellValueFactory(cellData -> {
                Residuo r = cellData.getValue();
                return r != null ? new SimpleDoubleProperty(r.getPeso()).asObject() : new SimpleDoubleProperty(0.0).asObject();
            });
            
            colPrioridad.setCellValueFactory(cellData -> {
                Residuo r = cellData.getValue();
                return r != null ? new SimpleIntegerProperty(r.getPrioridad()).asObject() : new SimpleIntegerProperty(0).asObject();
            });
            
            System.out.println("CellValueFactory configurado correctamente usando callbacks directos");
        } catch (Exception e) {
            System.err.println("ERROR CRÍTICO al configurar CellValueFactory: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Asegurar que las columnas tengan un ancho mínimo
        if (colId.getPrefWidth() <= 0) colId.setPrefWidth(80);
        if (colNombre.getPrefWidth() <= 0) colNombre.setPrefWidth(120);
        if (colTipo.getPrefWidth() <= 0) colTipo.setPrefWidth(100);
        if (colZona.getPrefWidth() <= 0) colZona.setPrefWidth(120);
        if (colPeso.getPrefWidth() <= 0) colPeso.setPrefWidth(100);
        if (colPrioridad.getPrefWidth() <= 0) colPrioridad.setPrefWidth(80);
        
        System.out.println("Tabla inicializada correctamente");
    }

    private void cargarDatos() {
        if (tablaResiduos == null) {
            System.err.println("Error: tablaResiduos es null");
            return;
        }
        if (sistema == null) {
            System.err.println("Error: sistema es null");
            return;
        }
        
        java.util.List<Residuo> listaResiduos = sistema.obtenerResiduosComoLista();
        System.out.println("Cargando datos - Total residuos en sistema: " + (listaResiduos != null ? listaResiduos.size() : 0));
        
        if (listaResiduos == null || listaResiduos.isEmpty()) {
            System.out.println("ADVERTENCIA: No hay residuos en el sistema");
            tablaResiduos.setItems(FXCollections.observableArrayList());
            return;
        }
        
        // Mostrar detalles de cada residuo para depuración
        for (Residuo r : listaResiduos) {
            System.out.println("Residuo encontrado: ID=" + r.getId() + 
                             ", Nombre=" + r.getNombre() + 
                             ", Tipo=" + r.getTipo() + 
                             ", Zona=" + r.getZona() + 
                             ", Peso=" + r.getPeso() + 
                             ", Prioridad=" + r.getPrioridad());
        }
        
        ObservableList<Residuo> datos = FXCollections.observableArrayList(listaResiduos);
        
        // Verificar que las columnas estén configuradas
        if (colId.getCellValueFactory() == null) {
            System.err.println("ERROR CRÍTICO: colId no tiene CellValueFactory configurado");
            inicializarTabla();
        }
        
        tablaResiduos.setItems(datos);
        System.out.println("Residuos cargados en tabla: " + datos.size());
        System.out.println("Items en tabla: " + tablaResiduos.getItems().size());
    }
    
    @FXML
    private void eliminarResiduo() {
        if (tablaResiduos == null || sistema == null) {
            mostrarAlerta("Error", "El sistema no está inicializado correctamente.");
            return;
        }

        Residuo seleccionado = tablaResiduos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Seleccione un residuo para eliminar.");
            return;
        }

        if (sistema.getListaResiduos() == null) {
            mostrarAlerta("Error", "La lista de residuos no está disponible.");
            return;
        }

        boolean eliminado = sistema.getListaResiduos().eliminarResiduo(seleccionado);

        if (eliminado) {
            tablaResiduos.getItems().remove(seleccionado);
            // Reiniciar iterador después de eliminar
            this.iteradorActual = sistema.getListaResiduos().iterator();
            mostrarAlerta("Éxito", "Residuo eliminado correctamente.");
        } else {
            mostrarAlerta("Error", "No se pudo eliminar el residuo.");
        }
    }

    @FXML
    private void agregarResiduo() {
        if (sistema == null) {
            mostrarAlerta("Error", "El sistema no está inicializado.");
            return;
        }
        
        // Crear diálogo personalizado
        Dialog<Pair<String, Residuo>> dialog = new Dialog<>();
        dialog.setTitle("Agregar Nuevo Residuo");
        dialog.setHeaderText("Ingrese los datos del residuo");

        // Crear campos de entrada
        TextField txtId = new TextField();
        TextField txtNombre = new TextField();
        TextField txtPeso = new TextField();
        TextField txtPrioridad = new TextField();

        // ComboBox para tipos comunes
        ComboBox<String> comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll("Orgánico", "Plástico", "Vidrio", "Electrónico", "Metal", "Papel", "Otro");
        comboTipo.setEditable(true);

        // Obtener zonas disponibles
        ComboBox<String> comboZona = new ComboBox<>();
        if (sistema.getMapaZonas() != null) {
            for (String zona : sistema.getMapaZonas().keySet()) {
                comboZona.getItems().add(zona);
            }
        }
        comboZona.setEditable(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new javafx.scene.control.Label("ID:"), 0, 0);
        grid.add(txtId, 1, 0);
        grid.add(new javafx.scene.control.Label("Nombre:"), 0, 1);
        grid.add(txtNombre, 1, 1);
        grid.add(new javafx.scene.control.Label("Tipo:"), 0, 2);
        grid.add(comboTipo, 1, 2);
        grid.add(new javafx.scene.control.Label("Peso (kg):"), 0, 3);
        grid.add(txtPeso, 1, 3);
        grid.add(new javafx.scene.control.Label("Zona:"), 0, 4);
        grid.add(comboZona, 1, 4);
        grid.add(new javafx.scene.control.Label("Prioridad:"), 0, 5);
        grid.add(txtPrioridad, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Botones
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Validar antes de cerrar
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String id = txtId.getText().trim();
                    String nombre = txtNombre.getText().trim();
                    String tipo = comboTipo.getValue() != null ? comboTipo.getValue() : comboTipo.getEditor().getText().trim();
                    double peso = Double.parseDouble(txtPeso.getText().trim());
                    String zona = comboZona.getValue() != null ? comboZona.getValue() : comboZona.getEditor().getText().trim();
                    int prioridad = Integer.parseInt(txtPrioridad.getText().trim());

                    if (id.isEmpty() || nombre.isEmpty() || tipo.isEmpty() || zona.isEmpty()) {
                        mostrarAlerta("Error", "Todos los campos son obligatorios.");
                        return null;
                    }

                    // Crear zona si no existe
                    if (!sistema.getMapaZonas().containsKey(zona)) {
                        ecotrack.logica.Zona nuevaZona = new ecotrack.logica.Zona(zona);
                        sistema.agregarZona(nuevaZona);
                    }

                    Residuo nuevo = new Residuo(id, nombre, tipo, peso, zona, prioridad);
                    return new Pair<>("OK", nuevo);
                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "El peso y la prioridad deben ser números válidos.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result != null && result.getValue() != null) {
                Residuo nuevo = result.getValue();
                sistema.agregarResiduo(nuevo);
                
                // Actualizar la tabla - mantener el ordenamiento si está activo
                if (comboOrdenamiento != null && !comboOrdenamiento.getValue().equals("Sin ordenar")) {
                    ordenarTabla();
                } else {
                    cargarDatos();
                }
                
                // Reiniciar iterador
                if (sistema.getListaResiduos() != null) {
                    this.iteradorActual = sistema.getListaResiduos().iterator();
                }
                mostrarAlerta("Éxito", "Residuo agregado correctamente.");
            }
        });
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


