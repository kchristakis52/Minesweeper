package com.example.minesweeper;

import java.util.LinkedList;


public class Fifo<E> extends LinkedList<E> {
    private final int limit;


    public Fifo(int limit) {
        this.limit = limit;
    }
    @Override
    public boolean add(E o){
        super.add(o);
        while (size() > limit) { super.remove(); }
        return true;
    }
}
