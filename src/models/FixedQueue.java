package models;

import java.util.LinkedList;

public class FixedQueue<E> extends LinkedList<E> {

    int maxQueueSize;

    public FixedQueue(int maxSize) {
        this.maxQueueSize = maxSize;
    }

    @Override
    public boolean add(E e) {
        if(size() >= maxQueueSize) {
            removeLast();
        }
        return super.add(e);
    }
}
