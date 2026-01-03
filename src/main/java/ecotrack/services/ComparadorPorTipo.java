package ecotrack.services;

import java.util.Comparator;

import ecotrack.logica.Residuo;

public class ComparadorPorTipo implements Comparator<Residuo> {
    public int compare(Residuo a, Residuo b){
        return a.getTipo().compareTo(b.getTipo());
    }
}
