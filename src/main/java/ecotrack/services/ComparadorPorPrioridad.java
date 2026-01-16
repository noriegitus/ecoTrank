package ecotrack.services;

import java.util.Comparator;

import ecotrack.logica.Residuo;

public class ComparadorPorPrioridad implements Comparator<Residuo> {
    public int compare(Residuo a, Residuo b){
        // Manejo de nulls
        if (a == null && b == null) return 0;
        if (a == null) return -1; // null va primero
        if (b == null) return 1;
        
        return Integer.compare(a.getPrioridad(), b.getPrioridad());
    }
}
