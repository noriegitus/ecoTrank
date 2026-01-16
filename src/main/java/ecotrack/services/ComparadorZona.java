package ecotrack.services;

import ecotrack.logica.Zona;
import java.io.Serializable;
import java.util.Comparator;

public class ComparadorZona implements Comparator<Zona>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Zona z1, Zona z2) {
        // Manejo de nulls
        if (z1 == null && z2 == null) return 0;
        if (z1 == null) return -1; // null va primero
        if (z2 == null) return 1;
        
        return Double.compare(z1.calcularUtilidad(), z2.calcularUtilidad());
    }
}