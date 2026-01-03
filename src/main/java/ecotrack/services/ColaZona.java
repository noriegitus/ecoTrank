package ecotrack.services;

import java.util.PriorityQueue;
import ecotrack.logica.Zona;

public class ColaZona {
    private PriorityQueue<Zona> coladePrioridad;

    public ColaZona(){
        coladePrioridad = new PriorityQueue<>((z1, z2) -> (
            Double.compare(z1.calcularUtilidad(), z2.calcularUtilidad())
        ));
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
