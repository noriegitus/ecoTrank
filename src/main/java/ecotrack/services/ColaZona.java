package ecotrack.services;

import java.io.Serializable;
import java.util.PriorityQueue;
import ecotrack.logica.Zona;

public class ColaZona implements Serializable {
    private static final long serialVersionUID = 1L;
    private PriorityQueue<Zona> coladePrioridad;

    public ColaZona(){
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