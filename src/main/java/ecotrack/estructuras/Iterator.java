package ecotrack.estructuras;

public interface Iterator<E> extends java.util.Iterator<E> {
    public boolean hasPrevious();
    public E previous();
    public E peek();
}
 