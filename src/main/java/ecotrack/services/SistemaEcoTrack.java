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
        if (z == null) return;
        
        colaZonas.agregarZona(z);
        
        mapaZonas.put(z.getNombreZona(), z);
    }

    public void agregarResiduo(Residuo r) {
        if (r == null) return;

        // Agregar a la lista circular (la calle)
        listaResiduos.agregarResiduo(r);
        
        // Registrar estadísticas
        estadisticas.registrarEstadistica(r);
        
        // Buscar la zona rápidamente usando HashMap
        Zona zonaCorrespondiente = mapaZonas.get(r.getZona());
        
        if (zonaCorrespondiente != null) {
            // Actualizamos el peso pendiente en la zona
            zonaCorrespondiente.registrarResiduoPendiente((int) r.getPeso());
            
            // Reordenamos la cola de prioridad
            colaZonas.actualizarPrioridadZona(zonaCorrespondiente);
        } else {
            // Manejo de error si la zona no existe
            System.err.println("Alerta: Zona '" + r.getZona() + "' no encontrada para el residuo: " + r.getNombre());
        }
    }
    
    public void recolectarResiduosDeZona(Zona z) {
        if (z == null) return;

        ArrayList<Residuo> residuosEncontrados = new ArrayList<>();

        // Buscar en la lista circular los residuos de esa zona
        for (Residuo r : listaResiduos) {
            if (r.getZona() != null && r.getZona().equalsIgnoreCase(z.getNombreZona())) {
                residuosEncontrados.add(r);
            }
        }

        // Retirar de la calle y llevar al centro
        for (Residuo r : residuosEncontrados) {
            if (listaResiduos.eliminarResiduo(r)) {
                centroReciclaje.recibirResiduo(r);
                z.regitrarResiduoRecolectado((int) r.getPeso());
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
}