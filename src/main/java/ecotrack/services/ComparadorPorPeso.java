package ecotrack.services;

import java.util.Comparator;

import ecotrack.logica.Residuo;

public class ComparadorPorPeso implements Comparator<Residuo> {
    public int compare(Residuo a, Residuo b){
        return Double.compare(a.getPeso(), b.getPeso());
    }
}
