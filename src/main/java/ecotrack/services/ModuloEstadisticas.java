package ecotrack.services;

import ecotrack.logica.Residuo;
import java.util.HashMap;
// import java.util.Map; // <-- BORRAR ESTO

import java.io.Serializable;

public class ModuloEstadisticas implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Declaración concreta
    private HashMap<String, Double> estadisticasPeso;

    public ModuloEstadisticas() {
        this.estadisticasPeso = new HashMap<>();
    }

    public void registrarEstadistica(Residuo r) {
        double pesoActual = estadisticasPeso.getOrDefault(r.getTipo(), 0.0);
        estadisticasPeso.put(r.getTipo(), pesoActual + r.getPeso());
    }
    
    // CAMBIO: Retornar HashMap en lugar de Map
    public HashMap<String, Double> obtenerDatos() {
        return estadisticasPeso;
    }
}