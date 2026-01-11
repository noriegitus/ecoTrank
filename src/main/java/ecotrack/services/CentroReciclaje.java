package ecotrack.services; 

import ecotrack.estructuras.DoublyLinkedCircularList;
import ecotrack.logica.Residuo; 
import java.io.Serializable;

public class CentroReciclaje implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Pila basada en Lista Enlazada
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
        return pilaReciclaje.get(0);
    }
    
    // Método auxiliar para saber cuántos hay
    public int cantidadPendiente() {
        return pilaReciclaje.size();
    }
    
    public boolean estaVacio() {
        return pilaReciclaje.isEmpty();
    }
}