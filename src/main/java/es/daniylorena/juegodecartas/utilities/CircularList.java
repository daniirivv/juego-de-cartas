package es.daniylorena.juegodecartas.utilities;

import java.util.*;

public class CircularList<T> implements Iterable<T> {

    private final List<T> elements;
    private int position;

    public CircularList(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
        this.position = 0;
    }

    public List<T> getElements() {
        return elements;
    }

    public int getPosition() {
        return position;
    }

    public T next(){
        T item = this.elements.get(this.position);
        position = (position + 1) % this.elements.size();
        return item;
    }

    public void reset(){
        this.position = 0;
    }

    public boolean add(T item){
        return this.elements.add(item);
    }

    public boolean remove(T item){
        return this.elements.remove(item);
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularIterator();
    }

    private class CircularIterator implements Iterator<T>{

        private int cursor;

        public CircularIterator() {
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return !elements.isEmpty();
        }

        @Override
        public T next() {
            T item = elements.get(cursor);
            cursor = (cursor + 1) % elements.size();
            return item;
        }
    }
}
