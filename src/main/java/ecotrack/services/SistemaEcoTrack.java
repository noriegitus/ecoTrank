package ecotrack.services;

import ecotrack.estructuras.DoublyLinkedCircularList;
import ecotrack.logica.Residuo;
import ecotrack.logica.Zona;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap; 

public class SistemaEcoTrack implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Estructuras de datos
    private DoublyLinkedCircularList<Residuo> listaResiduos; 
    private ColaZona colaZonas;                               
    private CentroReciclaje centroReciclaje;                  
    private ModuloEstadisticas estadisticas;                  
    
    // Mapa para búsqueda rápida O(1)
    private HashMap<String, Zona> mapaZonas; 

    public SistemaEcoTrack() {
        this.centroReciclaje = new CentroReciclaje();
        this.estadisticas = new ModuloEstadisticas();
        this.listaResiduos = new DoublyLinkedCircularList<>();
        this.colaZonas = new ColaZona();
        this.mapaZonas = new HashMap<>();
    }

    // --- MÉTODOS DE GESTIÓN DE DATOS ---

    public void agregarZona(Zona z) {
        if (z == null) {
            System.err.println("Error: No se puede agregar una zona null");
            return;
        }
        
        String nombreZona = z.getNombreZona();
        if (nombreZona == null || nombreZona.trim().isEmpty()) {
            System.err.println("Error: La zona debe tener un nombre válido");
            return;
        }
        
        colaZonas.agregarZona(z);
        
        mapaZonas.put(nombreZona, z);
    }

    public void agregarResiduo(Residuo r) {
        if (r == null) return;

        // Validar que la zona no sea null antes de buscar
        String zonaResiduo = r.getZona();
        if (zonaResiduo == null || zonaResiduo.trim().isEmpty()) {
            System.err.println("Error: El residuo no tiene una zona válida asignada");
            return;
        }

        // Validar que el peso sea válido antes de procesar
        double peso = r.getPeso();
        if (peso < 0) {
            System.err.println("Error: El peso del residuo no puede ser negativo. Valor: " + peso);
            return;
        }

        // Agregar a la lista circular (la calle)
        listaResiduos.agregarResiduo(r);
        
        // Registrar estadísticas
        estadisticas.registrarEstadistica(r);
        
        // Buscar la zona rápidamente usando HashMap
        Zona zonaCorrespondiente = mapaZonas.get(zonaResiduo);
        
        if (zonaCorrespondiente != null) {
            // Actualizamos el peso pendiente en la zona
            zonaCorrespondiente.registrarResiduoPendiente((int) peso);
            
            // Reordenamos la cola de prioridad
            colaZonas.actualizarPrioridadZona(zonaCorrespondiente);
        } else {
            // Manejo de error si la zona no existe
            System.err.println("Alerta: Zona '" + zonaResiduo + "' no encontrada para el residuo: " + r.getNombre());
        }
    }
    
    public void recolectarResiduosDeZona(Zona z) {
        if (z == null) {
            System.err.println("Error: No se puede recolectar residuos de una zona null");
            return;
        }

        // Validar que la zona existe en el mapa
        String nombreZona = z.getNombreZona();
        if (nombreZona == null || !mapaZonas.containsKey(nombreZona)) {
            System.err.println("Error: La zona '" + nombreZona + "' no existe en el sistema");
            return;
        }

        ArrayList<Residuo> residuosEncontrados = new ArrayList<>();

        // Buscar en la lista circular los residuos de esa zona
        for (Residuo r : listaResiduos) {
            if (r != null && r.getZona() != null && r.getZona().equalsIgnoreCase(nombreZona)) {
                residuosEncontrados.add(r);
            }
        }

        // Retirar de la calle y llevar al centro
        for (Residuo r : residuosEncontrados) {
            if (r != null && listaResiduos.eliminarResiduo(r)) {
                centroReciclaje.recibirResiduo(r);
                double peso = r.getPeso();
                if (peso >= 0) {
                    z.registrarResiduoRecolectado((int) peso);
                }
            }
        }
        
        // Actualizar prioridad tras la recolección (ahora la zona está "más limpia")
        colaZonas.actualizarPrioridadZona(z);
    }
    
    // --- GETTERS ---
    
    public CentroReciclaje getCentroReciclaje() { return centroReciclaje; }
    public ModuloEstadisticas getEstadisticas() { return estadisticas; }
    public DoublyLinkedCircularList<Residuo> getListaResiduos() { return listaResiduos; }
    public ColaZona getColaZonas() { return colaZonas; }
    public HashMap<String, Zona> getMapaZonas() { return mapaZonas; }

    // --- PERSISTENCIA ---
    
    public void guardarDatos() {
        ServicioPersistencia.guardarEstado(this, "ecotrack.dat");
    }

    public static SistemaEcoTrack cargarDatos() {
        Object data = ServicioPersistencia.cargarEstado("ecotrack.dat");
        if (data instanceof SistemaEcoTrack) {
            return (SistemaEcoTrack) data;
        }
        return new SistemaEcoTrack(); 
    }
    
    public ArrayList<Residuo> obtenerResiduosComoLista() {
        ArrayList<Residuo> lista = new ArrayList<>();
        for (Residuo r : listaResiduos) {
            lista.add(r);
        }
        return lista;
    }

}