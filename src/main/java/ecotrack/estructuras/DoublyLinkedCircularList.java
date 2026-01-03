/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecotrack.estructuras;

/**
 *
 * @author Acosta Allan
 */

import java.util.Iterator;
import java.util.NoSuchElementException;
import ecotrack.logica.Residuo;

public class DoublyLinkedCircularList<E> implements List<E> {

    // Clase interna Nodo (Bidireccional)
    private class Node<E> {
        E content;
        Node<E> next;
        Node<E> prev;

        public Node(E content) {
            this.content = content;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public DoublyLinkedCircularList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // MÉTODOS BASE DE LA INTERFAZ LIST<E>

    @Override
    public boolean addFirst(E e) {
        if (e == null) return false;
        Node<E> newNode = new Node<>(e);
        if (isEmpty()) {
            head = tail = newNode;
            head.next = head;
            head.prev = head; // Cierre circular inicial
        } else {
            newNode.next = head;
            newNode.prev = tail;
            head.prev = newNode;
            tail.next = newNode; // Mantener circularidad
            head = newNode;
        }
        size++;
        return true;
    }

    @Override
    public boolean addLast(E e) {
        if (e == null) return false;
        Node<E> newNode = new Node<>(e);
        if (isEmpty()) {
            head = tail = newNode;
            head.next = head;
            head.prev = head;
        } else {
            newNode.next = head;
            newNode.prev = tail;
            tail.next = newNode;
            head.prev = newNode; // Mantener circularidad
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
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    // Métodos por índice
    @Override
    public boolean add(int index, E element) { 
        throw new UnsupportedOperationException("Usa addFirst o addLast para la entrega actual."); 
    }
    
    @Override
    public E remove(int index) { 
        throw new UnsupportedOperationException("Usa removeFirst o removeLast para la entrega actual."); 
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) return null;
        Node<E> actual = head;
        for (int i = 0; i < index; i++) {
            actual = actual.next;
        }
        return actual.content;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) return null;
        Node<E> actual = head;
        for (int i = 0; i < index; i++) {
            actual = actual.next;
        }
        E oldVal = actual.content;
        actual.content = element;
        return oldVal;
    }

    // MÉTODOS PERSONALIZADOS

    @Override
    public boolean agregarResiduo(E r) {
        return addLast(r); 
    }

    @Override
    public E buscarResiduo(String id) {
        if (isEmpty()) return null;
        
        Node<E> actual = head;
        do {
            // Verificamos si el contenido es un Residuo para acceder a getId()
            if (actual.content instanceof Residuo) {
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
        
        Node<E> actual = head;
        do {
            if (actual.content.equals(r)) {
                // Caso único nodo
                if (size == 1) {
                    head = tail = null;
                    size = 0;
                    return true;
                }
                
                // Ajuste de punteros para eliminar nodo
                if (actual == head) {
                    return removeFirst() != null;
                } else if (actual == tail) {
                    return removeLast() != null;
                } else {
                    // Nodo intermedio
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

    // ITERADOR PERSONALIZADO

    // Clase interna para el iterador solicitado
    public class IteradorResiduos implements Iterator<E> {
        private Node<E> actual;
        private boolean start; // Bandera para manejar la primera vuelta en bucles

        public IteradorResiduos() {
            this.actual = head;
            this.start = true;
        }

        @Override
        public boolean hasNext() {
            // En lista circular no vacía, siempre hay siguiente
            return !isEmpty();
        }

        @Override
        public E next() {
            if (isEmpty()) throw new NoSuchElementException("La lista está vacía");
            E data = actual.content;
            actual = actual.next;
            start = false;
            return data;
        }

        public boolean hasPrevious() {
            return !isEmpty();
        }

        public E previous() {
            if (isEmpty()) throw new NoSuchElementException("La lista está vacía");
            actual = actual.prev;
            return actual.content;
        }
        
        public E peek() {
             if (isEmpty()) return null;
             return actual.content;
        }
    }
    
    @Override
    public Iterator<E> iterator() {
        return new IteradorResiduos();
    }
}