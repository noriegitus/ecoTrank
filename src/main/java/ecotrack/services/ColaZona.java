package ecotrack.services;

import java.io.Serializable;
import java.util.PriorityQueue;
import ecotrack.logica.Zona;

public class ColaZona implements Serializable {
    private static final long serialVersionUID = 1L; // Buena práctica agregarlo
    private PriorityQueue<Zona> coladePrioridad;

    public ColaZona(){
        // ANTES (Causaba el error):
        // coladePrioridad = new PriorityQueue<>((z1, z2) -> Double.compare(...));
        
        // AHORA (Solución):
        // Usamos la clase que acabamos de crear, que SÍ es serializable
        coladePrioridad = new PriorityQueue<>(new ComparadorZona());
    }

    public void agregarZona(Zona zona){
        coladePrioridad.add(zona);
    }

    public Zona despacharSiguienteVehiculo(){
        return coladePrioridad.poll();
    }

    public void actualizarPrioridadZona(Zona z){
        if(coladePrioridad.remove(z)){
            coladePrioridad.add(z);
        }
    }
}