package ecotrack.services;

import java.util.Comparator;

import ecotrack.logica.Residuo;

public class ComparadorPorPrioridad implements Comparator<Residuo> {
    public int compare(Residuo a, Residuo b){
        return Integer.compare(a.getPrioridad(), b.getPrioridad());
    }
}
