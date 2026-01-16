package ecotrack.services;

import java.util.Comparator;

import ecotrack.logica.Residuo;

public class ComparadorPorTipo implements Comparator<Residuo> {
    public int compare(Residuo a, Residuo b){
        // Manejo de nulls
        if (a == null && b == null) return 0;
        if (a == null) return -1; // null va primero
        if (b == null) return 1;
        
        String tipoA = a.getTipo();
        String tipoB = b.getTipo();
        
        if (tipoA == null && tipoB == null) return 0;
        if (tipoA == null) return -1;
        if (tipoB == null) return 1;
        
        return tipoA.compareTo(tipoB);
    }
}
