/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack.main;

import ecotrack.logica.Zona;
import ecotrack.services.SistemaEcoTrack;
import ecotrack.services.ColaZona;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ZonasController implements SistemaInyectable {

    private SistemaEcoTrack sistema;

    @FXML
    private Label lblZonaPrioritaria;

    @FXML
    private TableView<Zona> tablaZonas;

    @FXML
    private TableColumn<Zona, String> colNombre;

    @FXML
    private TableColumn<Zona, Integer> colPendiente;

    @FXML
    private TableColumn<Zona, Integer> colRecolectado;

    @FXML
    private TableColumn<Zona, Integer> colUtilidad;

    @FXML
    private void initialize() {
        System.out.println("ZonasController: Método initialize() llamado");
        // Solo configurar las columnas aquí, los datos se cargarán en setSistema()
        // No configuramos nada aquí porque sistema aún es null
        // Todo se configurará cuando se llame setSistema()
    }

    @Override
    public void setSistema(SistemaEcoTrack sistema) {
        this.sistema = sistema;
        System.out.println("ZonasController: Sistema inyectado correctamente");
        // Configurar las tablas si las columnas ya están inicializadas
        if (tablaZonas != null && colNombre != null) {
            configurarTabla();
            actualizarVista();
        }
    }

    private void configurarTabla() {
        if (colNombre == null || colPendiente == null || 
            colRecolectado == null || colUtilidad == null) {
            System.err.println("Error: Alguna columna no está inicializada");
            return;
        }
        
        // Usar callbacks directos para mayor compatibilidad con módulos
        colNombre.setCellValueFactory(cellData -> {
            Zona z = cellData.getValue();
            return z != null ? new SimpleStringProperty(z.getNombreZona()) : new SimpleStringProperty("");
        });
        
        colPendiente.setCellValueFactory(cellData -> {
            Zona z = cellData.getValue();
            return z != null ? new SimpleIntegerProperty(z.getPesoPendiente()).asObject() : new SimpleIntegerProperty(0).asObject();
        });
        
        colRecolectado.setCellValueFactory(cellData -> {
            Zona z = cellData.getValue();
            return z != null ? new SimpleIntegerProperty(z.getPesoRecolectado()).asObject() : new SimpleIntegerProperty(0).asObject();
        });
        
        colUtilidad.setCellValueFactory(cellData -> {
            Zona z = cellData.getValue();
            return z != null ? new SimpleIntegerProperty(z.getUtilidad()).asObject() : new SimpleIntegerProperty(0).asObject();
        });
        
        // Formatear columna de utilidad con colores
        colUtilidad.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Zona, Integer>() {
                @Override
                protected void updateItem(Integer utilidad, boolean empty) {
                    super.updateItem(utilidad, empty);
                    if (empty || utilidad == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(String.valueOf(utilidad));
                        // Verde para utilidad positiva, rojo para negativa
                        if (utilidad > 0) {
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                        } else if (utilidad < 0) {
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        } else {
                            setStyle("-fx-text-fill: gray;");
                        }
                    }
                }
            };
        });
    }
    private void actualizarVista() {
        try {
            if (tablaZonas == null) {
                System.err.println("Error: tablaZonas es null");
                return;
            }
            if (sistema == null) {
                System.err.println("Error: Sistema no inicializado en actualizarVista");
                return;
            }

            ColaZona cola = sistema.getColaZonas();
            if (cola == null) {
                System.err.println("Error: Cola de zonas no inicializada");
                return;
            }

            java.util.List<Zona> zonas = cola.getZonas();
            if (zonas == null) {
                System.err.println("Error: Lista de zonas es null");
                tablaZonas.setItems(FXCollections.observableArrayList());
                return;
            }
            
            tablaZonas.setItems(
                FXCollections.observableArrayList(zonas)
            );
            System.out.println("Zonas cargadas: " + zonas.size());

            Zona prioritaria = cola.peek();

            if (prioritaria != null && lblZonaPrioritaria != null) {
                lblZonaPrioritaria.setText(
                    "Zona prioritaria: " + prioritaria.getNombreZona() + 
                    " (Utilidad: " + prioritaria.calcularUtilidad() + ")"
                );
            } else if (lblZonaPrioritaria != null) {
                lblZonaPrioritaria.setText("Zona prioritaria: - (No hay zonas registradas)");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error en actualizarVista: " + e.getMessage());
        }
    }

    @FXML
    private void recolectarZona() {
        try {
            if (sistema == null) {
                new Alert(Alert.AlertType.ERROR,
                    "Error: El sistema no está inicializado.").show();
                return;
            }

            ColaZona cola = sistema.getColaZonas();
            if (cola == null) {
                new Alert(Alert.AlertType.ERROR,
                    "Error: La cola de zonas no está inicializada.").show();
                return;
            }

            Zona z = cola.peek();

            if (z == null) {
                new Alert(Alert.AlertType.WARNING,
                    "No hay zonas registradas").show();
                return;
            }

            sistema.recolectarResiduosDeZona(z);

            new Alert(Alert.AlertType.INFORMATION,
                "Zona '" + z.getNombreZona() + "' recolectada").show();

            actualizarVista();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                "Error al recolectar la zona: " + e.getMessage()).show();
        }
    }

    @FXML
    private void recolectarZonaSeleccionada() {
        Zona seleccionada = tablaZonas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            new Alert(Alert.AlertType.WARNING,
                "Seleccione una zona de la tabla para recolectar.").show();
            return;
        }

        sistema.recolectarResiduosDeZona(seleccionada);

        new Alert(Alert.AlertType.INFORMATION,
            "Zona '" + seleccionada.getNombreZona() + "' recolectada correctamente.").show();

        actualizarVista();
    }

    @FXML
    private void eliminarZona() {
        Zona seleccionada = tablaZonas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            new Alert(Alert.AlertType.WARNING,
                "Seleccione una zona de la tabla para eliminar.").show();
            return;
        }

        // Verificar que la zona esté vacía (sin residuos pendientes ni recolectados)
        if (seleccionada.getP_Pendiente() > 0) {
            new Alert(Alert.AlertType.ERROR,
                "No se puede eliminar la zona '" + seleccionada.getNombreZona() + 
                "' porque tiene " + seleccionada.getP_Pendiente() + 
                " kg de residuos pendientes.").show();
            return;
        }

        // Confirmar eliminación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Eliminar zona?");
        confirmacion.setContentText("¿Está seguro de eliminar la zona '" + 
            seleccionada.getNombreZona() + "'?\n" +
            "Esta acción no se puede deshacer.");

        confirmacion.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                // Remover de la cola de prioridad
                ColaZona cola = sistema.getColaZonas();
                boolean eliminada = cola.eliminarZona(seleccionada);
                
                if (eliminada) {
                    // Remover del mapa
                    sistema.getMapaZonas().remove(seleccionada.getNombreZona());
                    
                    actualizarVista();
                    
                    new Alert(Alert.AlertType.INFORMATION,
                        "Zona '" + seleccionada.getNombreZona() + "' eliminada correctamente.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR,
                        "No se pudo eliminar la zona.").show();
                }
            }
        });
    }

    @FXML
    private void agregarZona() {
        System.out.println("agregarZona() llamado - Botón presionado");
        try {
            if (sistema == null) {
                System.err.println("Error: Sistema es null");
                new Alert(Alert.AlertType.ERROR,
                    "Error: El sistema no está inicializado.").show();
                return;
            }
            System.out.println("Sistema está inicializado, abriendo diálogo...");

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Agregar Nueva Zona");
            dialog.setHeaderText("Ingrese el nombre de la zona");
            dialog.setContentText("Nombre de la zona:");

            dialog.showAndWait().ifPresent(nombreZona -> {
                try {
                    if (nombreZona != null && !nombreZona.trim().isEmpty()) {
                        String nombreTrim = nombreZona.trim();
                        
                        // Verificar si la zona ya existe
                        if (sistema.getMapaZonas().containsKey(nombreTrim)) {
                            new Alert(Alert.AlertType.WARNING,
                                "La zona '" + nombreTrim + "' ya existe.").show();
                            return;
                        }

                        Zona nuevaZona = new Zona(nombreTrim);
                        sistema.agregarZona(nuevaZona);
                        actualizarVista();
                        
                        new Alert(Alert.AlertType.INFORMATION,
                            "Zona '" + nombreTrim + "' agregada correctamente.").show();
                    } else {
                        new Alert(Alert.AlertType.WARNING,
                            "El nombre de la zona no puede estar vacío.").show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,
                        "Error al agregar la zona: " + e.getMessage()).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                "Error al abrir el diálogo: " + e.getMessage()).show();
        }
    }
}
