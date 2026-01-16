package ecotrack.services;

import ecotrack.logica.Residuo;
import java.util.HashMap;

import java.io.Serializable;

public class ModuloEstadisticas implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private HashMap<String, Double> estadisticasPeso;

    public ModuloEstadisticas() {
        this.estadisticasPeso = new HashMap<>();
    }

    public void registrarEstadistica(Residuo r) {
        if (r == null) {
            System.err.println("Error: No se puede registrar estadística de un residuo null");
            return;
        }
        
        String tipo = r.getTipo();
        if (tipo == null || tipo.trim().isEmpty()) {
            System.err.println("Error: El tipo del residuo no puede ser null o vacío");
            return;
        }
        
        double peso = r.getPeso();
        if (peso < 0) {
            System.err.println("Error: El peso del residuo no puede ser negativo. Valor: " + peso);
            return;
        }
        
        double pesoActual = estadisticasPeso.getOrDefault(tipo, 0.0);
        estadisticasPeso.put(tipo, pesoActual + peso);
    }
    
    public HashMap<String, Double> obtenerDatos() {
        return estadisticasPeso;
    }
}