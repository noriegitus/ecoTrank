package ecotrack.services;

import java.io.Serializable;
import ecotrack.estructuras.PriorityQueuePropia;
import ecotrack.logica.Zona;
import java.util.List;

/**
 * Cola de prioridad de zonas usando implementación propia de heap binario.
 * Reemplaza PriorityQueue de Java con nuestra implementación propia.
 */
public class ColaZona implements Serializable {
    private static final long serialVersionUID = 1L;
    private PriorityQueuePropia<Zona> coladePrioridad;

    public ColaZona(){
        coladePrioridad = new PriorityQueuePropia<>(new ComparadorZona());
    }

    public void agregarZona(Zona zona){
        if (zona == null) {
            System.err.println("Error: No se puede agregar una zona null a la cola de prioridad");
            return;
        }
        coladePrioridad.add(zona);
    }

    public Zona despacharSiguienteVehiculo(){
        return coladePrioridad.poll();
    }

    public void actualizarPrioridadZona(Zona z){
        if (z == null) {
            System.err.println("Error: No se puede actualizar la prioridad de una zona null");
            return;
        }
        if(coladePrioridad.remove(z)){
            coladePrioridad.add(z);
        }
    }
    
    public boolean eliminarZona(Zona z) {
        if (z == null) {
            System.err.println("Error: No se puede eliminar una zona null de la cola");
            return false;
        }
        return coladePrioridad.remove(z);
    }
    
    public Zona peek() {
        return coladePrioridad.peek();
    }
    
    
    //Para la GUI
    public List<Zona> getZonas() {
        return coladePrioridad.toList();
    }
}
