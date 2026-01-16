package ecotrack.estructuras;

import java.io.Serializable;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;


public class PriorityQueuePropia<E> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Lista simple para almacenar elementos ordenados por prioridad
    private ArrayList<E> elementos;
    private Comparator<E> comparador;
    
    
    public PriorityQueuePropia(Comparator<E> comparador) {
        if (comparador == null) {
            throw new IllegalArgumentException("El comparador no puede ser null");
        }
        this.comparador = comparador;
        this.elementos = new ArrayList<>();
    }
    
    
    public boolean add(E elemento) {
        if (elemento == null) {
            return false;
        }
        if (elementos.isEmpty()) {
            elementos.add(elemento);
            return true;
        }
        int indice = 0;
        for (int i = 0; i < elementos.size(); i++) {
            if (comparador.compare(elemento, elementos.get(i)) < 0) {
                indice = i;
                break;
            }
            indice = i + 1; 
        }
        elementos.add(indice, elemento);
        return true;
    }
    
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        return elementos.remove(0);
    }
   
    public E peek() {
        return isEmpty() ? null : elementos.get(0);
    }
    
    public boolean remove(E elemento) {
        if (elemento == null || isEmpty()) {
            return false;
        }
        return elementos.remove(elemento);
    }
    
    public int size() {
        return elementos.size();
    }
    
    
    public boolean isEmpty() {
        return elementos.isEmpty();
    }
    
    
    public void clear() {
        elementos.clear();
    }
   
    public List<E> toList() {
        return new ArrayList<>(elementos);
    }
}
