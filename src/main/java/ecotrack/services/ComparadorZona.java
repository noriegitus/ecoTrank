package ecotrack.services;

import ecotrack.logica.Zona;
import java.io.Serializable;
import java.util.Comparator;

public class ComparadorZona implements Comparator<Zona>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Zona z1, Zona z2) {
        return Double.compare(z1.calcularUtilidad(), z2.calcularUtilidad());
    }
}