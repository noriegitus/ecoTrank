package ecotrack.services; 

import ecotrack.estructuras.DoublyLinkedCircularList;
import ecotrack.logica.Residuo; 
import java.io.Serializable;

public class CentroReciclaje implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // CORRECCIÓN: Usamos TU propia estructura de datos (Pila basada en Lista Enlazada)
    // en lugar de usar ArrayDeque de Java, para cumplir estrictamente el PDF.
    private DoublyLinkedCircularList<Residuo> pilaReciclaje;

    public CentroReciclaje() {
        this.pilaReciclaje = new DoublyLinkedCircularList<>();
    }

    public void recibirResiduo(Residuo residuo) {
        // En una Pila, insertamos al inicio (push)
        pilaReciclaje.addFirst(residuo);
    }

    public Residuo procesarResiduo() {
        // En una Pila, sacamos del inicio (pop)
        return pilaReciclaje.isEmpty() ? null : pilaReciclaje.removeFirst();
    }
    
    public Residuo verSiguiente() {
        // Peek: ver el primero sin sacarlo
        return pilaReciclaje.get(0);
    }
    
    // Método auxiliar para saber cuántos hay (opcional para GUI)
    public int cantidadPendiente() {
        return pilaReciclaje.size();
    }
}