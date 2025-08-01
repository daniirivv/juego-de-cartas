package es.daniylorena.juegodecartas.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CircularList<T> implements Iterable<T> {

    private final List<T> playerList;
    private int position;

    public CircularList(List<T> elements) {
        this.playerList = new ArrayList<>(elements);
        this.position = 0;
    }

    public CircularList(List<T> elements, T winner) {
        this.playerList = new ArrayList<>(elements);

        this.position = (this.playerList.contains(winner)) ? this.playerList.indexOf(winner) : 0;
    }

    public List<T> getPlayerList() {
        return playerList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public T next() {
        T item = this.playerList.get(this.position);
        position = (position + 1) % this.playerList.size();
        return item;
    }

    public void reset() {
        this.position = 0;
    }

    public boolean add(T item) {
        return this.playerList.add(item);
    }

    public boolean remove(T item) {
        return this.playerList.remove(item);
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularIterator();
    }

    private class CircularIterator implements Iterator<T> {

        private int cursor;

        public CircularIterator() {
            this.cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return !playerList.isEmpty();
        }

        @Override
        public T next() {
            T item = playerList.get(cursor);
            cursor = (cursor + 1) % playerList.size();
            return item;
        }
    }
}
