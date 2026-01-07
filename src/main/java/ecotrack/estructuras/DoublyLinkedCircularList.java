package ecotrack.estructuras;

import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;
import ecotrack.logica.Residuo;

public class DoublyLinkedCircularList<E> implements List<E>, Serializable {
    private static final long serialVersionUID = 1L;

    private class Node implements Serializable {
        private static final long serialVersionUID = 1L; // Buena práctica para nodos serializables
        E content;
        Node next;
        Node prev;

        public Node(E content) {
            this.content = content;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public DoublyLinkedCircularList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // --- ITERADOR ---
    private class IteradorCircular implements Iterator<E> {
        private Node actual;
        private int contador; 

        public IteradorCircular() {
            this.actual = head;
            this.contador = 0;
        }

        @Override
        public boolean hasNext() {
            return !isEmpty() && contador < size;
        }

        @Override
        public E next() {
            if (isEmpty()) throw new NoSuchElementException("La lista está vacía");
            E data = actual.content;
            actual = actual.next;
            contador++;
            return data;
        }

        @Override
        public boolean hasPrevious() {
            return !isEmpty();
        }

        @Override
        public E previous() {
            if (isEmpty()) throw new NoSuchElementException("La lista está vacía");
            actual = actual.prev; 
            if (contador > 0) contador--; 
            return actual.content;
        }

        @Override
        public E peek() {
             if (isEmpty()) return null;
             return actual.content;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new IteradorCircular();
    }

    // --- MÉTODOS BASE ---

    @Override
    public boolean addFirst(E e) {
        if (e == null) return false;
        Node newNode = new Node(e);
        if (isEmpty()) {
            head = tail = newNode;
            head.next = head;
            head.prev = head;
        } else {
            newNode.next = head;
            newNode.prev = tail;
            head.prev = newNode;
            tail.next = newNode;
            head = newNode;
        }
        size++;
        return true;
    }

    @Override
    public boolean addLast(E e) {
        if (e == null) return false;
        Node newNode = new Node(e);
        if (isEmpty()) {
            head = tail = newNode;
            head.next = head;
            head.prev = head;
        } else {
            newNode.next = head;
            newNode.prev = tail;
            tail.next = newNode;
            head.prev = newNode;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) return null;
        E data = head.content;
        if (size == 1) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = tail;
            tail.next = head;
        }
        size--;
        return data;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) return null;
        E data = tail.content;
        if (size == 1) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = head;
            head.prev = tail;
        }
        size--;
        return data;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    @Override
    public boolean add(int index, E element) { 
        throw new UnsupportedOperationException("Usa addFirst o addLast para eficiencia"); 
    }
    
    @Override
    public E remove(int index) { 
        throw new UnsupportedOperationException("Usa removeFirst o removeLast para eficiencia"); 
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) return null;
        if (index == 0) return head.content;
        
        Node actual = head;
        for (int i = 0; i < index; i++) {
            actual = actual.next;
        }
        return actual.content;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) return null;
        Node actual = head;
        for (int i = 0; i < index; i++) {
            actual = actual.next;
        }
        E oldVal = actual.content;
        actual.content = element;
        return oldVal;
    }

    // --- MÉTODOS ESPECÍFICOS DEL PROYECTO (Residuos) ---

    @Override
    public boolean agregarResiduo(E r) {
        return addLast(r); 
    }

    @Override
    public E buscarResiduo(String id) {
        if (isEmpty() || id == null) return null;
        Node actual = head;
        
        // Recorremos hasta dar la vuelta
        do {
            // Verificamos instanceof para seguridad de tipos
            if (actual.content != null && actual.content instanceof Residuo) {
                Residuo r = (Residuo) actual.content;
                if (r.getId().equals(id)) {
                    return actual.content;
                }
            }
            actual = actual.next;
        } while (actual != head);
        
        return null;
    }

    @Override
    public boolean eliminarResiduo(E r) {
        if (isEmpty() || r == null) return false;
        Node actual = head;
        do {
            if (actual.content.equals(r)) {
                if (size == 1) {
                    head = tail = null;
                    size = 0;
                    return true;
                }
                if (actual == head) return removeFirst() != null;
                else if (actual == tail) return removeLast() != null;
                else {
                    actual.prev.next = actual.next;
                    actual.next.prev = actual.prev;
                    size--;
                    return true;
                }
            }
            actual = actual.next;
        } while (actual != head);
        return false;
    }
    
    // --- ORDENAMIENTO (Bubble Sort) ---
    public void sort(Comparator<E> c) {
        if (size < 2) return;
        boolean cambiado;
        do {
            cambiado = false;
            Node actual = head;
            // Iteramos size - 1 veces
            for (int i = 0; i < size - 1; i++) {
                Node siguiente = actual.next;
                if (c.compare(actual.content, siguiente.content) > 0) {
                    // Swap de contenido (más seguro que cambiar punteros en circular)
                    E temp = actual.content;
                    actual.content = siguiente.content;
                    siguiente.content = temp;
                    cambiado = true;
                }
                actual = actual.next;
            }
        } while (cambiado);
    }
}