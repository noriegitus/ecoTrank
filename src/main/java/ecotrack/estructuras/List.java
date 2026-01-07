package ecotrack.estructuras;

public interface List<E> extends Iterable<E> {
    
    // Metodos Base 
    
    boolean addFirst(E e);
    boolean addLast(E e);
    E removeFirst();
    E removeLast();
    int size();
    boolean isEmpty();
    void clear();
    boolean add(int index, E element);
    E remove(int index);
    E get(int index);
    E set(int index, E element);
    
    // Metodos Personalizados
    
    boolean agregarResiduo(E r);
    E buscarResiduo (String id);
    boolean eliminarResiduo (E r);

    @Override
    Iterator<E> iterator();
}
